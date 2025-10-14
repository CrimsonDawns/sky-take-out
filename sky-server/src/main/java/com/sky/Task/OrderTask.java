package com.sky.Task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类，定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理订单超过15分钟的订单
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void processTimeoutOrder() {
        log.info("定时处理超时订单{}", LocalDateTime.now());

        //plusMinutes(-15)让时间减去15分钟
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        //查询超时订单
        List<Orders> ordersList = orderMapper.getTimeoutOrder(Orders.PENDING_PAYMENT, time);

        if (ordersList != null || ordersList.size() > 0) {
            //将订单状态修改为取消
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时");
                orders.setCancelTime(LocalDateTime.now());

                orderMapper.update(orders);
            });
        }
    }

    /**
     * 处理派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void processDelivering() {
        log.info("定时处理处于递送中的订单{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusHours(-1);

        //查询运送中的订单
        List<Orders> ordersList = orderMapper.getDeliveringOrder(Orders.DELIVERY_IN_PROGRESS,time);

        if (ordersList != null || ordersList.size() > 0) {
            //将订单修改为已完成
            ordersList.forEach(orders -> {
                orders.setStatus(Orders.COMPLETED);

                orderMapper.update(orders);
            });
        }
    }
}
