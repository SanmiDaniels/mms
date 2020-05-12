
package ng.com.codetrik.mms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import ng.com.codetrik.mms.model.util.Detail;
import ng.com.codetrik.mms.model.dto.SiteDTO;
import ng.com.codetrik.mms.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/sites", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class SiteController {
    @Autowired
    SiteService siteService;
    
    //entry point in to site Resources
    @GetMapping()
    public EntityModel<SiteDTO> getSiteByCode(@RequestBody Detail detail){
        List<Link> links = new ArrayList<>();
        var site = siteService.queryBySiteCode(detail.getSiteCode());
        var model = new SiteDTO(site.getId(), site.getName(), site.getSiteCode(), site.getOperatorEmail(), site.getCapacity(), site.getProvince(), 
                site.getLga(), site.getSettlement(), site.getNumberOfPV(), site.getPeakWattPerPV(),site.getNumberOfBatteryInverter(), 
                site.getNumberOfPVInverter(), site.getNumberOfPhasePerPVInverter(), site.getNumberOfPhasePerBatteryInverter(), 
                site.getTotalBankPower(), site.getPerClusterBankPower(), site.getNumberOfCluster(), site.getBatteryInverterBrand(), 
                site.getBatteryInverterModel(), site.getPvInverterModel(), site.getPVInverterBrand(), site.getCurrentSiteManager());
        
        var linkToVendorsRelatedToSite = site.getVendor().stream().map( 
                vendor -> linkTo(methodOn(VendorController.class).getVendorById(vendor.getId())).withRel("vendor")
        ).collect(Collectors.toList());    
        
        links.add(linkTo(methodOn(OperatorController.class).getOperatorById(site.getOperator().getId())).withRel("operator"));
        links.addAll(linkToVendorsRelatedToSite);
        links.add(linkTo(methodOn(SiteController.class).getSiteById(site.getId())).withSelfRel());
        return new EntityModel<>(model,links);
    }

    @GetMapping(path = "/{id}")
    public EntityModel<SiteDTO> getSiteById(@PathVariable(value="id") UUID id){
        List<Link> links = new ArrayList<>();
        var site = siteService.queryById(id);
        var model = new SiteDTO(site.getId(), site.getName(), site.getSiteCode(), site.getOperatorEmail(), site.getCapacity(), site.getProvince(), 
                site.getLga(), site.getSettlement(), site.getNumberOfPV(), site.getPeakWattPerPV(),site.getNumberOfBatteryInverter(), 
                site.getNumberOfPVInverter(), site.getNumberOfPhasePerPVInverter(), site.getNumberOfPhasePerBatteryInverter(), 
                site.getTotalBankPower(), site.getPerClusterBankPower(), site.getNumberOfCluster(), site.getBatteryInverterBrand(), 
                site.getBatteryInverterModel(), site.getPvInverterModel(), site.getPVInverterBrand(), site.getCurrentSiteManager());
        
        var linkToVendorsRelatedToSite = site.getVendor().stream().map( 
                vendor -> linkTo(methodOn(VendorController.class).getVendorById(vendor.getId())).withRel("vendor")
        ).collect(Collectors.toList());
        
        links.add(linkTo(methodOn(OperatorController.class).getOperatorById(site.getOperator().getId())).withRel("operator"));
        links.addAll(linkToVendorsRelatedToSite);
        links.add(linkTo(methodOn(SiteController.class).getSiteById(site.getId())).withSelfRel());
        return new EntityModel<>(model,links);
    }    
}
