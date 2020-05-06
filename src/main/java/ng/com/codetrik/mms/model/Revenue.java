/*
when a record is made to this table, the internal generated id of such record will be send back along with the email send to notify creation of this record,
this internal id shall be use to complete the record once the vedor pays the expected delivery to the account.

for transaction with rollover, weekly email will be generated to notify of the rollover, and internal id for that record shall be append to the message to
update the record if the rollover as been cleared.


*/
package ng.com.codetrik.mms.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
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
    private Long id;
    
    @Column(name = "extected_amount") @NotNull
    private Double expectedAmount;//amount expected to be deposited by the vendor (basic + last rollover)
    
    @Column(name = "deposited_amount") @Value("0")
    private Double depositedAmount;//ammount deposited by the vendor
    
    private Double basic;//basic amount to be deposited 
      
    private Double rollover;//(depositedAmount - basic)
    
    @Column(name="expectation_time")
    private LocalDateTime expectationTime;//value to be derived at service-tier
    
    @Column(name="delivery_time")
    private LocalDateTime deliveryTime;//value to be derived at service-tier
    
    @ManyToOne
    private Vendor vendor;

    @UpdateTimestamp
    @Column(name="last_updated_time")
    private ZonedDateTime lastUpdatedTime;//automatically picked local last updated time 
    
    @CreationTimestamp @Column(name="last_created_time")
    private ZonedDateTime lastCreatedTime;//automatically picked local last created time 
    
    @Value("false") @Column(name="is_deleted")
    private boolean isDeleted; //field for soft delete
        
    /************Transient Variables*******************/
    @NotNull @Transient
    private int Year;//will be the same for both date of expectation and date of delivery
    
    @NotNull @Transient @Min(value = 1) @Max(value = 12)
    private int Month;//will be the same for both date of expectation and date of delivery
    
    @NotNull @Transient @Max(value=31) @Min(value=1)
    private int expectationDay;//instant day of the week the extected revenue was recorded
    
    @NotNull @Transient @Max(value=23) @Min(value=0)
    private int expectationHour;//instant hour of the day the extected revenue was recorded
    
    @NotNull @Transient @Max(value=59) @Min(value=0)
    private int expectationMinute;//instant minute of the hour the extected revenue was recorded

    @NotNull @Transient @Max(value=31) @Min(value=0)
    private int deliveryDay;//instant day of the week the payment was made to settle the expectation
    
    @NotNull @Transient @Max(value=23) @Min(value=0)
    private int deliveryHour;//instant hour of the day the payment was made to settle the expectation
    
    @NotNull @Transient @Max(value=59) @Min(value=0)
    private int deliveryMinute;//instant minute of the hour the payment was made to settle the expectation
    
    /***************************Transient Variable******************************************/
    
    @Transient @NotNull @Email(message="value supplied for vendorEmail does not look like an email")
    private String vendorEmail;// to be use in indexing vendor
    
    /**********************Constructors***********************************/
    
    public Revenue(){}

    /*************************Entity life cycle methods**************************/
    @PreRemove
    public void preRemove(){
        this.isDeleted=true;
    }   
    
    /***********************Getters and Setters*********************************/
    
    
}
