package ng.com.codetrik.mms.service;

import java.util.UUID;
import ng.com.codetrik.mms.model.Recipient;
import ng.com.codetrik.mms.repository.OperatorRepository;
import ng.com.codetrik.mms.repository.RecipientRepository;
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
public class RecipientServiceImpl implements RecipientService{
    @Autowired
    RecipientRepository recipientRepo;
    
    @Autowired
    OperatorRepository operatorRepo;
    
    @Autowired
    JavaMailSender emailSender;
    private final Logger LOGGER = LoggerFactory.getLogger(RecipientServiceImpl.class);
    
    @Override
    public Recipient createRecipient(Recipient recipient) {//do not send to list of recipient
        recipient.setOperator(operatorRepo.findByEmail(recipient.getOperatorEmail()));
        Recipient re = recipientRepo.saveAndFlush(recipient);
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            String template = re.toString();
            message.setTo(re.getOperatorEmail());
            message.setSubject("Your Company added a new recipient with the following details: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        } 
        return re;
    }

    @Override
    public Recipient updateRecipient(Recipient recipient) {//do not send to list of recipient
        recipient.setId(recipient.getId());
        Recipient re = recipientRepo.saveAndFlush(recipient);
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            String template = re.toString();
            message.setTo(re.getOperatorEmail());
            message.setSubject("Your Company added a new recipeint with the following details: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }
        return re;        
    }

    @Override
    public Recipient queryById(UUID id) {
        Recipient re = recipientRepo.findById(id).get();
        re.setOperatorEmail(re.getOperator().getEmail());
        return re;
    }
    

}
