package ng.com.codetrik.mms.model.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GeneratorDTO extends RepresentationModel<GeneratorDTO>{
    private UUID id;
    private String name;
    private String capacity;//generator capacity 
    private String serialNumber;
    private String operatorEmail;
    private String siteCode;
}
