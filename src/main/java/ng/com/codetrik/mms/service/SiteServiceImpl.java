package ng.com.codetrik.mms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import ng.com.codetrik.mms.model.entity.Operator;
import ng.com.codetrik.mms.model.entity.Site;
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
public class SiteServiceImpl implements SiteService{
    
@Autowired
SiteRepository siteRepo;

@Autowired
OperatorRepository operatorRepo;

@Autowired
JavaMailSender emailSender;

 private final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    @Override
    public Site createSite(Site newSite) {
        var operator = operatorRepo.findByEmail(newSite.getOperatorEmail());//first obtain the operator to be mapped to this newSite
        newSite.setOperator(operator);//set operator associated to this newSite
        var site =  siteRepo.saveAndFlush(newSite);
        sendSiteEmail(operator, site, "Your Company added a new site with the following details: ");          
        return site;
        
    }

    //this have to be called first by front end before update can be performed
    @Override
    public Site queryBySiteCode(String siteCode) {
        var s = siteRepo.findBySiteCode(siteCode);//site get queried from the database
        s.setOperatorEmail(s.getOperator().getEmail());//this made the operator email available during update for email recipients findings
        return s;//site get returned back to front end
    }

    
    @Override
    public Site queryById(UUID id) {
        var s =  siteRepo.findById(id).get();//site get queried from the database
        s.setOperatorEmail(s.getOperator().getEmail());//this made the operator email available during update for email recipients findings
        return s;//site get returned back to front end
    }

    @Override
    public Site updateSite(Site newSite) {
        var existingSite = siteRepo.findBySiteCode(newSite.getSiteCode());
        var operator = operatorRepo.findByEmail(newSite.getOperatorEmail());
        existingSite.setOperatorEmail(newSite.getOperatorEmail());
        existingSite.setName(newSite.getName());
        existingSite.setCapacity(newSite.getCapacity());
        existingSite.setProvince(newSite.getProvince());
        existingSite.setLga(newSite.getLga());
        existingSite.setSettlement(newSite.getSettlement());
        existingSite.setNumberOfPV(newSite.getNumberOfPV());
        existingSite.setPeakWattPerPV(newSite.getPeakWattPerPV());
        existingSite.setNumberOfBatteryInverter(newSite.getNumberOfBatteryInverter());
        existingSite.setNumberOfPVInverter(newSite.getNumberOfPVInverter());
        existingSite.setNumberOfPhasePerPVInverter(newSite.getNumberOfPhasePerPVInverter());
        existingSite.setNumberOfPhasePerBatteryInverter(newSite.getNumberOfPhasePerBatteryInverter());
        existingSite.setTotalBankPower(newSite.getTotalBankPower());
        existingSite.setPerClusterBankPower(newSite.getPerClusterBankPower());
        existingSite.setNumberOfCluster(newSite.getNumberOfCluster());
        existingSite.setBatteryInverterBrand(newSite.getBatteryInverterBrand());
        existingSite.setBatteryInverterModel(newSite.getBatteryInverterModel());
        existingSite.setPVInverterBrand(newSite.getPVInverterBrand());
        existingSite.setPvInverterModel(newSite.getPvInverterModel());
        existingSite.setCurrentSiteManager(newSite.getCurrentSiteManager());
        existingSite.setSiteCode(newSite.getSiteCode());
        existingSite.setOperator(operator);
        var site = siteRepo.saveAndFlush(existingSite);
        sendSiteEmail(operator, site, "Your Company updated a site with the following details: "); 
        return site;
    }

    @Override
    public List<String> allThisOperatorSiteCode(String operatorEmail) {
        var sites = operatorRepo.findByEmail(operatorEmail).getSite();
        List<String> siteCodeList = new ArrayList<>();
        sites.forEach((s)-> {siteCodeList.add(s.getSiteCode());});
        return siteCodeList;
    }

    @Override
    public List<String> allThisOperatorSite(String operatorEmail) {
        var sites = operatorRepo.findByEmail(operatorEmail).getSite();
        List<String> siteNameList = new ArrayList<>();
        sites.forEach((s)->{siteNameList.add(s.getName());});
        return siteNameList;
    }

    @Override
    public Map<String, String> allThisOperatorSiteCodeAndSite(String operatorEmail) {
        Map<String, String> siteCodeWithSite = new HashMap<>();
        var sites = operatorRepo.findByEmail(operatorEmail).getSite();
        sites.forEach((s)-> {
            siteCodeWithSite.put(s.getSiteCode(), s.getName());
        });
        return siteCodeWithSite;
    }
    
    private void sendSiteEmail(Operator operator, Site site, String subject){
       var recipients = operator.getRecipient(); //get list of recipients associated to the Operator        
        try{
            var message = new SimpleMailMessage();//create simple message instance
            var template = site.toString();//build template message from toString
            if(recipients!=null){ //check if the list of recipient is null to avaoid null pointer exception
                var recp = new String[recipients.size()];//create empty array of recipients
                recipients.forEach((r) -> { recp[recipients.indexOf(r)] = r.getEmail();});
                message.setTo(recp);
            }else{
                message.setTo(site.getOperatorEmail());//default send message to the email associated to the operator
            }  
            message.setSubject(subject); 
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.error(Marker.ANY_MARKER, e.getMessage());
        }        
    }
}
