package com.changgou.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.order.pojo.Task;
import com.changgou.user.dao.PointLogMapper;
import com.changgou.user.dao.UserMapper;
import com.changgou.user.pojo.PointLog;
import com.changgou.user.pojo.User;
import com.changgou.user.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/****
 * @Author:LJJ
 * @Description: User业务层接口实现类
 * @Date 2020/3/3 10:18
 *****/

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointLogMapper pointLogMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * User条件+分页查询
     * @param user 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<User> findPage(User user, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(user);
        //执行搜索
        return new PageInfo<User>(userMapper.selectByExample(example));
    }

    /**
     * User分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<User> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<User>(userMapper.selectAll());
    }

    /**
     * User条件查询
     * @param user
     * @return
     */
    @Override
    public List<User> findList(User user){
        //构建查询条件
        Example example = createExample(user);
        //根据构建的条件查询数据
        return userMapper.selectByExample(example);
    }


    /**
     * User构建查询对象
     * @param user
     * @return
     */
    public Example createExample(User user){
        Example example=new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if(user!=null){
            // 用户名
            if(!StringUtils.isEmpty(user.getUsername())){
                    criteria.andLike("username","%"+user.getUsername()+"%");
            }
            // 密码，加密存储
            if(!StringUtils.isEmpty(user.getPassword())){
                    criteria.andEqualTo("password",user.getPassword());
            }
            // 注册手机号
            if(!StringUtils.isEmpty(user.getPhone())){
                    criteria.andEqualTo("phone",user.getPhone());
            }
            // 注册邮箱
            if(!StringUtils.isEmpty(user.getEmail())){
                    criteria.andEqualTo("email",user.getEmail());
            }
            // 创建时间
            if(!StringUtils.isEmpty(user.getCreated())){
                    criteria.andEqualTo("created",user.getCreated());
            }
            // 修改时间
            if(!StringUtils.isEmpty(user.getUpdated())){
                    criteria.andEqualTo("updated",user.getUpdated());
            }
            // 会员来源：1:PC，2：H5，3：Android，4：IOS
            if(!StringUtils.isEmpty(user.getSourceType())){
                    criteria.andEqualTo("sourceType",user.getSourceType());
            }
            // 昵称
            if(!StringUtils.isEmpty(user.getNickName())){
                    criteria.andEqualTo("nickName",user.getNickName());
            }
            // 真实姓名
            if(!StringUtils.isEmpty(user.getName())){
                    criteria.andLike("name","%"+user.getName()+"%");
            }
            // 使用状态（1正常 0非正常）
            if(!StringUtils.isEmpty(user.getStatus())){
                    criteria.andEqualTo("status",user.getStatus());
            }
            // 头像地址
            if(!StringUtils.isEmpty(user.getHeadPic())){
                    criteria.andEqualTo("headPic",user.getHeadPic());
            }
            // QQ号码
            if(!StringUtils.isEmpty(user.getQq())){
                    criteria.andEqualTo("qq",user.getQq());
            }
            // 手机是否验证 （0否  1是）
            if(!StringUtils.isEmpty(user.getIsMobileCheck())){
                    criteria.andEqualTo("isMobileCheck",user.getIsMobileCheck());
            }
            // 邮箱是否检测（0否  1是）
            if(!StringUtils.isEmpty(user.getIsEmailCheck())){
                    criteria.andEqualTo("isEmailCheck",user.getIsEmailCheck());
            }
            // 性别，1男，0女
            if(!StringUtils.isEmpty(user.getSex())){
                    criteria.andEqualTo("sex",user.getSex());
            }
            // 会员等级
            if(!StringUtils.isEmpty(user.getUserLevel())){
                    criteria.andEqualTo("userLevel",user.getUserLevel());
            }
            // 积分
            if(!StringUtils.isEmpty(user.getPoints())){
                    criteria.andEqualTo("points",user.getPoints());
            }
            // 经验值
            if(!StringUtils.isEmpty(user.getExperienceValue())){
                    criteria.andEqualTo("experienceValue",user.getExperienceValue());
            }
            // 出生年月日
            if(!StringUtils.isEmpty(user.getBirthday())){
                    criteria.andEqualTo("birthday",user.getBirthday());
            }
            // 最后登录时间
            if(!StringUtils.isEmpty(user.getLastLoginTime())){
                    criteria.andEqualTo("lastLoginTime",user.getLastLoginTime());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改User
     * @param user
     */
    @Override
    public void update(User user){
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 增加User
     * @param user
     */
    @Override
    public void add(User user){
        userMapper.insert(user);
    }

    /**
     * 根据ID查询User
     * @param id
     * @return
     */
    @Override
    public User findById(String id){
        return  userMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询User全部数据
     * @return
     */
    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    /***
     * 添加用户积分
     * @param username
     * @param pint
     * @return
     */
    @Override
    public int addUserPoints(String username, Integer pint) {
        return userMapper.addUserPoints(username,pint);
    }

    /**
     * 更新用户积分
     * @param task
     * @return
     */
    @Override
    @Transactional
    public int updateUserPoints(Task task) {
        System.out.println("用户服务现在开始对任务进行处理");
        //1. 从task中获取相关数据
        Map map = JSON.parseObject(task.getRequestBody(), Map.class);
        String username = (String) map.get("username");
        String orderId = (String) map.get("orderId");
        int point = (int) map.get("point");

        //2. 判断当前任务是否操作过
        PointLog pointLog = pointLogMapper.findPointLogByOrderId(orderId);
        if(pointLog != null){
            return 0;
        }
        //3.将任务存入redis
        redisTemplate.boundValueOps(task.getId()).set("exist", 30, TimeUnit.SECONDS);

        //4.修改用户积分
        int points = userMapper.addUserPoints(username, point);
        if(points <= 0){
            return 0;
        }

        //5.记录日志积分表
        PointLog pointLog1 = new PointLog();
        pointLog1.setOrderId(orderId);
        pointLog1.setPoint(point);
        pointLog1.setUserId(username);
        int i = pointLogMapper.insertSelective(pointLog1);
        if(i <= 0){
            return 0;
        }

        //6.删除redis中任务信息
        redisTemplate.delete(task.getId());
        System.out.println("用户服务完成了更改用户积分的操作");
        return 1;
    }
}
