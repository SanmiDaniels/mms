package ng.com.codetrik.mms.service;

import ng.com.codetrik.mms.model.entity.Revenue;

public interface RevenueService {
    Revenue createRevenue(Revenue newRevenue);
    Revenue updateRevenue(Revenue newRevenue);
    Revenue queryByEmail(String email);
}
