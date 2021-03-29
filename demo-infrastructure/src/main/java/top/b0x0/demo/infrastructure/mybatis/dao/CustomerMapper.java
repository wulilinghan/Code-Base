package top.b0x0.demo.infrastructure.mybatis.dao;


import org.apache.ibatis.annotations.Param;
import top.b0x0.demo.infrastructure.mybatis.domain.Customer;

/**
 * Created on 2020-10-29
 *
 * @author musui
 */
public interface CustomerMapper {
    /**
     * 根据用户Id查询Customer(不查询Address)
     *
     * @param id userid
     * @return customer
     */
    Customer find(long id);

    /**
     * 根据用户Id查询Customer(同时查询Address)
     *
     * @param id userid
     * @return customer
     */
    Customer findWithAddress(@Param("id") long id);

    /**
     * 根据orderId查询Customer
     *
     * @param orderId orderid
     * @return customer
     */
    Customer findByOrderId(long orderId);

    /**
     * 持久化Customer对象
     *
     * @param customer customer
     * @return i
     */
    int save(Customer customer);

    /**
     * 更新Customer对象
     *
     * @param customer customer
     * @return i
     */
    int update(Customer customer);
}