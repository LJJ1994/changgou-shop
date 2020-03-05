package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.ESManagerMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.ManagerEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-06 00:55:55
 * @Modified By:
 */
@Service
public class ManagerEsServiceImpl implements ManagerEsService {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private ESManagerMapper esManagerMapper;

    /**
     * 创建ES索引库映射
     */
    @Override
    public void createMappingAndIndex() {
        // 创建skuinfo索引
        elasticsearchTemplate.createIndex(SkuInfo.class);
        // 创建映射
        elasticsearchTemplate.putMapping(SkuInfo.class);
    }

    /**
     * 导入数据到ES索引库
     */
    @Override
    public void importAll() {
        List<Sku> skuList = skuFeign.findListForSearch();
        if(skuList == null || skuList.size() <= 0) {
            throw new RuntimeException("当前没有数据被查询到,无法导入索引库");
        }
        String skuJsonString = JSON.toJSONString(skuList);
        List<SkuInfo> skuInfoList = JSON.parseArray(skuJsonString, SkuInfo.class);
        for(SkuInfo skuInfo : skuInfoList){
            Map map = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(map);
        }
        esManagerMapper.saveAll(skuInfoList);
    }

    /**
     * 通过spuid更新es索引库
     * @param id
     */
    @Override
    public void importDataBySpuId(Long id) {
        List<Sku> skuList = skuFeign.findSkuListBySpuId(id);
        if (skuList == null || skuList.size()<=0){
            throw new RuntimeException("当前没有数据被查询到,无法导入索引库");
        }
        //将集合转换为json
        String jsonSkuList = JSON.toJSONString(skuList);
        List<SkuInfo> skuInfoList = JSON.parseArray(jsonSkuList, SkuInfo.class);

        for (SkuInfo skuInfo : skuInfoList) {
            //将规格信息进行转换
            Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(specMap);
        }

        //添加索引库
        esManagerMapper.saveAll(skuInfoList);
    }

    /**
     * 删除es索引库中的sku
     * @param id
     */
    @Override
    public void deleteDataBySpuId(Long id) {
        List<Sku> skuList = skuFeign.findSkuListBySpuId(id);
        if (skuList == null || skuList.size()<=0){
            throw new RuntimeException("当前没有数据被查询到,无法导入索引库");
        }
        for (Sku sku : skuList) {
            esManagerMapper.deleteById(sku.getId());
        }
    }
}
