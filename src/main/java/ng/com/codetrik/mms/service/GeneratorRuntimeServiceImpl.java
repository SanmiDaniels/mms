package ng.com.codetrik.mms.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.UUID;
import ng.com.codetrik.mms.model.entity.Expenditure;
import ng.com.codetrik.mms.model.entity.Generator;
import ng.com.codetrik.mms.model.entity.GeneratorRuntime;
import ng.com.codetrik.mms.model.entity.Operator;
import ng.com.codetrik.mms.model.enumeration.Months;
import ng.com.codetrik.mms.repository.GeneratorRepository;
import ng.com.codetrik.mms.repository.GeneratorRuntimeRepository;
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
public class GeneratorRuntimeServiceImpl implements GeneratorRuntimeService{
    @Autowired 
    GeneratorRuntimeRepository genRunRepo;
    
    @Autowired 
    SiteRepository siteRepo;
    
    @Autowired
    GeneratorRepository genRepo;
    
    @Autowired
    JavaMailSender emailSender;    
    
    private final Logger LOGGER = LoggerFactory.getLogger(GeneratorRuntimeServiceImpl.class);    
    
    @Override
    public GeneratorRuntime createRuntime(GeneratorRuntime newGeneratorRuntime) {
        var generator = genRepo.findBySerialNumber(newGeneratorRuntime.getGeneratorSerialNumber());
        newGeneratorRuntime.setGenerator(generator);
        //estimate the startInstant zoneddate and time from the provided transient variables
        newGeneratorRuntime.setStartTime(LocalDateTime.of(newGeneratorRuntime.getYear(), newGeneratorRuntime.getMonth(),newGeneratorRuntime.getStartDay(),
                newGeneratorRuntime.getStartHour(), newGeneratorRuntime.getStartMinute()).atZone(ZoneId.of("Africa/Lagos")));
        //estimate the stopInstant date and time from the provided transient variables
        newGeneratorRuntime.setStopTime(LocalDateTime.of(newGeneratorRuntime.getYear(), newGeneratorRuntime.getMonth(), newGeneratorRuntime.getStopDay(), 
                newGeneratorRuntime.getStopHour(), newGeneratorRuntime.getStopMinute()).atZone(ZoneId.of("Africa/Lagos")));
        //estimate the runtime 
        var startInstant = newGeneratorRuntime.getStartTime().toInstant();
        var stopInstant = newGeneratorRuntime.getStopTime().toInstant();
        var duration = Duration.between(startInstant, stopInstant);
        var durationHoursPart = duration.toHoursPart();
        var durationMinutesPart = duration.toMinutesPart();
        var durationSecondsPart = duration.toSecondsPart();
        var runtime = LocalTime.of(durationHoursPart,durationMinutesPart,durationSecondsPart);
        newGeneratorRuntime.setRuntime(runtime); 
        newGeneratorRuntime.setEnumMonth(Months.values()[newGeneratorRuntime.getMonth()-1]);//optaim enum value for month
        var generatorRuntime = genRunRepo.saveAndFlush(newGeneratorRuntime);//save to the DB
        var operator = generator.getOperator();//GET operator associated to the generator associated with the provided serial number
        sendGenRuntimeEmail(operator, generatorRuntime, generator, "Generator was runned with the folowing details: ");        
        return generatorRuntime;
    }
 
    @Override
    public GeneratorRuntime queryById(UUID id) {
       var generator = genRunRepo.findById(id).get();
       generator.setYear(generator.getStartTime().getYear());
       generator.setMonth(generator.getStartTime().getMonthValue());
       generator.setStartDay(generator.getStartTime().getDayOfMonth());
       generator.setStartHour(generator.getStartTime().getHour());
       generator.setStartMinute(generator.getStartTime().getMinute());
       generator.setStopDay(generator.getStopTime().getDayOfMonth());
       generator.setStopHour(generator.getStopTime().getHour());
       generator.setStopMinute(generator.getStopTime().getMinute()); 
       generator.setGeneratorSerialNumber(generator.getGenerator().getSerialNumber());
       generator.setSiteCode(generator.getGenerator().getSite().getSiteCode()); 
       return generator;
    }
   
