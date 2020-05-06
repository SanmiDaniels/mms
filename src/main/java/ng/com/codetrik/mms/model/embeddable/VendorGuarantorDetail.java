package ng.com.codetrik.mms.model.embeddable;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendorGuarantorDetail implements Serializable {
    @Column(name="guarantor_name")
    private String guarantorName;
    
    @Column(name="guarantor_address")
    private String guarantorAddress;
    
    @Column(name="guarantor_phone_number")
    private String guarantorPhoneNumber;
}
