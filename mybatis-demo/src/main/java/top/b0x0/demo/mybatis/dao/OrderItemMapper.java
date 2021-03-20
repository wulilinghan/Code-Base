package top.b0x0.demo.mybatis.dao;

import org.apache.ibatis.annotations.Param;
import top.b0x0.demo.mybatis.domain.OrderItem;

import java.util.List;

/**
 * Created on 2020-10-29
 */
public interface OrderItemMapper {
    // 根据id查询OrderItem对象
    OrderItem find(long id);
    // 查询指定的订单中的全部OrderItem
    List<OrderItem> findByOrderId(long orderId);
    // 保存一个OrderItem信息
    long save(@Param("orderItem")OrderItem orderItem,
              @Param("orderId") long orderId);
}