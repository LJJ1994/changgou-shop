package com.changgou.search.service;

public interface ManagerEsService {
    //创建索引库结构
    void createMappingAndIndex();

    //导入全部数据进入es
    void importAll();

    // 通过spuid更新ES索引库
    void importDataBySpuId(Long id);

    // 通过spuid删除ES索引库
    void deleteDataBySpuId(Long id);
}
