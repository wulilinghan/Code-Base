package top.b0x0.demo.mybatis.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020-10-29
 *
 * @author musui
 */
@Data
public class Order {

    private long id;
    private Customer customer;
    private Address deliveryAddress;
    private List<OrderItem> orderItems = new ArrayList<>();
    private long createTime;
    private BigDecimal totalPrice;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", customer=").append(customer);
        sb.append(", deliveryAddress=").append(deliveryAddress);
        sb.append(", orderItems=").append(orderItems);
        sb.append(", createTime=").append(createTime);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append('}');
        return sb.toString();
    }
}