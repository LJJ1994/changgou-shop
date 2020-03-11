package com.changgou.order.service;

import com.changgou.order.pojo.TaskHis;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:LJJ
 * @Description:
 * @Date 2020/3/3 10:18
 *****/
public interface TaskHisService {

    /***
     * TaskHis多条件分页查询
     * @param taskHis
     * @param page
     * @param size
     * @return
     */
    PageInfo<TaskHis> findPage(TaskHis taskHis, int page, int size);

    /***
     * TaskHis分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<TaskHis> findPage(int page, int size);

    /***
     * TaskHis多条件搜索方法
     * @param taskHis
     * @return
     */
    List<TaskHis> findList(TaskHis taskHis);

    /***
     * 删除TaskHis
     * @param id
     */
    void delete(Long id);

    /***
     * 修改TaskHis数据
     * @param taskHis
     */
    void update(TaskHis taskHis);

    /***
     * 新增TaskHis
     * @param taskHis
     */
    void add(TaskHis taskHis);

    /**
     * 根据ID查询TaskHis
     * @param id
     * @return
     */
     TaskHis findById(Long id);

    /***
     * 查询所有TaskHis
     * @return
     */
    List<TaskHis> findAll();
}