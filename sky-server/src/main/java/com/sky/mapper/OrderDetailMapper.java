package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * 批量插入订单明细
     * @param orderDetails
     */
    void insertBatch(List<OrderDetail> orderDetails);
}
