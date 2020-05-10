package ng.com.codetrik.mms.controller;

import java.util.UUID;
import ng.com.codetrik.mms.model.util.Login;
import ng.com.codetrik.mms.model.util.OperatorModel;
import ng.com.codetrik.mms.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
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
    
    @GetMapping()
    public EntityModel<OperatorModel> getOperatorByEmail(@RequestBody Login login){
        var operator = operatorService.queryByEmail(login.getEmail());
        var  model = new OperatorModel(operator.getId(), operator.getName(), operator.getEmail(), operator.getSiteCount(), operator.getAddress());
        return new EntityModel<>(
                model,
                linkTo(methodOn(OperatorController.class).getOperatorById(operator.getId())).withRel("operator")
        );
    }
    
    @GetMapping(path = "/{id}")
    public EntityModel<OperatorModel> getOperatorById(@PathVariable(value = "id") UUID id){
        var operator = operatorService.queryById(id);
        var  model = new OperatorModel(operator.getId(), operator.getName(), operator.getEmail(), operator.getSiteCount(), operator.getAddress());
        return new EntityModel<>(
                model,
                linkTo(methodOn(OperatorController.class).getOperatorById(id)).withSelfRel()
        );
    }    
}
