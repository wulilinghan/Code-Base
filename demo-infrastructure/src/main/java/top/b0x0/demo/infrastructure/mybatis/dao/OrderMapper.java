package top.b0x0.demo.infrastructure.mybatis.dao;


import top.b0x0.demo.infrastructure.mybatis.domain.Order;

import java.util.List;

/**
 * Created on 2020-10-29
 */
public interface OrderMapper {
    // 根据订单Id查询
    Order find(long id);
    // 查询一个用户一段时间段内的订单列表
    List<Order> findByCustomerId(long customerId, long startTime, long endTime);
    // 保存一个订单
    long save(Order order);
}