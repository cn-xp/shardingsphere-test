package com.shardingsphere.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shardingsphere.test.mapper.OrderMapper;
import com.shardingsphere.test.mapper.UserMapper;
import com.shardingsphere.test.model.Order;
import com.shardingsphere.test.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.util.*;

@SpringBootTest
public class DemoApplication {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void insertUser() {
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        for (int i = 100; i < 200; i++) {
            User user = new User();
            user.setName("测试" + i);
            user.setSex("男");
            Instant instant = now.minusYears(i).atZone(zoneId).toInstant();
            user.setCreatedTime(Date.from(instant));
            userMapper.insert(user);
        }

    }

    @Test
    public void insertOrder() {
        List<User> users = userMapper.selectAll();
        System.out.println("用户数量：" + users.size());
        for (int i = 0; i < users.size(); i++) {
            Long userId = users.get(i).getId();
            Order order = new Order();
            order.setUserId(userId);
            order.setOrderName("订单" + i);
            orderMapper.insert(order);
        }
    }

    @Test
    public void findAllUserAndOrder() {
        Map<Long, List<Order>> map = new HashMap<>();
        List<User> users = userMapper.selectAll();
        for (int i = 0; i < users.size(); i++) {
            Long userId = users.get(i).getId();
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            List<Order> orders = orderMapper.selectList(wrapper);
            map.put(userId, orders);
        }
    }

    @Test
    public void findUserBetweenCreatedTime() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        LocalDateTime start = LocalDateTime.of(1890, Month.FEBRUARY, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(1920, Month.FEBRUARY, 1, 0, 0, 0);
        ZoneId zoneId = ZoneId.systemDefault();
        Instant startInstant = start.atZone(zoneId).toInstant();
        Instant endInstant = end.atZone(zoneId).toInstant();
        queryWrapper.between("created_time", Date.from(startInstant), Date.from(endInstant));
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println("users:" + users);
    }
}
