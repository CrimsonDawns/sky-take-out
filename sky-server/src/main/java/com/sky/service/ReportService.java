package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    /**
     * 统计营业额
     *
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计用户数量
     *
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 订单数量统计
     *
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计销量前 10 的商品
     *
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10Statistics(LocalDate begin, LocalDate end);
}
