package com.changgou.goods.controller;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
import com.github.pagehelper.PageInfo;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:LJJ
 * @Description:
 * @Date 2020/3/3 10:18
 *****/

@RestController
@RequestMapping("/spu")
@CrossOrigin
public class SpuController {

    @Autowired
    private SpuService spuService;

    /***
     * Spu分页条件搜索实现
     * @param spu
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Spu spu, @PathVariable  int page, @PathVariable  int size){
        //调用SpuService实现分页条件查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(spu, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * Spu分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用SpuService实现分页查询Spu
        PageInfo<Spu> pageInfo = spuService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param spu
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<Spu>> findList(@RequestBody(required = false)  Spu spu){
        //调用SpuService实现条件查询Spu
        List<Spu> list = spuService.findList(spu);
        return new Result<List<Spu>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        //调用SpuService实现根据主键删除
        spuService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改Spu数据
     * @param spu
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody  Spu spu,@PathVariable Long id){
        //设置主键值
        spu.setId(id);
        //调用SpuService实现修改Spu
        spuService.update(spu);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增Spu数据
     * @param spu
     * @return
     */
    @PostMapping
    public Result add(@RequestBody   Spu spu){
        //调用SpuService实现添加Spu
        spuService.add(spu);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询Spu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable Long id){
        //调用SpuService实现根据主键查询Spu
        Spu spu = spuService.findById(id);
        return new Result<Spu>(true,StatusCode.OK,"查询成功",spu);
    }

    /***
     * 查询Spu全部数据
     * @return
     */
    @GetMapping
    public Result<List<Spu>> findAll(){
        //调用SpuService实现查询所有Spu
        List<Spu> list = spuService.findAll();
        return new Result<List<Spu>>(true, StatusCode.OK,"查询成功",list) ;
    }

    @PostMapping("/save")
    public Result saveGoods(@RequestBody Goods goods){
        spuService.add(goods);
        return new Result(true, StatusCode.OK, "添加商品成功!");
    }

    @GetMapping("/goods/{id}")
    public Result<Goods> findGoodsById(@PathVariable("id") Long id){
        Goods goods = spuService.findGoodsById(id);
        return new Result<>(true, StatusCode.OK, "查找商品成功!", goods);
    }

    @PutMapping("/audit/{id}")
    public Result audit(@PathVariable("id") Long id){
        spuService.audit(id);
        return new Result(true, StatusCode.OK, "审核成功");
    }

    @PostMapping("/put/{id}")
    public Result put(@PathVariable("id") Long id){
        spuService.put(id);
        return new Result(true, StatusCode.OK, "上架成功");
    }

    @PostMapping("/pull/{id}")
    public Result pull(@PathVariable("id") Long id){
        spuService.pull(id);
        return new Result(true, StatusCode.OK, "下架成功");
    }

    @PostMapping("/put/many")
    public Result putMany(@RequestBody Long[] ids){
        int count = spuService.putMany(ids);
        return new Result(true, StatusCode.OK, "批量上架 " + count + " 个商品");
    }

    @PostMapping("/pull/many")
    public Result pullMany(@RequestBody Long[] ids){
        int count = spuService.pullMany(ids);
        return new Result(true, StatusCode.OK, "批量下架 " + count + " 个商品");
    }

    @PutMapping("/delete/logic/{id}")
    public Result deleteLogic(@PathVariable("id") Long id){
        spuService.logicDelete(id);
        return new Result(true, StatusCode.OK, "逻辑删除成功!");
    }

    @PutMapping("/restore/{id}")
    public Result restore(@PathVariable("id") Long id){
        spuService.restore(id);
        return new Result(true, StatusCode.OK, "回收成功!");
    }

    @DeleteMapping("/delete/real/{id}")
    public Result realDelete(@PathVariable("id") Long id){
        spuService.realDelete(id);
        return new Result(true, StatusCode.OK, "物理删除成功!");
    }
}