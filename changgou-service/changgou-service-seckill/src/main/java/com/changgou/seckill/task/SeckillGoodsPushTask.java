package com.changgou.seckill.task;

import com.changgou.entity.DateUtil;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description: 该定时任务，每隔xx时间将MySQL数据库中的秒杀商品存入redis
 * @Create: 2020-03-14 14:25:25
 * @Modified By:
 */
@Component
public class SeckillGoodsPushTask {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Scheduled(cron = "0/5 * * * * ?")
    public void loadSecKillGoodsToRedis(){
        /**
         * 1.查询所有符合条件的秒杀商品
         * 	1) 获取时间段集合并循环遍历出每一个时间段
         * 	2) 获取每一个时间段名称,用于后续redis中key的设置
         * 	3) 状态必须为审核通过 status=1
         * 	4) 商品库存个数>0
         * 	5) 秒杀商品开始时间>=当前时间段
         * 	6) 秒杀商品结束<当前时间段+2小时
         * 	7) 排除之前已经加载到Redis缓存中的商品数据
         * 	8) 执行查询获取对应的结果集
         * 2.将秒杀商品存入缓存
         */
        List<Date> dateMenus = DateUtil.getDateMenus();
        for (Date dateMenu : dateMenus) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // redis key
            String secKillKey = DateUtil.date2Str(dateMenu);
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("status", "1"); // 审核通过的商品
            criteria.andGreaterThan("stockCount", 0); // 商品库存大于0
            criteria.andGreaterThanOrEqualTo("startTime", simpleDateFormat.format(dateMenu)); //开始时间
            criteria.andLessThan("endTime", simpleDateFormat1.format(DateUtil.addDateHour(dateMenu, 2)));//结束时间

            Set keys = redisTemplate.boundHashOps("SeckillGoods_" + secKillKey).keys();//查寻redis 已经存在的商品
            if(keys != null && keys.size() > 0){
                criteria.andNotIn("id", keys);
            }
            List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.selectByExample(example);
            for (SeckillGoods seckillGoods : seckillGoodsList) {
                // 添加商品到redis缓存
                redisTemplate.boundHashOps("SeckillGoods_" + secKillKey).put(seckillGoods.getId(), seckillGoods);
                //商品数据队列存储,防止高并发超卖
                Long[] ids= this.pushIds(seckillGoods.getStockCount(), seckillGoods.getId());
                redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillGoods.getId()).leftPushAll(ids);
                // 自增计数器
                redisTemplate.boundHashOps("SeckillGoodsCount").increment(seckillGoods.getId(), seckillGoods.getStockCount());
            }
        }
    }

    /***
     * 将商品ID存入到数组中
     * @param len:长度
     * @param id :值
     * @return
     */
    public Long[] pushIds(int len, Long id){
        Long[] ids = new Long[len];
        for(int i=0; i<len; i++){
            ids[i] = id;
        }
        return ids;
    }
}
