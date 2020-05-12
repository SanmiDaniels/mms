package ng.com.codetrik.mms.controller;

import java.util.UUID;
import ng.com.codetrik.mms.model.dto.GeneratorDTO;
import ng.com.codetrik.mms.model.util.Detail;
import ng.com.codetrik.mms.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/generators", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class GeneratorController {
    @Autowired
    GeneratorService generatorService;
    
    @GetMapping()
    public GeneratorDTO getGeneratorBySerial(@RequestBody Detail detail){
        var generator = generatorService.queryBySerialNumber(detail.getSerialNumber());
        var model = new GeneratorDTO(generator.getId(), generator.getName(), generator.getCapacity(), generator.getSerialNumber(), generator.getOperatorEmail(), generator.getSiteCode());
        
        model.add(linkTo(methodOn(SiteController.class).getSiteById(generator.getSite().getId())).withRel("site"));
        model.add(linkTo(methodOn(OperatorController.class).getOperatorById(generator.getOperator().getId())).withRel("operator"));
        model.add(linkTo(methodOn(GeneratorController.class).getGeneratorById(generator.getId())).withSelfRel());
        return model;
    }
    
    @GetMapping(path="/{id}")
    public GeneratorDTO getGeneratorById(@PathVariable(value="id") UUID id){
        var generator = generatorService.queryById(id);
        var model = new GeneratorDTO(generator.getId(), generator.getName(), generator.getCapacity(), generator.getSerialNumber(), generator.getOperatorEmail(), generator.getSiteCode());
        
        model.add(linkTo(methodOn(SiteController.class).getSiteById(generator.getSite().getId())).withRel("site")); 
        model.add(linkTo(methodOn(OperatorController.class).getOperatorById(generator.getOperator().getId())).withRel("operator"));
        model.add(linkTo(methodOn(GeneratorController.class).getGeneratorById(generator.getId())).withSelfRel());
        return model;        
    }
}
