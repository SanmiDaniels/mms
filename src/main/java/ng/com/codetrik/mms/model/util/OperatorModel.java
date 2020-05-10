package ng.com.codetrik.mms.model.util;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OperatorModel {
    
    private UUID id;
    private String name;
    private String email;
    private int siteCount;
    private String address;
}
