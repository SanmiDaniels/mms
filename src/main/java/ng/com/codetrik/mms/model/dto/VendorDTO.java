package ng.com.codetrik.mms.model.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ng.com.codetrik.mms.model.embeddable.VendorAccountDetail;
import ng.com.codetrik.mms.model.embeddable.VendorGuarantorDetail;
import org.springframework.hateoas.RepresentationModel;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VendorDTO extends RepresentationModel<VendorDTO>{
    private UUID id;
    private String name;//vendor name
    private String address;//vendor address
    private String bussinessName;//vendor CAC bussiness name
    private String registrationNumber;//Vendor's CAC registration number 
    private String phoneNumber;//vendor's phone number
    private boolean credibility;
    private String email;//vendor's email to be use in indexing the vendor table  
    private String siteCode;//to be use in indexing site
    private String operatorEmail;//to be use in indexing opperator
    private VendorAccountDetail accountDetail;//wrapper for storing vendor bank details
    private VendorGuarantorDetail vendorGuarantorDetail;
}
