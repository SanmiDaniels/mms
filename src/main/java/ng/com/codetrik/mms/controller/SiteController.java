
package ng.com.codetrik.mms.controller;

import java.util.UUID;
import ng.com.codetrik.mms.model.util.Detail;
import ng.com.codetrik.mms.model.util.SiteModel;
import ng.com.codetrik.mms.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
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
    public EntityModel<SiteModel>getSiteByCode(@RequestBody Detail detail){
        var site = siteService.queryBySiteCode(detail.getSiteCode());
        var model = new SiteModel(site.getId(), site.getName(), site.getSiteCode(), site.getOperatorEmail(), site.getCapacity(), site.getProvince(), 
                site.getLga(), site.getSettlement(), site.getNumberOfPV(), site.getPeakWattPerPV(),site.getNumberOfBatteryInverter(), 
                site.getNumberOfPVInverter(), site.getNumberOfPhasePerPVInverter(), site.getNumberOfPhasePerBatteryInverter(), 
                site.getTotalBankPower(), site.getPerClusterBankPower(), site.getNumberOfCluster(), site.getBatteryInverterBrand(), 
                site.getBatteryInverterModel(), site.getPvInverterModel(), site.getPVInverterBrand(), site.getCurrentSiteManager());
        return new EntityModel<>(
                model,
                linkTo(methodOn(SiteController.class).getSiteById(site.getId())).withSelfRel(),
                linkTo(methodOn(OperatorController.class).getOperatorById(site.getOperator().getId())).withRel("operator")
                );
    }

    @GetMapping(path = "/{id}")
    public EntityModel<SiteModel>getSiteById(@PathVariable(value="id") UUID id){
        var site = siteService.queryById(id);
        var siteModel = new SiteModel(site.getId(), site.getName(), site.getSiteCode(), site.getOperatorEmail(), site.getCapacity(), site.getProvince(), 
                site.getLga(), site.getSettlement(), site.getNumberOfPV(), site.getPeakWattPerPV(),site.getNumberOfBatteryInverter(), 
                site.getNumberOfPVInverter(), site.getNumberOfPhasePerPVInverter(), site.getNumberOfPhasePerBatteryInverter(), 
                site.getTotalBankPower(), site.getPerClusterBankPower(), site.getNumberOfCluster(), site.getBatteryInverterBrand(), 
                site.getBatteryInverterModel(), site.getPvInverterModel(), site.getPVInverterBrand(), site.getCurrentSiteManager());
        return new EntityModel<>(
                siteModel,
                linkTo(methodOn(OperatorController.class).getOperatorById(site.getOperator().getId())).withRel("operator")
                );
    }    
}
