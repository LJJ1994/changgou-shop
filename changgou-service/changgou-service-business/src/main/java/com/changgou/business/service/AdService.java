package com.changgou.business.service;

import com.github.pagehelper.Page;
import pojo.Ad;

import java.util.List;
import java.util.Map;

public interface AdService {

    /***
     * 查询所有
     * @return
     */
    List<Ad> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Ad findById(Integer id);

    /***
     * 新增
     * @param ad
     */
    void add(Ad ad);

    /***
     * 修改
     * @param ad
     */
    void update(Ad ad);

    /***
     * 删除
     * @param id
     */
    void delete(Integer id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<Ad> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Ad> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Ad> findPage(Map<String, Object> searchMap, int page, int size);

    /**
     * 通过pisition查询所有广告轮播
     * @param position
     * @return
     */
    List<Ad> findByPosition(String position);
}
