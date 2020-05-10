
package ng.com.codetrik.mms.controller;

import ng.com.codetrik.mms.model.util.SiteModel;
import ng.com.codetrik.mms.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/sites", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class SiteController {
    @Autowired
    SiteService siteService;
    
    @GetMapping(path = "/{siteCode}")
    public EntityModel<SiteModel>getSiteByCode(@PathVariable(value="siteCode") String code){
        var site = siteService.queryBySiteCode(code);
        var model = new SiteModel(site.getId(), site.getName(), site.getSiteCode(), site.getOperatorEmail(), site.getCapacity(), site.getProvince(), 
                site.getLga(), site.getSettlement(), site.getNumberOfPV(), site.getPeakWattPerPV(),site.getNumberOfBatteryInverter(), 
                site.getNumberOfPVInverter(), site.getNumberOfPhasePerPVInverter(), site.getNumberOfPhasePerBatteryInverter(), 
                site.getTotalBankPower(), site.getPerClusterBankPower(), site.getNumberOfCluster(), site.getBatteryInverterBrand(), 
                site.getBatteryInverterModel(), site.getPvInverterModel(), site.getPVInverterBrand(), site.getCurrentSiteManager());
        return new EntityModel<>(
                model,
                linkTo(methodOn(SiteController.class).getSiteByCode(code)).withSelfRel(),
                linkTo(methodOn(OperatorController.class).getOperatorById(site.getOperator().getId())).withRel("operator")
                );
    }
    
}
