package ng.com.codetrik.mms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ng.com.codetrik.mms.model.entity.Generator;
import ng.com.codetrik.mms.model.entity.Operator;
import ng.com.codetrik.mms.repository.GeneratorRepository;
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
public class GeneratorServiceImpl implements GeneratorService{
    
    @Autowired
    GeneratorRepository genRepo;
    
    @Autowired 
    OperatorRepository operatorRepo;
    
    @Autowired
    SiteRepository siteRepo;
    
    @Autowired
    JavaMailSender emailSender;
    
    @PersistenceContext
    EntityManager em;
    
    private final Logger LOGGER = LoggerFactory.getLogger(GeneratorServiceImpl.class);
     
    @Override
    public Generator createGenerator(Generator newGenerator) {
        var operator = operatorRepo.findByEmail(newGenerator.getOperatorEmail());//get Operator associated to thos generator
        newGenerator.setOperator(operator);//set operator asscociated to this generator
        newGenerator.setSite(siteRepo.findBySiteCode(newGenerator.getSiteCode()));//get and set site associated to this generator
        var generator = genRepo.saveAndFlush(newGenerator);//save generator in to the DB
        sendGeneratorEmail(generator,operator,"Your Company registered a generator with the following details: ");         
        return generator;
    }
    
    @Override
    public Generator updateGenerator(Generator newGenerator) {
        var existingGenerator = genRepo.getOne(newGenerator.getId());
        var operator = operatorRepo.findByEmail(newGenerator.getOperatorEmail());
        existingGenerator.setSiteCode(newGenerator.getSiteCode());//reset sitecode for existing generator
        existingGenerator.setOperatorEmail(newGenerator.getOperatorEmail());//reset operator email for existing generator
        existingGenerator.setName(newGenerator.getName());//reset name for for existing generator
        existingGenerator.setSite(siteRepo.findBySiteCode(newGenerator.getSiteCode()));//reset associated site for for existing generator
        existingGenerator.setCapacity(newGenerator.getCapacity());//reset generator capaciy for existing generator
        existingGenerator.setOperator(operator);//reset associated operator for existing generator
        existingGenerator.setSerialNumber(newGenerator.getSerialNumber());//reset serial number for existing generator
        var generator = genRepo.saveAndFlush(existingGenerator);//update the generator
        sendGeneratorEmail(generator,operator,"Your Company updated generator with the following details: "); 
        return generator;
    }
    
    @Override
    public Generator queryBySerialNumber(String serialNumber) {
        var gen = genRepo.findBySerialNumber(serialNumber);
        gen.setOperatorEmail(gen.getOperator().getEmail());//this made the operator email available during update for email recipients findings
        gen.setSiteCode(gen.getSite().getSiteCode());
        return gen;
    }

    @Override
    public Generator queryById(UUID id) {
        var gen = genRepo.findById(id).get();
        gen.setOperatorEmail(gen.getOperator().getEmail());//this made the operator email available during update for email recipients findings
        gen.setSiteCode(gen.getSite().getSiteCode());
        return gen;
    }
    @Override
    public Map<String, String> nameAndSerialOfGeneratorOnSite(String siteCode) {
        Map<String,String> generatorWithSerialNumber = new HashMap<>();
       var generatorList  = siteRepo.findBySiteCode(siteCode).getGenerator();
       generatorList.forEach((generator)->{
           generatorWithSerialNumber.put(generator.getName(), generator.getSerialNumber());
       });
       return generatorWithSerialNumber;
    } 
    
    @Override
    public List<Generator> generatorsOnSite(String siteCode) {
        return siteRepo.findBySiteCode(siteCode).getGenerator();
    } 
    
    private void sendGeneratorEmail(Generator generator, Operator operator, String subject){
        var recipients = operator.getRecipient(); //get list of recipients associated to the Operator     
        try{
            var message = new SimpleMailMessage();//create simple message instance
            var template = generator.toString();//build template message from toString      
            if(recipients!=null){ //check if the list of recipient is null to avaoid null pointer exception
                var recp = new String[recipients.size()];//create empty array of recipients
                recipients.forEach((r) -> {
                    recp[recipients.indexOf(r)] = r.getEmail();
                });
                message.setTo(recp);
            }else{
                message.setTo(generator.getOperatorEmail());//defaultly send message to the operator email 
            }
            message.setSubject(subject); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }        
    }

}
