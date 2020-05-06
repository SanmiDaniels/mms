package ng.com.codetrik.mms.model.embeddable;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class VendorAccountDetail implements Serializable {
    
    @Column(name="account_number")
    private String accountNumber;
    @Column(name="bank_name")
    private String bankName;
    
}
