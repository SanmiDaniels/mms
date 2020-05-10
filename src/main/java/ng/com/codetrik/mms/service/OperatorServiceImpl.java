package ng.com.codetrik.mms.service;

import java.util.UUID;
import ng.com.codetrik.mms.model.entity.Operator;
import ng.com.codetrik.mms.repository.OperatorRepository;
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
public class OperatorServiceImpl implements OperatorService{
    /****************Dependencies*********************/
    
    @Autowired
    OperatorRepository operatorRepo;
    
    @Autowired
    public JavaMailSender emailSender; 
    
    private final Logger LOGGER = LoggerFactory.getLogger(OperatorServiceImpl.class);
    /*******************Service methods*************************/
    
    //creation of Operator and sending of email to the operator address if sucessful
    //email should only be sent to the operator email alone upon creation since its highly likely recipeint has not been set for such operator    
    @Override
    public Operator createOperator(Operator operator) {
        var opp = operatorRepo.saveAndFlush(operator);
        try{//senting mail to the operator email address once persisting is sucessful
            var message = new SimpleMailMessage(); //use simple message to generate mesg
            var template = opp.toString();
            message.setTo(opp.getEmail()); 
            message.setSubject("Your Company is added to our platform as mini-grid operator"); 
            message.setText(template);
            emailSender.send(message);            
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }      
        return opp;
    }
    //this have to be called from the frontend before update can be performed
    @Override
    public Operator queryByEmail(String email) {
       return operatorRepo.findByEmail(email);  
    }

    
    @Override
    public Operator queryById(UUID id) {
        return operatorRepo.findById(id).get();
    }
    //updating of an newOperator
    @Override
    public Operator updateOperator(Operator newOperator) {
        var existingOperator = operatorRepo.findById(newOperator.getId()).get();
        existingOperator.setName(newOperator.getName());
        existingOperator.setEmail(newOperator.getEmail());
        existingOperator.setPassword(newOperator.getPassword());
        existingOperator.setSiteCount(newOperator.getSiteCount());
        existingOperator.setAddress(newOperator.getAddress());
        var operator = operatorRepo.saveAndFlush(newOperator);
        var recipients = operator.getRecipient(); //get recipients associated to this newOperator 
        try{
            var message = new SimpleMailMessage();//create simple message instance
            var template = operator.toString();//build template message from toString            
            if(recipients!=null){ //check if the list of recipient is null or empty
                var recp = new String[recipients.size()];//create empty array of size recipients
                recipients.forEach((r) -> {
                    recp[recipients.indexOf(r)] = r.getEmail();
                });
                message.setTo(recp);
            }else{
                message.setTo(operator.getEmail());//defaultly send message to the newOperator email insteat
            }
            message.setSubject("Your Company updated her operator detail: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }      
        return operator;
    }
    
       
}
