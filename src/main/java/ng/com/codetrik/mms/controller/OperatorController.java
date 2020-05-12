package ng.com.codetrik.mms.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import ng.com.codetrik.mms.model.entity.Operator;
import ng.com.codetrik.mms.model.util.Login;
import ng.com.codetrik.mms.model.dto.OperatorDTO;
import ng.com.codetrik.mms.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/operators", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class OperatorController {
    @Autowired
    OperatorService operatorService;
    
    //entry point in to Operator resources
    @GetMapping()
    public EntityModel<OperatorDTO> getOperatorByEmail(@RequestBody Login login){
        var operator = operatorService.queryByEmail(login.getEmail());
        var  model = new OperatorDTO(operator.getId(), operator.getName(), operator.getEmail(), operator.getSiteCount(), operator.getAddress());
        
        var links = operator.getSite().stream().map(
                site -> linkTo(methodOn(SiteController.class).getSiteById(site.getId())).withRel("site")
        ).collect(Collectors.toList());
        
        links.addAll(operator.getGenerator().stream().map(
                generator -> linkTo(methodOn(GeneratorController.class).getGeneratorById(generator.getId())).withRel("generator")
        ).collect(Collectors.toList()));  
        
        links.addAll(operator.getVendor().stream().map(
                vendor -> linkTo(methodOn(VendorController.class).getVendorById(vendor.getId())).withRel("vendor")
        ).collect(Collectors.toList())); 
        
        links.add(linkTo(methodOn(OperatorController.class).getOperatorById(operator.getId())).withSelfRel());
        return new EntityModel<>(model,links);
    }
    
    @GetMapping(path = "/{id}")
    public EntityModel<OperatorDTO> getOperatorById(@PathVariable(value = "id") UUID id){
        var operator = operatorService.queryById(id);
        var  model = new OperatorDTO(operator.getId(), operator.getName(), operator.getEmail(), operator.getSiteCount(), operator.getAddress());
        
        var links = operator.getSite().stream().map(
                site -> linkTo(methodOn(SiteController.class).getSiteById(site.getId())).withRel("site")
        ).collect(Collectors.toList());
        
        links.addAll(operator.getGenerator().stream().map(
                generator -> linkTo(methodOn(GeneratorController.class).getGeneratorById(generator.getId())).withRel("generator")
        ).collect(Collectors.toList()));
        
        links.addAll(operator.getVendor().stream().map(
                vendor -> linkTo(methodOn(VendorController.class).getVendorById(vendor.getId())).withRel("vendor")
        ).collect(Collectors.toList()));       
        
        links.add(linkTo(methodOn(OperatorController.class).getOperatorById(operator.getId())).withSelfRel());
        return new EntityModel<>(model,links);
    } 
}
