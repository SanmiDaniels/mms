package ng.com.codetrik.mms.service;

import java.util.UUID;
import ng.com.codetrik.mms.model.Revenue;
import ng.com.codetrik.mms.model.Vendor;
import ng.com.codetrik.mms.repository.OperatorRepository;
import ng.com.codetrik.mms.repository.RevenueRepository;
import ng.com.codetrik.mms.repository.SiteRepository;
import ng.com.codetrik.mms.repository.VendorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VendorServiceImpl implements VendorService{

    @Autowired
    VendorRepository vendorRepo;
    
    @Autowired
    RevenueRepository revenueRepo;
    
    @Autowired
    OperatorRepository operatorRepo;
    
    @Autowired
    SiteRepository siteRepo;
    
    @Autowired
    JavaMailSender emailSender;
    
    private final Logger LOGGER = LoggerFactory.getLogger(VendorServiceImpl.class);
    
    @Override
    public Vendor createVendor(Vendor newVendor) {
        var operator = operatorRepo.findByEmail(newVendor.getOperatorEmail());//get operator to be associated with this vendor
        var site = siteRepo.findBySiteCode(newVendor.getSiteCode());//get site to be associated with this operator
        newVendor.setOperator(operator);
        newVendor.setSite(site);
        var vendor = vendorRepo.save(newVendor);
        var recipients = operator.getRecipient();
        try{
            var message = new SimpleMailMessage();
            var template = vendor.toString();
            if(recipients!=null){
                var recp = new String[recipients.size()];
                recipients.forEach((r)->{
                   recp[recipients.indexOf(r)] = r.getEmail();
                });
                message.setTo(recp);
            }else{
                message.setTo(newVendor.getOperatorEmail());
            }
            message.setSubject("Your company registered a new vendor with the following details: ");
            message.setText(template);
            emailSender.send(message);
        }catch(MailException e){
            LOGGER.info(Marker.ANY_MARKER, e.getMessage());
        }
        revenueRepo.save(new Revenue(0.00, 0.00, 0.00, 0.00, 0.00, vendor, newVendor.getEmail(), 0.00));//create reference revenue record for new vendor
        return vendor;
    }

    @Override
    public Vendor updateVendor(Vendor newVendor){
        var existingVendor = vendorRepo.findByEmail(newVendor.getEmail());//get vendor to update 
        var operator = operatorRepo.findByEmail(newVendor.getOperatorEmail());//get operator to be reassociated to the existing vendor
        existingVendor.setOperatorEmail(newVendor.getOperatorEmail());//reset operatoremail of existing vendor 
        existingVendor.setSiteCode(newVendor.getSiteCode());//reset sitecode of of existing vendor 
        existingVendor.setOperator(operator);//reset associated operator of of existing vendor 
        existingVendor.setSite(siteRepo.findBySiteCode(newVendor.getSiteCode()));//reset associated site of of existing vendor 
        existingVendor.setName(newVendor.getName());//reset name of of existing vendor 
        existingVendor.setAddress(newVendor.getAddress());//reset address of of existing vendor 
        existingVendor.setBussinessName(newVendor.getBussinessName());//reset bussiness name of of existing vendor 
        existingVendor.setRegistrationNumber(newVendor.getRegistrationNumber());//reset registration number of of existing vendor 
        existingVendor.setPhoneNumber(newVendor.getPhoneNumber());//reset phone number of of existing vendor 
        existingVendor.setVendorGuarantorDetail(newVendor.getVendorGuarantorDetail());//reset vendor guarantor details 
        existingVendor.setAccountDetail(newVendor.getAccountDetail());//reset vendor account details
        var vendor = vendorRepo.saveAndFlush(existingVendor);//update the changes to the existing vendor 
        var recipients = operator.getRecipient();
                try{
                    var message = new SimpleMailMessage();
                    var template = vendor.toString();
                    if(recipients!=null){
                        var recp = new String[recipients.size()];
                        recipients.forEach((r)->{
                           recp[recipients.indexOf(r)] = r.getEmail();
                        });
                        message.setTo(recp);
                    }else{
                        message.setTo(newVendor.getOperatorEmail());
                    }
                    message.setSubject("Your company updated a vendor with the following details: ");
                    message.setText(template);
                    emailSender.send(message);
                }catch(MailException e){
                    LOGGER.info(Marker.ANY_MARKER, e.getMessage());
                }        
        return vendor;
    }

    @Override
    public Vendor queryById(UUID id) {
        var vendor = vendorRepo.findById(id).get();
        vendor.setOperatorEmail(vendor.getOperator().getEmail());
        vendor.setSiteCode(vendor.getSite().getSiteCode());
        return vendor;
    }

    @Override
    public Vendor queryByEmail(String email) {
        var vendor = vendorRepo.findByEmail(email);
        vendor.setOperatorEmail(vendor.getOperator().getEmail());
        vendor.setSiteCode(vendor.getSite().getSiteCode());
        return vendor;
    }
    
}
