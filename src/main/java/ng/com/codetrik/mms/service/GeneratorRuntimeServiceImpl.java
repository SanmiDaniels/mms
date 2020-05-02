package ng.com.codetrik.mms.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import ng.com.codetrik.mms.model.Generator;
import ng.com.codetrik.mms.model.GeneratorRuntime;
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
    public GeneratorRuntime createRuntime(GeneratorRuntime genRun) {
        var gen = genRepo.findBySerialNumber(genRun.getGeneratorSerialNumber());
        genRun.setGenerator(gen);
        //estimate the start zoneddate and time from the provided transient variables
        genRun.setStartTime(LocalDateTime.of(genRun.getYear(), genRun.getMonth(),genRun.getStartDay(),genRun.getStartHour(), genRun.getStartMinute()).atZone(ZoneId.of("Africa/Lagos")));
        //estimate the stop date and time from the provided transient variables
        genRun.setStopTime(LocalDateTime.of(genRun.getYear(), genRun.getMonth(), genRun.getStopDay(), genRun.getStopHour(), genRun.getStopMinute()).atZone(ZoneId.of("Africa/Lagos")));
        //estimate the runtime 
        var start = genRun.getStartTime().toInstant();
        var stop = genRun.getStopTime().toInstant();
        var duration = Duration.between(start, stop);
        var durationHoursPart = duration.toHoursPart();
        var durationMinutesPart = duration.toMinutesPart();
        var durationSecondsPart = duration.toSecondsPart();
        var runtime = LocalTime.of(durationHoursPart,durationMinutesPart,durationSecondsPart);
        genRun.setRuntime(runtime); 
        
        //optaim enum value for month
        genRun.setEnumMonth(Months.values()[genRun.getMonth()-1]);
        
        //save to the DB
        var genRunReturned = genRunRepo.saveAndFlush(genRun);
        
        //GET operator associated to the generator associated with the provided serial number
        var opp = gen.getOperator();
        
        var recipients = opp.getRecipient(); //get list of recipients associated to the Operator          
        try{
            var message = new SimpleMailMessage();//create simple message instance
            var template = genRunReturned.toString() + ", \nGenerator Name = " +  gen.getName() + ", \nGenerator Serial = " +  gen.getSerialNumber() + 
                    ", \nSite = " +  gen.getSite().getName();//build template message from toString
            
            if(!recipients.isEmpty()&& recipients!=null){ //check if the list of recipient is null to avaoid null pointer exception
                var recp = new String[recipients.size()];//create empty array of recipients
                recipients.forEach((r) -> {
                    recp[recipients.indexOf(r)] = r.getEmail();
                });
                message.setTo(recp);
            }else{
                message.setTo(opp.getEmail());//default send message to the email associated to the operator
            }  
            message.setSubject("Generator was runned with the folowing details: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }        
        return genRunReturned;
    }

    @Override
    public List<Generator> generatorsOnSite(String siteCode) {
        return siteRepo.findBySiteCode(siteCode).getGenerator();
    }

    @Override
    public GeneratorRuntime queryById(UUID id) {
       var gr = genRunRepo.findById(id).get();
       gr.setYear(gr.getStartTime().getYear());
       gr.setMonth(gr.getStartTime().getMonthValue());
       
       gr.setStartDay(gr.getStartTime().getDayOfMonth());
       gr.setStartHour(gr.getStartTime().getHour());
       gr.setStartMinute(gr.getStartTime().getMinute());
       
       gr.setStopDay(gr.getStopTime().getDayOfMonth());
       gr.setStopHour(gr.getStopTime().getHour());
       gr.setStopMinute(gr.getStopTime().getMinute()); 
       gr.setGeneratorSerialNumber(gr.getGenerator().getSerialNumber());
       gr.setSiteCode(gr.getGenerator().getSite().getSiteCode());
       
       return gr;
    }

    @Override
    public GeneratorRuntime updateRuntime(GeneratorRuntime genRun) {
        var gen = genRepo.findBySerialNumber(genRun.getGeneratorSerialNumber());
        genRun.setGenerator(gen);
        //estimate the start zoneddate and time from the provided transient variables
        genRun.setStartTime(LocalDateTime.of(genRun.getYear(), genRun.getMonth(),genRun.getStartDay(),genRun.getStartHour(), genRun.getStartMinute()).atZone(ZoneId.of("Africa/Lagos")));
        //estimate the stop date and time from the provided transient variables
        genRun.setStopTime(LocalDateTime.of(genRun.getYear(), genRun.getMonth(), genRun.getStopDay(), genRun.getStopHour(), genRun.getStopMinute()).atZone(ZoneId.of("Africa/Lagos")));
        //estimate the runtime 
        var start = genRun.getStartTime().toInstant();
        var stop = genRun.getStopTime().toInstant();
        var duration = Duration.between(start, stop);
        var durationHoursPart = duration.toHoursPart();
        var durationMinutesPart = duration.toMinutesPart();
        var durationSecondsPart = duration.toSecondsPart();
        var runtime = LocalTime.of(durationHoursPart,durationMinutesPart,durationSecondsPart);
        genRun.setRuntime(runtime); 
        
        //obtaim enum value for month
        genRun.setEnumMonth(Months.values()[genRun.getMonth()-1]);
        
        //save to the DB
        var genRunReturned = genRunRepo.saveAndFlush(genRun);
        
        //GET operator associated to the generator associated with the provided serial number
        var opp = gen.getOperator();
        
        var recipients = opp.getRecipient(); //get list of recipients associated to the Operator          
        try{
            var message = new SimpleMailMessage();//create simple message instance
            var template = genRunReturned.toString() + ", \nGenerator Name = " +  gen.getName() + ", \nGenerator Serial = " +  gen.getSerialNumber() + 
                    ", \nSite = " +  gen.getSite().getName();//build template message from toString
            
            if(!recipients.isEmpty()&& recipients!=null){ //check if the list of recipient is null to avaoid null pointer exception
                var recp = new String[recipients.size()];//create empty array of recipients
                recipients.forEach((r) -> {
                    recp[recipients.indexOf(r)] = r.getEmail();
                });
                message.setTo(recp);
            }else{
                message.setTo(opp.getEmail());//default send message to the email associated to the operator
            }  
            message.setSubject("Generator runtime was edited to folowing details: "); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }        
        return genRunReturned;

    }
    
}
