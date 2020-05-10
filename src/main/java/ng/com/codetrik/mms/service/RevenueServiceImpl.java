package ng.com.codetrik.mms.service;

import ng.com.codetrik.mms.model.entity.Revenue;
import ng.com.codetrik.mms.repository.OperatorRepository;
import ng.com.codetrik.mms.repository.RevenueRepository;
import ng.com.codetrik.mms.repository.VendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    @Autowired
    JavaMailSender emailSender; 
    
    @Autowired
    OperatorRepository operatorRepo;
    
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
        
        var revenue = revenueRepo.saveAndFlush(newRevenue);//save to database 
        //email feature
        var operator = revenue.getVendor().getOperator();
        var recipients =  operator.getRecipient();//get list of recipients associated to the Operator          
        try{
            var message = new SimpleMailMessage();//create simple message instance
            var template = revenue.toString();
            
            if(recipients!=null){ //check if the list of recipient is null to avaoid null pointer exception
                var recp = new String[recipients.size()];//create empty array of recipients
                recipients.forEach((r) -> {
                    recp[recipients.indexOf(r)] = r.getEmail();
                });
                message.setTo(recp);
            }else{
                message.setTo(operator.getEmail());//default send message to the email associated to the operator
            }  
            message.setSubject("Expected revenue today is: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }        
        return revenue;
    }

    @Override
    public Revenue updateRevenue(Revenue newRevenuee) {
        var existingRevenue = revenueRepo.findLastRevenue(newRevenuee.getVendorEmail());//existingRevenue is the lastRevenueFromVendor revenue record
        var secondLastRevenueFromVendor = revenueRepo.findSecondLastRevenue(newRevenuee.getVendorEmail());
        existingRevenue.setDepositedAmount(newRevenuee.getDepositedAmount());
        existingRevenue.setBasic(newRevenuee.getBasic());
            double difference = 0.00;
        double diff = 0.00;
        existingRevenue.setSlipOver(secondLastRevenueFromVendor.getRollover());       
        if(secondLastRevenueFromVendor.getExcessOf() > 0.00){//caters for his last over payment if any in the estimation of current expected amount
            diff = existingRevenue.getBasic() - secondLastRevenueFromVendor.getExcessOf();
            if(diff<=0.00){//ensure expected amount is not set to negative
                existingRevenue.setExpectedAmount(0.00);//this means the vendor still have excessoff
            }else{
                existingRevenue.setExpectedAmount(diff);
            }            
        }else{//caters for his last rollover if there was no over payment in the estimation of curent espected amount
            existingRevenue.setExpectedAmount(existingRevenue.getBasic() + secondLastRevenueFromVendor.getRollover());
        }
        if(existingRevenue.getExpectedAmount()!= 0.00 && existingRevenue.getDepositedAmount() != 0.00){
            difference = existingRevenue.getExpectedAmount() - existingRevenue.getDepositedAmount();
            if(difference < 0.00){//set current excessOf to estimated value if current deposited amount is greater than current estimated espected amount
                existingRevenue.setExcessOf(Math.abs(difference));
                existingRevenue.setRollover(0.00);
            }else{//set current rollover to estimated value if current deposited amount is less than current estimated espected amount
                existingRevenue.setRollover(difference);
                existingRevenue.setExcessOf(0.00);
            }            
        }else if(existingRevenue.getExpectedAmount() == 0.00 && existingRevenue.getDepositedAmount() != 0.00 
                || existingRevenue.getExpectedAmount() == 0.00 && existingRevenue.getDepositedAmount() == 0.00){
            
                existingRevenue.setExcessOf(Math.abs(diff) + existingRevenue.getDepositedAmount());
                existingRevenue.setRollover(0.00);
        }        

        existingRevenue.setVendor(vendorRepo.findByEmail(existingRevenue.getVendorEmail()));//set associated vendor to this revenue
        
        var revenue = revenueRepo.saveAndFlush(existingRevenue);
        
        //email feature
        var operator = revenue.getVendor().getOperator();
        var recipients =  operator.getRecipient();//get list of recipients associated to the Operator          
        try{
            var message = new SimpleMailMessage();//create simple message instance
            var template = revenue.toString();
            
            if(recipients!=null){ //check if the list of recipient is null to avaoid null pointer exception
                var recp = new String[recipients.size()];//create empty array of recipients
                recipients.forEach((r) -> {
                    recp[recipients.indexOf(r)] = r.getEmail();
                });
                message.setTo(recp);
            }else{
                message.setTo(operator.getEmail());//default send message to the email associated to the operator
            }  
            message.setSubject("Expected Revenue updated as follows: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }         
        return revenue;
    }    
    
    @Override
    public Revenue queryByEmail(String email){
        return revenueRepo.findLastRevenue(email);  
    }
    
}
