package ng.com.codetrik.mms.service;

import ng.com.codetrik.mms.model.Revenue;
import ng.com.codetrik.mms.repository.RevenueRepository;
import ng.com.codetrik.mms.repository.VendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class RevenueServiceImpl implements RevenueService{
    
    @Autowired
    RevenueRepository revenueRepo;
    
    @Autowired
    VendorRepository vendorRepo;
    
    private final Logger LOGGER = LoggerFactory.getLogger(RevenueServiceImpl.class);
    @Override
    public Revenue createRevenue(Revenue newRevenue){ 
        var lastRevenueFromVendor = revenueRepo.findLastRevenue("obasa@gmail.com");//pick last revenue record for specified vendor email
        double difference = 0.00;
        double diff = 0.00;
        newRevenue.setSlipOver(lastRevenueFromVendor.getRollover());       
        if(lastRevenueFromVendor.getExcessOf() > 0.00){//caters for his last over payment if any in the estimation of current expected amount
            diff = newRevenue.getBasic() - lastRevenueFromVendor.getExcessOf();
            if(diff<=0.00){//ensure expected amount is not set to negative
                newRevenue.setExpectedAmount(0.00);//this means the vendor still have excessoff
            }else{
                newRevenue.setExpectedAmount(diff);
            }            
        }else{//caters for his last rollover if there was no over payment in the estimation of curent espected amount
            newRevenue.setExpectedAmount(newRevenue.getBasic() + lastRevenueFromVendor.getRollover());
        }
        if(newRevenue.getExpectedAmount()!= 0.00 && newRevenue.getDepositedAmount() != 0.00){
            difference = newRevenue.getExpectedAmount() - newRevenue.getDepositedAmount();
            if(difference < 0.00){//set current excessOf to estimated value if current deposited amount is greater than current estimated espected amount
                newRevenue.setExcessOf(Math.abs(difference));
                newRevenue.setRollover(0.00);
            }else{//set current rollover to estimated value if current deposited amount is less than current estimated espected amount
                newRevenue.setRollover(difference);
                newRevenue.setExcessOf(0.00);
            }            
        }else if(newRevenue.getExpectedAmount() == 0.00 && newRevenue.getDepositedAmount() != 0.00 
                || newRevenue.getExpectedAmount() == 0.00 && newRevenue.getDepositedAmount() == 0.00){
            
                newRevenue.setExcessOf(Math.abs(diff) + newRevenue.getDepositedAmount());
                newRevenue.setRollover(0.00);
        }        

        newRevenue.setVendor(vendorRepo.findByEmail(newRevenue.getVendorEmail()));//set associated vendor to this revenue
        
        var revenue = revenueRepo.saveAndFlush(newRevenue);
        return revenue;
    }
    
}
