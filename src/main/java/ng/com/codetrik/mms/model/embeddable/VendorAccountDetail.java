package ng.com.codetrik.mms.model.embeddable;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VendorAccountDetail implements Serializable {
    
    @Column(name="account_number")
    private String accountNumber;
    @Column(name="bank_name")
    private String bankName;

    public VendorAccountDetail(){}
    
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    
}
