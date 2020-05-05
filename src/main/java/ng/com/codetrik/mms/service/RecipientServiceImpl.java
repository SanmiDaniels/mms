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
    public Recipient createRecipient(Recipient newRecipient) {//do not send to list of newRecipient
        newRecipient.setOperator(operatorRepo.findByEmail(newRecipient.getOperatorEmail()));
        var recipient = recipientRepo.saveAndFlush(newRecipient);
        try{
            var message = new SimpleMailMessage();
            var template = recipient.toString();
            message.setTo(recipient.getOperatorEmail());
            message.setSubject("Your Company added a new recipient with the following details: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        } 
        return recipient;
    }

    @Override
    public Recipient updateRecipient(Recipient newRecipient) {//do not send to list of newRecipient
        var existingRecipient = recipientRepo.findById(newRecipient.getId()).get();
        existingRecipient.setOperatorEmail(newRecipient.getOperatorEmail());//reset the operatorEmail of the existingRecipient
        existingRecipient.setName(newRecipient.getName());//reset the operatorEmail of the existingRecipient
        existingRecipient.setEmail(newRecipient.getEmail());//reset the email of the existingRecipient
        existingRecipient.setRole(newRecipient.getRole());//resett the role of the exixtingRecipient
        existingRecipient.setOperator(operatorRepo.findByEmail(newRecipient.getOperatorEmail()));
        var recipient = recipientRepo.saveAndFlush(existingRecipient);
        try{
            var message = new SimpleMailMessage();
            var template = recipient.toString();
            message.setTo(recipient.getOperatorEmail());
            message.setSubject("Your Company added a new recipeint with the following details: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }
        return recipient;        
    }

    @Override
    public Recipient queryById(UUID id) {
        var recipient = recipientRepo.findById(id).get();
        recipient.setOperatorEmail(recipient.getOperator().getEmail());
        return recipient;
    }
    

}
