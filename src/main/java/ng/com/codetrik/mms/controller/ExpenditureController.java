
package ng.com.codetrik.mms.controller;

import ng.com.codetrik.mms.service.ExpenditureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/expenditures", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class ExpenditureController {
   @Autowired 
   ExpenditureService expenditureService;
   

}
