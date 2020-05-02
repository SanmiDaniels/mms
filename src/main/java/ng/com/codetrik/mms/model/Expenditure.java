/*
When an expenditure record is created, an operatorEmail will be sent to the recipient of the opperator to alert the recipient that an expenditure request was made.
to that operatorEmail is assigned an internal id of the expenditure record created, this id must be provided in order to update the record.

also to update an expenditure record, operatorEmail and password provided must match that of the opperator associated with the record.

*/
package ng.com.codetrik.mms.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
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
import ng.com.codetrik.mms.model.enumeration.ExpenditureStatus;
import ng.com.codetrik.mms.model.enumeration.ExpenditureType;
import ng.com.codetrik.mms.model.enumeration.Months;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Value;

@Entity @SQLDelete(sql = "update expenditure set is_deleted=true where id=?") @Where(clause = "is_deleted=false")
@Table(name = "expenditure" ,schema = "minigrid_management_system") @Data
public class Expenditure implements Serializable {
    
    /*******************************Table Field variables****************************/
    @Id @GeneratedValue(generator = "UUID") @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator") @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    
    private Double ammount;
    
    @Enumerated(EnumType.STRING) @Column(name="expenditure_type")
    private ExpenditureType enumType;//to be derived at service-tier using the type value provided
    
    @Enumerated(EnumType.STRING)
    private ExpenditureStatus enumStatus;
    
    @Enumerated(EnumType.STRING) @Column(name="request_month")
    private Months enumMonth;//to be derived at service-tier using the month value provided
    
    @Column(name="request_from") @NotNull
    private String name;
    
    @NotNull @Column(name="request_year")
    private int year;
    
    @ManyToOne
    private Operator operator;//operator associated to this exppenditure 
    
    @ManyToOne
    private Site site; //site associated to this expenditure
    
    
    @UpdateTimestamp @Column(name="last_processed_date")
    private LocalDateTime dateProcessed;//automatically picked local last updated time 
    
    @CreationTimestamp @Column(name="expenditure_creation_date")
    private LocalDateTime dateFiled;//automatically picked local last created time
    
    @Value("false") @Column(name="is_deleted")
    private boolean isDeleted; //field for soft delete
    
    @NotNull
    private String description; //expenditure description
    
    @NotNull @Email(message="requestBy property must be a valid email of the person requesting for this expenditure") @Column(name="request_by")
    private String requestorEmail;
    
    /***************************Transient variables********************************/
    @NotNull @Transient @Max(value=12) @Min(value=1)
    private int month;//to be use in enumerating the enumMonth
    
    @NotNull @Transient @Max(value=6) @Min(value=0)
    private int type;//to be use in enumerating the enumType

    @NotNull @Transient @Max(value=3) @Min(value=0)
    private int status;//to be use in enumerating the enumType    
   
    @Transient @NotNull @Email
    private String operatorEmail;//opperators operatorEmail to be use in indexing opperator whose expenditure is associated to
    
    @NotNull
    private String siteCode;
            
    /****************constructors************************************/    
    public Expenditure(){}
    
    /*********************Entity Life Cycle methods*************/
    @PreRemove
    public void preRemove(){
        this.isDeleted=true;
    }
   
    /********************************Getters and Setters*******************************/
    @Override
    public String toString() {
        return "Expenditure internal generated id = " + id + ", \nammount = " + ammount + ", \nenumType = " + enumType.toString() + 
                ", \n Status = " + enumStatus.toString() + ", Month = " + enumMonth.toString() + ", request from  = " + name 
                + ", \nyear = " + year + ", \ndescription = " + description + ", \nrequestorEmail = " + requestorEmail;
    }
        
}
