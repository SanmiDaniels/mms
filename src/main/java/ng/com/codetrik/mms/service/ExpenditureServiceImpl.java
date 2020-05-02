
package ng.com.codetrik.mms.service;

import java.util.List;
import java.util.UUID;
import ng.com.codetrik.mms.model.Expenditure;
import ng.com.codetrik.mms.model.enumeration.ExpenditureStatus;
import ng.com.codetrik.mms.model.enumeration.ExpenditureType;
import ng.com.codetrik.mms.model.enumeration.Months;
import ng.com.codetrik.mms.repository.ExpenditureRepository;
import ng.com.codetrik.mms.repository.OperatorRepository;
import ng.com.codetrik.mms.repository.SiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ExpenditureServiceImpl implements ExpenditureService{

    @Autowired
    JavaMailSender emailSender;
    
    @Autowired 
    ExpenditureRepository expenditureRepo;
    
    @Autowired
    OperatorRepository operatorRepo;   
    
    @Autowired
    SiteRepository siteRepo;
    
    private final Logger LOGGER = LoggerFactory.getLogger(ExpenditureServiceImpl.class);
    
    @Override
    public Expenditure createExpenditure(Expenditure exp) {
        var opp = operatorRepo.findByEmail(exp.getOperatorEmail());//get operator associated to this mail
        exp.setOperator(opp);//set the ssociated operator tp this expenditure
        var site = siteRepo.findBySiteCode(exp.getSiteCode());//get site associated to this expenditure
        exp.setSite(site);//set the associated site to this expenditure
        exp.setEnumType(ExpenditureType.values()[exp.getType()]);
        exp.setEnumStatus(ExpenditureStatus.values()[exp.getStatus()]);
        exp.setEnumMonth(Months.values()[exp.getMonth()-1]);
        var expp = expenditureRepo.saveAndFlush(exp);
        var recipients = opp.getRecipient(); //get list of recipients associated to the Operator
        try{
            var message = new SimpleMailMessage();//create simple message instance
            var template = expp.toString();//build template message from toString
            if(!recipients.isEmpty()&& recipients!=null){ //check if the list of recipient is null to avaoid null pointer exception
                var recp = new String[recipients.size()];//create empty array of recipients
                recipients.forEach((r) -> {
                    recp[recipients.indexOf(r)] = r.getEmail();
                });
                message.setTo(recp);
            }else{
                message.setTo(expp.getOperatorEmail());//default send message to the email associated to the operator
            }  
            message.setSubject("Your Company created an expenditure record with the following details: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }
        
        
        return expp;     
    }

    @Override
    public Expenditure updateExpenditire(Expenditure exp) {
       var opp = operatorRepo.findByEmail(exp.getOperatorEmail());//get operator associated to this mail
        exp.setOperator(opp);//set the ssociated operator tp this expenditure
        var site = siteRepo.findBySiteCode(exp.getSiteCode());//get site associated to this expenditure
        exp.setSite(site);//set the associated site to this expenditure
        exp.setEnumType(ExpenditureType.values()[exp.getType()]);
        exp.setEnumStatus(ExpenditureStatus.values()[exp.getStatus()]);
        exp.setEnumMonth(Months.values()[exp.getMonth()-1]);
        var expp = expenditureRepo.saveAndFlush(exp);
        var recipients = opp.getRecipient(); //get list of recipients associated to the Operator
        try{
            var message = new SimpleMailMessage();//create simple message instance
            var template = expp.toString();//build template message from toString
            if(!recipients.isEmpty()&& recipients!=null){ //check if the list of recipient is null to avaoid null pointer exception
                String[] recp = new String[recipients.size()];//create empty array of recipients
                recipients.forEach((r) -> {
                    recp[recipients.indexOf(r)] = r.getEmail();
                });
                message.setTo(recp);
            }else{
                message.setTo(expp.getOperatorEmail());//default send message to the email associated to the operator
            }  
            message.setSubject("Your Company updated an expenditure record with following details: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }
        
        
        return expp; 
    }

    @Override
    public Expenditure queryById(UUID id) {
       var exp = expenditureRepo.findById(id).get();
       exp.setMonth(exp.getEnumMonth().ordinal());
       exp.setType(exp.getEnumType().ordinal());
       exp.setStatus(exp.getEnumStatus().ordinal());
       exp.setOperatorEmail(exp.getOperator().getEmail());
       exp.setSiteCode(exp.getSite().getSiteCode());
       return exp;
    }

    @Override
    public List<Expenditure> queryByRequestorEmail(String requestorEmail) {
        var expenditures = expenditureRepo.findByRequestorEmail(requestorEmail);
        expenditures.forEach((exp)->{
                exp.setMonth(exp.getEnumMonth().ordinal());
                exp.setType(exp.getEnumType().ordinal());
                exp.setStatus(exp.getEnumStatus().ordinal());
                exp.setOperatorEmail(exp.getOperator().getEmail());
                exp.setSiteCode(exp.getSite().getSiteCode());
        });
        return expenditures;
    }
    
}
