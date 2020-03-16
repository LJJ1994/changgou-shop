package com.changgou.seckill.service;

import com.changgou.entity.SeckillStatus;
import com.changgou.seckill.pojo.SeckillOrder;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:LJJ
 * @Description:
 * @Date 2020/3/3 10:18
 *****/
public interface SeckillOrderService {

    /***
     * SeckillOrder多条件分页查询
     * @param seckillOrder
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size);

    /***
     * SeckillOrder分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillOrder> findPage(int page, int size);

    /***
     * SeckillOrder多条件搜索方法
     * @param seckillOrder
     * @return
     */
    List<SeckillOrder> findList(SeckillOrder seckillOrder);

    /***
     * 删除SeckillOrder
     * @param id
     */
    void delete(Long id);

    /***
     * 修改SeckillOrder数据
     * @param seckillOrder
     */
    void update(SeckillOrder seckillOrder);

    /***
     * 新增SeckillOrder
     * @param
     */
    void add(Long id, String time, String username);

    /**
     * 根据ID查询SeckillOrder
     * @param username
     * @return
     */
     SeckillOrder findById(String username);

    /***
     * 查询所有SeckillOrder
     * @return
     */
    List<SeckillOrder> findAll();

    /***
     * 抢单状态查询
     * @param username
     */
    SeckillStatus queryStatus(String username);

    /**
     * 更新订单状态
     * @param out_trade_no
     * @param transactionId
     * @param username
     */
    void updatePayStatus(String out_trade_no, String transactionId, String username);
}
