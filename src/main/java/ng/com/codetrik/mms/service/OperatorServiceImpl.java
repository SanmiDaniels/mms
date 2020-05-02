package ng.com.codetrik.mms.service;

import java.util.List;
import java.util.UUID;
import ng.com.codetrik.mms.model.Operator;
import ng.com.codetrik.mms.model.Recipient;
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
        Operator opp = operatorRepo.saveAndFlush(operator);
        try{//senting mail to the operator email address once persisting is sucessful
            SimpleMailMessage message = new SimpleMailMessage(); //use simple message to generate mesg
            String template = opp.toString();
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
    //updating of an operator
    @Override
    public Operator updateOperator(Operator operator) {
        operator.setId(operator.getId());
        Operator opp = operatorRepo.saveAndFlush(operator);
        List<Recipient> recipients = opp.getRecipient(); //get recipients associated to this operator 
        try{
            SimpleMailMessage message = new SimpleMailMessage();//create simple message instance
            String template = opp.toString();//build template message from toString            
            if(!recipients.isEmpty()&& recipients!=null){ //check if the list of recipient is null or empty
                String[] recp = new String[recipients.size()];//create empty array of size recipients
                recipients.forEach((r) -> {
                    recp[recipients.indexOf(r)] = r.getEmail();
                });
                message.setTo(recp);
            }else{
                message.setTo(opp.getEmail());//defaultly send message to the operator email insteat
            }
            message.setSubject("Your Company updated her operator detail: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }      
        return opp;
    }
    
       
}