   @Override
    public GeneratorRuntime updateRuntime(GeneratorRuntime newGeneratorRuntime){
        var existingGeneratorRuntime = genRunRepo.findById(newGeneratorRuntime.getId()).get();//get existing GeneratorRuntime to update
        var operator = existingGeneratorRuntime.getGenerator().getOperator();//GET operator associated to the generator associated with the provided serial number                 
        var generator = genRepo.findBySerialNumber(newGeneratorRuntime.getGeneratorSerialNumber());//get generator associated with the serialnumber of newGeneratorRuntime
        existingGeneratorRuntime.setGenerator(generator);//reset generator for existingGeneratorRuntime
        existingGeneratorRuntime.setDieselStartLevel(newGeneratorRuntime.getDieselStartLevel());//reset diesel start level for existingGeneratorRuntime
        existingGeneratorRuntime.setDieselStopLevel(newGeneratorRuntime.getDieselStopLevel());//reset diesel stop level for existingGeneratorRuntime
        existingGeneratorRuntime.setSiteCode(newGeneratorRuntime.getSiteCode());//reset site code for existingGeneratorRuntime
        existingGeneratorRuntime.setGeneratorSerialNumber(newGeneratorRuntime.getGeneratorSerialNumber()); //reset generator serial number for existingGeneratorRuntime
        existingGeneratorRuntime.setStartDay(newGeneratorRuntime.getStartDay());//reset start day for existingGeneratorRuntime
        existingGeneratorRuntime.setMonth(newGeneratorRuntime.getMonth());//reset month for existingGeneratorRuntime
        existingGeneratorRuntime.setYear(newGeneratorRuntime.getYear());
        //reset start time of existing GeneratorRuntime
        existingGeneratorRuntime.setStartTime(LocalDateTime.of(newGeneratorRuntime.getYear(), newGeneratorRuntime.getMonth(),
                newGeneratorRuntime.getStartDay(),newGeneratorRuntime.getStartHour(), 
                newGeneratorRuntime.getStartMinute()).atZone(ZoneId.of("Africa/Lagos")));
         //reset stop time of existing GeneratorRuntime
        existingGeneratorRuntime.setStopTime(LocalDateTime.of(newGeneratorRuntime.getYear(), newGeneratorRuntime.getMonth(),
                newGeneratorRuntime.getStopDay(), newGeneratorRuntime.getStopHour(), 
                newGeneratorRuntime.getStopMinute()).atZone(ZoneId.of("Africa/Lagos")));
        //estimate the runtime 
        var startInstant = existingGeneratorRuntime.getStartTime().toInstant();
        var stopInstant = existingGeneratorRuntime.getStopTime().toInstant();
        var duration = Duration.between(startInstant, stopInstant);
        var durationHoursPart = duration.toHoursPart();
        var durationMinutesPart = duration.toMinutesPart();
        var durationSecondsPart = duration.toSecondsPart();
        var runtime = LocalTime.of(durationHoursPart,durationMinutesPart,durationSecondsPart);
        existingGeneratorRuntime.setRuntime(runtime); //reset runtime of existing generator runtime      
        existingGeneratorRuntime.setEnumMonth(Months.values()[newGeneratorRuntime.getMonth()-1]);//reset Month of the existing generator runtime
        var generatorRuntime = genRunRepo.saveAndFlush(existingGeneratorRuntime);//save to the DB
        sendGenRuntimeEmail(operator, generatorRuntime, generator, "Generator runtime was edited to folowing details: ");                
        return generatorRuntime;
    } 
    
    private void sendGenRuntimeEmail(Operator operator, GeneratorRuntime generatorRuntime, Generator generator, String subject){
        var recipients = operator.getRecipient(); //get list of recipients associated to the Operator          
        try{
            var message = new SimpleMailMessage();//create simple message instance
            var template = generatorRuntime.toString() + ", \nGenerator Name = " +  generator.getName() + ", \nGenerator Serial = " +  
            generator.getSerialNumber() + ", \nSite = " +  generator.getSite().getName();//build template message from toString
            if(recipients!=null){ //check if the list of recipient is null to avaoid null pointer exception
                var recp = new String[recipients.size()];//create empty array of recipients
                recipients.forEach((r) -> {recp[recipients.indexOf(r)] = r.getEmail();});
                message.setTo(recp);
            }else{
                message.setTo(operator.getEmail());//default send message to the email associated to the operator
            }  
            message.setSubject(subject); 
            message.setText(template);
            emailSender.send(message);
            }catch(MailException e){
                LOGGER.error(Marker.ANY_MARKER, e.getMessage());
            }           
    }                
}
