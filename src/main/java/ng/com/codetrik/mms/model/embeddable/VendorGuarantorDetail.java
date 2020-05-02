package ng.com.codetrik.mms.model.embeddable;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VendorGuarantorDetail implements Serializable {
    @Column(name="guarantor_name")
    private String guarantorName;
    
    @Column(name="guarantor_address")
    private String guarantorAddress;
    
    @Column(name="guarantor_phone_number")
    private String guarantorPhoneNumber;
    
    public VendorGuarantorDetail(){}

    public String getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(String guarantorName) {
        this.guarantorName = guarantorName;
    }

    public String getGuarantorAddress() {
        return guarantorAddress;
    }

    public void setGuarantorAddress(String guarantorAddress) {
        this.guarantorAddress = guarantorAddress;
    }

    public String getGuarantorPhoneNumber() {
        return guarantorPhoneNumber;
    }

    public void setGuarantorPhoneNumber(String guarantorPhoneNumber) {
        this.guarantorPhoneNumber = guarantorPhoneNumber;
    }
}
