package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SkuMapper;
import com.changgou.goods.dao.SpuMapper;
import com.changgou.goods.pojo.*;
import com.changgou.goods.service.SpuService;
import com.changgou.entity.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/****
 * @Author:LJJ
 * @Description: Spu业务层接口实现类
 * @Date 2020/3/3 10:18
 *****/

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private IdWorker idWorker;

    /**
     * Spu条件+分页查询
     * @param spu 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Spu> findPage(Spu spu, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(spu);
        //执行搜索
        return new PageInfo<Spu>(spuMapper.selectByExample(example));
    }

    /**
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spu> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Spu>(spuMapper.selectAll());
    }

    /**
     * Spu条件查询
     * @param spu
     * @return
     */
    @Override
    public List<Spu> findList(Spu spu){
        //构建查询条件
        Example example = createExample(spu);
        //根据构建的条件查询数据
        return spuMapper.selectByExample(example);
    }


    /**
     * Spu构建查询对象
     * @param spu
     * @return
     */
    public Example createExample(Spu spu){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(spu!=null){
            // 主键
            if(!StringUtils.isEmpty(spu.getId())){
                    criteria.andEqualTo("id",spu.getId());
            }
            // 货号
            if(!StringUtils.isEmpty(spu.getSn())){
                    criteria.andEqualTo("sn",spu.getSn());
            }
            // SPU名
            if(!StringUtils.isEmpty(spu.getName())){
                    criteria.andLike("name","%"+spu.getName()+"%");
            }
            // 副标题
            if(!StringUtils.isEmpty(spu.getCaption())){
                    criteria.andEqualTo("caption",spu.getCaption());
            }
            // 品牌ID
            if(!StringUtils.isEmpty(spu.getBrandId())){
                    criteria.andEqualTo("brandId",spu.getBrandId());
            }
            // 一级分类
            if(!StringUtils.isEmpty(spu.getCategory1Id())){
                    criteria.andEqualTo("category1Id",spu.getCategory1Id());
            }
            // 二级分类
            if(!StringUtils.isEmpty(spu.getCategory2Id())){
                    criteria.andEqualTo("category2Id",spu.getCategory2Id());
            }
            // 三级分类
            if(!StringUtils.isEmpty(spu.getCategory3Id())){
                    criteria.andEqualTo("category3Id",spu.getCategory3Id());
            }
            // 模板ID
            if(!StringUtils.isEmpty(spu.getTemplateId())){
                    criteria.andEqualTo("templateId",spu.getTemplateId());
            }
            // 运费模板id
            if(!StringUtils.isEmpty(spu.getFreightId())){
                    criteria.andEqualTo("freightId",spu.getFreightId());
            }
            // 图片
            if(!StringUtils.isEmpty(spu.getImage())){
                    criteria.andEqualTo("image",spu.getImage());
            }
            // 图片列表
            if(!StringUtils.isEmpty(spu.getImages())){
                    criteria.andEqualTo("images",spu.getImages());
            }
            // 售后服务
            if(!StringUtils.isEmpty(spu.getSaleService())){
                    criteria.andEqualTo("saleService",spu.getSaleService());
            }
            // 介绍
            if(!StringUtils.isEmpty(spu.getIntroduction())){
                    criteria.andEqualTo("introduction",spu.getIntroduction());
            }
            // 规格列表
            if(!StringUtils.isEmpty(spu.getSpecItems())){
                    criteria.andEqualTo("specItems",spu.getSpecItems());
            }
            // 参数列表
            if(!StringUtils.isEmpty(spu.getParaItems())){
                    criteria.andEqualTo("paraItems",spu.getParaItems());
            }
            // 销量
            if(!StringUtils.isEmpty(spu.getSaleNum())){
                    criteria.andEqualTo("saleNum",spu.getSaleNum());
            }
            // 评论数
            if(!StringUtils.isEmpty(spu.getCommentNum())){
                    criteria.andEqualTo("commentNum",spu.getCommentNum());
            }
            // 是否上架
            if(!StringUtils.isEmpty(spu.getIsMarketable())){
                    criteria.andEqualTo("isMarketable",spu.getIsMarketable());
            }
            // 是否启用规格
            if(!StringUtils.isEmpty(spu.getIsEnableSpec())){
                    criteria.andEqualTo("isEnableSpec",spu.getIsEnableSpec());
            }
            // 是否删除
            if(!StringUtils.isEmpty(spu.getIsDelete())){
                    criteria.andEqualTo("isDelete",spu.getIsDelete());
            }
            // 审核状态
            if(!StringUtils.isEmpty(spu.getStatus())){
                    criteria.andEqualTo("status",spu.getStatus());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        spuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Spu
     * @param spu
     */
    @Override
    public void update(Spu spu){
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 增加Spu
     * @param spu
     */
    @Override
    public void add(Spu spu){
        spuMapper.insert(spu);
    }

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
    @Override
    public Spu findById(Long id){
        return  spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Spu全部数据
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 添加商品
     * @param goods
     */
    @Override
    public void add(Goods goods) {
        //保存spu
        Spu spu = goods.getSpu();
        if(spu.getId() == null ){ // 新增
            spu.setId(idWorker.nextId());
            spuMapper.insertSelective(spu);
        } else { // 修改
            spuMapper.updateByPrimaryKeySelective(spu);
            Sku sku = new Sku();
            sku.setSpuId(spu.getId());
            skuMapper.delete(sku);
        }
        //保存sku列表
        List<Sku> skuList = goods.getSkuList();
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        //设置sku名称 spu名称 + 规格参数
        String name = spu.getName();
        Date date = new Date();
        for(Sku sku : skuList) {
            sku.setId(idWorker.nextId());
            if(sku.getSpec() == null || "".equals(sku.getSpec())){
                sku.setSpec("{}");
            }
            Map<String, String> map = JSON.parseObject(sku.getSpec(), Map.class);
            if(map != null || map.size()>0){
                for(String val : map.values()){
                    name += " " + val;
                }
            }
            sku.setSpuId(spu.getId());
            sku.setName(name);
            sku.setCreateTime(date);
            sku.setUpdateTime(date);
            sku.setBrandName(brand.getName());
            sku.setCategoryId(category.getId());
            sku.setCategoryName(category.getName());
            skuMapper.insertSelective(sku);
        }
    }

    /**
     * 查询商品
     * @param id
     * @return
     */
    @Override
    public Goods findGoodsById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);

        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> select = skuMapper.select(sku);

        Goods goods = new  Goods();
        goods.setSpu(spu);
        goods.setSkuList(select);

        return goods;
    }

    /**
     * 商品审核
     * @param id
     */
    @Override
    public void audit(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu.getIsDelete().equalsIgnoreCase("1")){
            throw new RuntimeException("此商品已经下架!");
        }
        spu.setStatus("1"); //已经审核
        spu.setIsMarketable("1"); // 自动上架
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品下架
     * @param id
     */
    @Override
    public void pull(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu == null || "1".equals(spu.getIsDelete())){
            throw new RuntimeException("商品为空或者已经删除!");
        }
        if(!spu.getStatus().equals("1") || !spu.getIsMarketable().equals("1")){
            throw new RuntimeException("此商品未审核通过或者未上架!");
        }
        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品上架
     * @param id
     */
    @Override
    public void put(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu.getIsDelete().equals("1")){
            throw new RuntimeException("此商品已经删除");
        }
        if(!spu.getStatus().equals("1") || spu.getIsMarketable().equals("1")){
            throw new RuntimeException("此商品未审核通过或已经上架!");
        }
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品批量上架
     * @param ids
     * @return
     */
    @Override
    public int putMany(Long[] ids) {
        Spu spu = new Spu();
        spu.setIsMarketable("1");
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        criteria.andEqualTo("isMarketable", "0"); //未上架
        criteria.andEqualTo("isDelete", "0"); // 未删除
        criteria.andEqualTo("status", "1"); //审核通过

        return spuMapper.updateByExampleSelective(spu, example);
    }

    /**
     * 批量下架
     * @param ids
     * @return
     */
    @Override
    public int pullMany(Long[] ids) {
        Spu spu = new Spu();
        spu.setIsMarketable("0");
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        criteria.andEqualTo("isMarketable", "1"); //未上架
        criteria.andEqualTo("isDelete", "0"); // 未删除
        criteria.andEqualTo("status", "1"); //审核通过

        return spuMapper.updateByExampleSelective(spu, example);
    }

    /**
     * 逻辑删除
     * @param id
     */
    @Override
    public void logicDelete(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(!spu.getIsMarketable().equals("0")){
            throw new RuntimeException("必须先将商品下架!");
        }
        spu.setIsDelete("1");
        spu.setStatus("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 还原
     * @param id
     */
    @Override
    public void restore(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(spu.getIsDelete().equals("0")){
            throw new RuntimeException("此商品未删除!");
        }
        spu.setIsDelete("0");
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 物理删除，必须先逻辑删除。
     * @param id
     */
    @Override
    public void realDelete(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(!spu.getIsDelete().equals("1")){
            throw new RuntimeException("此商品未逻辑删除!");
        }
        spuMapper.deleteByPrimaryKey(id);
    }
}
