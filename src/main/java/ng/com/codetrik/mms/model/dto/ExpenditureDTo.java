package ng.com.codetrik.mms.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ng.com.codetrik.mms.model.enumeration.ExpenditureStatus;
import ng.com.codetrik.mms.model.enumeration.ExpenditureType;
import ng.com.codetrik.mms.model.enumeration.Months;
import org.springframework.hateoas.RepresentationModel;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ExpenditureDTo extends RepresentationModel<ExpenditureDTo>{
    private UUID id;
    private Double ammount;
    private ExpenditureType enumType;
    private ExpenditureStatus enumStatus;
    private Months enumMonth;
    private String name;
    private int year;
    private LocalDateTime dateProcessed;
    private LocalDateTime dateFiled;
    private String description;
    private String requestorEmail;
    private String operatorEmail;
    private String siteCode;
    
}
