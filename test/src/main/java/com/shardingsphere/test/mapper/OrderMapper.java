package com.shardingsphere.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shardingsphere.test.model.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends BaseMapper<Order> {
}
