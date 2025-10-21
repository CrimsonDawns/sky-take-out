package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * 查询超市订单
     *
     * @param status
     * @param time
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getTimeoutOrder(Integer status, LocalDateTime time);

    /**
     * 查询运送中的订单
     *
     * @param deliveryInProgress
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getDeliveringOrder(Integer deliveryInProgress, LocalDateTime time);

    @Select("select sum(amount) from orders where " +
            "order_time >= #{beginTime} and order_time <= #{endTime} and status = #{status}")
    Double sumTurnover(LocalDateTime beginTime, LocalDateTime endTime, Integer status);
}
