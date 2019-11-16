package com.itheima.service;

import java.util.Map;

/**
 * 运营数据统计接口服务
 * @author wangxin
 * @version 1.0
 */
public interface ReportService {
    /**
     * 获取运营数据统计报表
     * @return
     */
    Map<String,Object> getBusinessReportData() throws Exception;
}
