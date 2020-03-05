package com.changgou.search.service;

public interface ManagerEsService {
    //创建索引库结构
    void createMappingAndIndex();

    //导入全部数据进入es
    void importAll();
}
