package ng.com.codetrik.mms.model;

import ng.com.codetrik.mms.model.embeddable.VendorAccountDetail;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;
import ng.com.codetrik.mms.model.embeddable.VendorGuarantorDetail;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Value;

@Entity
@SQLDelete(sql = "update vendor set is_deleted=true where id=?")
@Where(clause = "is_deleted=false")
@Table(name = "vendor",schema = "minigrid_management_system")
@Data
public class Vendor implements Serializable {
    /******************Table Fields*****************************/
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    
    @NotNull
    private String name;//vendor name
    
    @NotNull
    private String address;//vendor address
    
    @NotNull
    @Column(name="bussiness_name")
    private String bussinessName;//vendor CAC bussiness name
    
    @NotNull
    @Column(name="cac_registration_number")
    private String registrationNumber;//Vendor's CAC registration number 
    
    @NotNull
    @Column(name="phone_number")
    private String phoneNumber;//vendor's phone number
    
    @Embedded
    private VendorAccountDetail accountDetail;//wrapper for storing vendor bank details
    
    @Embedded
    private VendorGuarantorDetail vendorGuarantorDetail;
    
    @ManyToOne
    private Site site;
    
    @ManyToOne
    private Operator operator;
    
    @OneToMany(mappedBy = "vendor")
    private List<Revenue> revenueFromVendor;
    
    @Column(name="is_deleted")
    @Value("false")
    private boolean isDeleted;//flag for soft delete    

    @Email(message="does not look like an email")
    @Column(unique = true)
    @NotNull
    private String email;//vendor's email to be use in indexing the vendor table    
    
    @UpdateTimestamp 
    @Column(name="last_updated_time")
    private LocalDateTime lastUpdatedTime;//automatically picked local last updated time 
    
    @CreationTimestamp
    @Column(name="last_created_time")
    private LocalDateTime lastCreatedTime;//automatically picked local last created time 
    
    /****************Transient Variables*********************/
    @Transient
    @NotNull
    private String siteCode;//to be use in indexing site
    
    @Transient
    @Email
    @NotNull
    private String operatorEmail;//to be use in indexing opperator
    
    /***************************Constructors*************************/           
    public Vendor(){}

    public Vendor(String name, String address, String bussinessName, String registrationNumber, String phoneNumber, VendorAccountDetail accountDetail, VendorGuarantorDetail vendorGuarantorDetail, String email, String siteCode, String operatorEmail) {
        this.name = name;
        this.address = address;
        this.bussinessName = bussinessName;
        this.registrationNumber = registrationNumber;
        this.phoneNumber = phoneNumber;
        this.accountDetail = accountDetail;
        this.vendorGuarantorDetail = vendorGuarantorDetail;
        this.email = email;
        this.siteCode = siteCode;
        this.operatorEmail = operatorEmail;
    }
    
    /*********************Entity life Cycle methods***********************/
    
    @PreRemove
    public void preRemove(){
        this.isDeleted=true;
    }

    /**********************************Getters and Setters***********************/
     
}
