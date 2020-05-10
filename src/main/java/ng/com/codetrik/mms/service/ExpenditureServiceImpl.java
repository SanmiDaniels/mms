
package ng.com.codetrik.mms.service;

import java.util.List;
import java.util.UUID;
import ng.com.codetrik.mms.model.entity.Expenditure;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
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
    public Expenditure createExpenditure(Expenditure expenditure) {
        var operator = operatorRepo.findByEmail(expenditure.getOperatorEmail());//get operator associated to this mail
        expenditure.setOperator(operator);//set the ssociated operator tp this expenditure
        var site = siteRepo.findBySiteCode(expenditure.getSiteCode());//get site associated to this expenditure
        expenditure.setSite(site);//set the associated site to this expenditure
        expenditure.setEnumType(ExpenditureType.values()[expenditure.getType()]);
        expenditure.setEnumStatus(ExpenditureStatus.values()[expenditure.getStatus()]);
        expenditure.setEnumMonth(Months.values()[expenditure.getMonth()-1]);
        var expp = expenditureRepo.saveAndFlush(expenditure);
        var recipients = operator.getRecipient(); //get list of recipients associated to the Operator
        try{
            var message = new SimpleMailMessage();//create simple message instance
            var template = expp.toString();//build template message from toString
            if(recipients!=null){ //check if the list of recipient is null to avaoid null pointer exception
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
    @Override
        public Expenditure updateExpenditire(Expenditure newExpenditure) { 
            var opp = operatorRepo.findByEmail(newExpenditure.getOperatorEmail());//get Operator associated with the new Expenditure
            var existingExpenditure = expenditureRepo.findById(newExpenditure.getId()).get();
            existingExpenditure.setMonth(newExpenditure.getMonth());//reset month of existingExpenditure
            existingExpenditure.setType(newExpenditure.getType());//reset type of existingExpenditure
            existingExpenditure.setStatus(newExpenditure.getStatus());//reset status of existingExpenditure
            existingExpenditure.setOperatorEmail(newExpenditure.getOperatorEmail());//reset operator email of existingExpenditure
            existingExpenditure.setSiteCode(newExpenditure.getSiteCode());//reset sitecode of existingExpenditure
            existingExpenditure.setRequestorEmail(newExpenditure.getRequestorEmail());//reset requestor email of existingExpenditure
            existingExpenditure.setAmmount(newExpenditure.getAmmount());//reset amount of existingExpenditure
            existingExpenditure.setEnumType(ExpenditureType.values()[newExpenditure.getType()]);//reset emumtype of existingExpenditure
            existingExpenditure.setEnumStatus(ExpenditureStatus.values()[newExpenditure.getStatus()]);//reset enum stattus of existingExpenditure
            existingExpenditure.setEnumMonth(Months.values()[newExpenditure.getMonth()-1]);//reset emun month of existingExpenditure
            existingExpenditure.setName(newExpenditure.getName());//reset name of existingExpenditure
            existingExpenditure.setYear(newExpenditure.getYear());//reset year of existingExpenditure
            existingExpenditure.setOperator(opp);//set the ssociated operator tp this expenditure
            existingExpenditure.setSite(siteRepo.findBySiteCode(newExpenditure.getSiteCode()));//reset the associated site to this expenditure
            existingExpenditure.setDescription(newExpenditure.getDescription());//reset description of existingExpenditure             
            var  expenditure = expenditureRepo.saveAndFlush(existingExpenditure);  
            var recipients = opp.getRecipient(); //get list of recipients associated to the Operator of the new Expenditure
                    try{
                       var message = new SimpleMailMessage();//create simple message instance
                       var template = expenditure.toString();//build template message from toString
                       if(recipients!=null){ //check if the list of recipient is null to avaoid null pointer exception
                            String[] recp = new String[recipients.size()];//create empty array of recipients
                            recipients.forEach((r) -> {
                               recp[recipients.indexOf(r)] = r.getEmail();
                            });
                            message.setTo(recp);
                        }else{
                            message.setTo(newExpenditure.getOperatorEmail());//default send message to the email associated to the new operator
                            }  
                        message.setSubject("Your Company updated an expenditure record with following details: "); 
                        message.setText(template);
                        emailSender.send(message);
                        }catch(MailException e){
                            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
                             }
            return expenditure;                     
        }    
    
}
