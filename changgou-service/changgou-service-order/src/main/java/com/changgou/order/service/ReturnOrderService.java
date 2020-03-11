package com.changgou.order.service;

import com.changgou.order.pojo.ReturnOrder;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:LJJ
 * @Description:
 * @Date 2020/3/3 10:18
 *****/
public interface ReturnOrderService {

    /***
     * ReturnOrder多条件分页查询
     * @param returnOrder
     * @param page
     * @param size
     * @return
     */
    PageInfo<ReturnOrder> findPage(ReturnOrder returnOrder, int page, int size);

    /***
     * ReturnOrder分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<ReturnOrder> findPage(int page, int size);

    /***
     * ReturnOrder多条件搜索方法
     * @param returnOrder
     * @return
     */
    List<ReturnOrder> findList(ReturnOrder returnOrder);

    /***
     * 删除ReturnOrder
     * @param id
     */
    void delete(String id);

    /***
     * 修改ReturnOrder数据
     * @param returnOrder
     */
    void update(ReturnOrder returnOrder);

    /***
     * 新增ReturnOrder
     * @param returnOrder
     */
    void add(ReturnOrder returnOrder);

    /**
     * 根据ID查询ReturnOrder
     * @param id
     * @return
     */
     ReturnOrder findById(String id);

    /***
     * 查询所有ReturnOrder
     * @return
     */
    List<ReturnOrder> findAll();
}
