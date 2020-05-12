
package ng.com.codetrik.mms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import ng.com.codetrik.mms.model.entity.Vendor;
import ng.com.codetrik.mms.model.util.Detail;
import ng.com.codetrik.mms.model.dto.VendorDTO;
import ng.com.codetrik.mms.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/vendors", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class VendorController {
    
    @Autowired
    VendorService vendorService;
        
    //entry point in to the vendor resource
    @GetMapping()
    public VendorDTO getVendorByEmail(@RequestBody Detail detail){
        var vendor = vendorService.queryByEmail(detail.getVendorEmail());
        var model = new VendorDTO(vendor.getId(), vendor.getName(), vendor.getAddress(), vendor.getBussinessName(), vendor.getRegistrationNumber(), 
                vendor.getPhoneNumber(), vendor.isCredibility(), vendor.getEmail(), vendor.getSiteCode(), vendor.getOperatorEmail(), 
                vendor.getAccountDetail(), vendor.getVendorGuarantorDetail());
        
        model.add(linkTo(methodOn(OperatorController.class).getOperatorById(vendor.getOperator().getId())).withRel("operator"));//proxy approach
        model.add(linkTo(methodOn(SiteController.class).getSiteById(vendor.getSite().getId())).withRel("site"));
        model.add(linkTo(VendorController.class).slash(vendor.getId()).withSelfRel());//slash aproach 
        return model;
    }
    
    @GetMapping(path="/{id}")
    public  VendorDTO getVendorById(@PathVariable(value="id") UUID id){
        var vendor = vendorService.queryById(id);
        var model = new VendorDTO(vendor.getId(), vendor.getName(), vendor.getAddress(), vendor.getBussinessName(), vendor.getRegistrationNumber(), 
                vendor.getPhoneNumber(), vendor.isCredibility(), vendor.getEmail(), vendor.getSiteCode(), vendor.getOperatorEmail(), 
                vendor.getAccountDetail(), vendor.getVendorGuarantorDetail());
        
        model.add(linkTo(methodOn(OperatorController.class).getOperatorById(vendor.getOperator().getId())).withRel("operator"));
        model.add(linkTo(methodOn(SiteController.class).getSiteById(vendor.getSite().getId())).withRel("site")); 
        model.add(linkTo(VendorController.class).slash(vendor.getId()).withSelfRel());
        return model;
    }    

}
