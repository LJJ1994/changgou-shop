package com.changgou.seckill.controller;

import com.changgou.entity.DateUtil;
import com.changgou.entity.Result;
import com.changgou.seckill.feign.SecKillGoodsFeign;
import com.changgou.seckill.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-14 16:10:10
 * @Modified By:
 */
@Controller
@RequestMapping("/wseckillgoods")
public class SecKillGoodsController {
    @Autowired
    private SecKillGoodsFeign secKillGoodsFeign;

    //跳转秒杀首页
    @RequestMapping("/toIndex")
    public String toIndex(){
        return "seckill-index";
    }

    /**
     * 跳转秒杀商品详情
     * time : 当前秒杀时间段
     * id : 秒杀商品id
     */
    @RequestMapping("/toSeckillItem")
    public String toSeckillItem(@RequestParam("time") String time,
                                @RequestParam("id") Long id,
                                Model model){
        SeckillGoods seckillGoods = secKillGoodsFeign.findOne(time, id);
        model.addAttribute("goods", seckillGoods);
        //开始时间，结束时间
        model.addAttribute("startTime",seckillGoods.getStartTime());
        model.addAttribute("endTime",seckillGoods.getEndTime());
        return "seckill-item";
    }

    //获取秒杀时间段集合信息
    @RequestMapping("/timeMenus")
    @ResponseBody
    public List<String> dateMenus(){

        //获取当前时间段相关的信息集合
        List<Date> dateMenus = DateUtil.getDateMenus();
        List<String> result = new ArrayList<>();

        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date dateMenu : dateMenus) {
            String format = simpleDateFormat.format(dateMenu);
            result.add(format);
        }
        return  result;
    }

    /**
     * 获取秒杀商品列表
     * @param time
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Result<List<SeckillGoods>> list(@RequestParam("time") String time){

        return secKillGoodsFeign.list(DateUtil.formatStr(time));
    }
}
