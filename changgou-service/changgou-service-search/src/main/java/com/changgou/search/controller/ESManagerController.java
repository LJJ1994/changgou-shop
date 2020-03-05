package com.changgou.search.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.search.service.ManagerEsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class ESManagerController {

    @Autowired
    private ManagerEsService managerEsService;

    //创建索引库结构
    @GetMapping("/create")
    public Result create(){
        managerEsService.createMappingAndIndex();
        return new Result(true, StatusCode.OK,"创建索引库结构成功");
    }

    //导入全部数据
    @GetMapping("/importAll")
    public Result importAll(){
        managerEsService.importAll();
        return new Result(true, StatusCode.OK,"导入全部数据成功");
    }
}
