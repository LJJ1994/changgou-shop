package com.changgou.seckill.controller;

import com.changgou.entity.Result;
import com.changgou.entity.SeckillStatus;
import com.changgou.entity.StatusCode;
import com.changgou.seckill.config.TokenDecode;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SeckillOrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:LJJ
 * @Description:
 * @Date 2020/3/3 10:18
 *****/

@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private RedisTemplate redisTemplate;

    /***
     * SeckillOrder分页条件搜索实现
     * @param seckillOrder
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  SeckillOrder seckillOrder, @PathVariable  int page, @PathVariable  int size){
        //调用SeckillOrderService实现分页条件查询SeckillOrder
        PageInfo<SeckillOrder> pageInfo = seckillOrderService.findPage(seckillOrder, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * SeckillOrder分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用SeckillOrderService实现分页查询SeckillOrder
        PageInfo<SeckillOrder> pageInfo = seckillOrderService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param seckillOrder
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<SeckillOrder>> findList(@RequestBody(required = false)  SeckillOrder seckillOrder){
        //调用SeckillOrderService实现条件查询SeckillOrder
        List<SeckillOrder> list = seckillOrderService.findList(seckillOrder);
        return new Result<List<SeckillOrder>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        //调用SeckillOrderService实现根据主键删除
        seckillOrderService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改SeckillOrder数据
     * @param seckillOrder
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody  SeckillOrder seckillOrder,@PathVariable Long id){
        //设置主键值
        seckillOrder.setId(id);
        //调用SeckillOrderService实现修改SeckillOrder
        seckillOrderService.update(seckillOrder);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 秒杀多线程下单
     * @param id
     * @param time
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestParam("id") Long id,
                      @RequestParam("time") String time){
        String username = tokenDecode.getUserInfo().get("username");
        // 匿名用户，表示用户未登录
        if(username.equalsIgnoreCase("anonymousUser")){
            return new Result(false, StatusCode.ACCESSERROR, "请登录");
        }
        seckillOrderService.add(id, time, username);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询SeckillOrder数据
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public Result<SeckillOrder> findById(@PathVariable("username") String username){
        //调用SeckillOrderService实现根据用户名查询redis中的SeckillOrder
        SeckillOrder seckillOrder = seckillOrderService.findById(username);
        return new Result<SeckillOrder>(true,StatusCode.OK,"查询成功",seckillOrder);
    }

    /***
     * 查询SeckillOrder全部数据
     * @return
     */
    @GetMapping
    public Result<List<SeckillOrder>> findAll(){
        //调用SeckillOrderService实现查询所有SeckillOrder
        List<SeckillOrder> list = seckillOrderService.findAll();
        return new Result<List<SeckillOrder>>(true, StatusCode.OK,"查询成功",list) ;
    }

    /**
     * 查询redis 用户抢单状态信息
     * @return
     */
    @GetMapping(value = "/query")
    public Result queryStatus(){
        // 获取用户名
        String username = tokenDecode.getUserInfo().get("username");
        // 匿名用户，表示用户未登录
        if(username.equalsIgnoreCase("anonymousUser")){
            return new Result(false, StatusCode.ACCESSERROR, "请登录");
        }
        SeckillStatus seckillStatus = seckillOrderService.queryStatus(username);
        if (seckillStatus == null){
            return new Result(false, StatusCode.ERROR, "没有抢购信息");
        }
        return new Result(true, StatusCode.OK, "查询抢购状态成功!", seckillStatus);
    }
}
