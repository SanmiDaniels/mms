package ng.com.codetrik.mms.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Value;

@Entity @SQLDelete(sql = "update revenue set is_deleted=true where id=?") @Where(clause = "is_deleted=false") 
@Table(name = "revenue",schema = "minigrid_management_system") @Data
public class Revenue implements Serializable {
    /***************Table Fields**********************/
    @Id @GeneratedValue(strategy = GenerationType.AUTO) 
    private long id;
    
    @Column(name = "expected_amount")
    private double expectedAmount;//amount expected to be deposited by the vendor (basic + last rollover)
    
    @Column(name = "deposited_amount") @Value("0.00") @NotNull
    private double depositedAmount;//ammount deposited by the vendor to be provided or taken as 0.00 default
    
    @NotNull
    private double basic;//basic amount to be deposited by vendor to be provided
    
    private double rollover;//(expectedAmount - depositedAmount)
    
    @Column(name = "excess_of")
    private double excessOf;
    
    @Column(name="expectation_time") @CreationTimestamp @ToString.Exclude
    private LocalDateTime expectationTime;//value to be derived at service-tier
    
    @Column(name="delivery_time") @UpdateTimestamp @ToString.Exclude
    private LocalDateTime deliveryTime;//value to be derived at service-tier
    
    @ManyToOne @ToString.Exclude
    private Vendor vendor;

    @NotNull @Email(message="value supplied for vendorEmail does not look like an email") @Column(name = "vendor_email")
    private String vendorEmail;// to be use in indexing vendor
    
    @Value("false") @Column(name="is_deleted") @ToString.Exclude
    private boolean isDeleted; //field for soft delete
        
    /************Transient Variables*******************/
    @Transient @Value("0.0")
    private Double slipOver;
        
    /**********************Constructors***********************************/
    
    public Revenue(){}

    public Revenue(double expectedAmount, double depositedAmount, double basic, Double rollover, double excessOf, Vendor vendor, String vendorEmail, double slipOver) {
        this.expectedAmount = expectedAmount;
        this.depositedAmount = depositedAmount;
        this.basic = basic;
        this.rollover = rollover;
        this.excessOf = excessOf;
        this.vendor = vendor;
        this.vendorEmail = vendorEmail;
        this.slipOver = slipOver;
    }

   public Revenue(double basic, double depositedAmount, String vendorEmail) {
        this.basic = basic;
        this.vendorEmail = vendorEmail;
        this.depositedAmount = depositedAmount;
    }    
   public Revenue(long id, double basic, double depositedAmount, String vendorEmail) {
        this.basic = basic;
        this.vendorEmail = vendorEmail;
        this.depositedAmount = depositedAmount;
        this.id = id;
    }    

    /*************************Entity life cycle methods**************************/
    @PreRemove
    public void preRemove(){
        this.isDeleted=true;
    }   
    
    /***********************Getters and Setters*********************************/
    
    
}
