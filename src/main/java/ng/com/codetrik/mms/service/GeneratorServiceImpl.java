package ng.com.codetrik.mms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import ng.com.codetrik.mms.model.Generator;
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
    
    private final Logger LOGGER = LoggerFactory.getLogger(GeneratorServiceImpl.class);
     
    @Override
    public Generator createGenerator(Generator gen) {
        var opp = operatorRepo.findByEmail(gen.getOperatorEmail());//get Operator associated to thos generator
        gen.setOperator(opp);//set operator asscociated to this generator
        gen.setSite(siteRepo.findBySiteCode(gen.getSiteCode()));//get and set site associated to this generator
        var g = genRepo.saveAndFlush(gen);//save generator in to the DB
        var recipients = opp.getRecipient(); //get list of recipients associated to the Operator
        try{
            var message = new SimpleMailMessage();//create simple message instance
            var template = g.toString();//build template message from toString
            if(!recipients.isEmpty()&& recipients!=null){ //check if the list of recipient is null to avaoid null pointer exception
                var recp = new String[recipients.size()];//create empty array of recipients
                recipients.forEach((r) -> {
                    recp[recipients.indexOf(r)] = r.getEmail();
                });
                message.setTo(recp);
            }else{
                message.setTo(gen.getOperatorEmail());
            }  
            message.setSubject("Your Company added a new generator with the following details: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }         
        return g;
    }

    @Override
    public Generator updateGenerator(Generator newGenerator) {
        var existingGenerator = genRepo.getOne(newGenerator.getId());
        existingGenerator.setSiteCode(newGenerator.getSiteCode());//reset sitecode for existing generator
        existingGenerator.setOperatorEmail(newGenerator.getOperatorEmail());//reset operator email for existing generator
        existingGenerator.setName(newGenerator.getName());//reset name for for existing generator
        existingGenerator.setSite(siteRepo.findBySiteCode(newGenerator.getSiteCode()));//reset associated site for for existing generator
        existingGenerator.setCapacity(newGenerator.getCapacity());//reset generator capaciy for existing generator
        existingGenerator.setOperator(operatorRepo.findByEmail(newGenerator.getOperatorEmail()));//reset associated operator for existing generator
        existingGenerator.setSerialNumber(newGenerator.getSerialNumber());//reset serial number for existing generator
        var generator = genRepo.saveAndFlush(existingGenerator);//update the generator
        var recipients = operatorRepo.findByEmail(generator.getOperatorEmail()).getRecipient(); //get recipients associated to this operator       
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
                message.setTo(newGenerator.getOperatorEmail());//defaultly send message to the operator email insteat
            }
            message.setSubject("Your Company updated generator with the following details: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        } 
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
}
