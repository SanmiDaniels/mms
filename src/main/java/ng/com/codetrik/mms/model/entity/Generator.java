package ng.com.codetrik.mms.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Value;

@Entity @SQLDelete(sql = "update generator set is_deleted=true where id=?") @Where(clause = "is_deleted=false")
@Table(name = "generator" ,schema = "minigrid_management_system") @Data
public class Generator implements Serializable {
    /**************************Table field**********************************/
    @Id @GeneratedValue(generator = "UUID") @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator") 
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    
    @NotNull
    private String name;//name of the generator
    
    @ManyToOne
    private Site site;//site related to the generator
    
    @NotNull
    private String capacity;//generator capacity 
    
    @ManyToOne
    private Operator operator;//grid ownner|developer associated to the generator
    
    @OneToMany(mappedBy = "generator")
    private List<GeneratorRuntime> generatorRuntime;//generator runtime(s) associated to the generator

    @UpdateTimestamp @Column(name="last_updated_time")
    private LocalDateTime lastUpdatedTime;//automatically picked last updated time 
    
    @CreationTimestamp @Column(name="last_created_time")
    private LocalDateTime lastCreatedTime;//automatically picked last created time
    
    @NotNull @Column(name = "serial_number",unique = true)
    private String serialNumber;//serial number associated with the generator
    
    @Value("false") @Column(name="is_deleted")
    private boolean isDeleted;//flag for soft delete
    
    /************************Transient variables ****************************/
    @Transient @Email @NotNull
    private String operatorEmail;//to be use in indexing operator and must be provided
    
    @Transient @NotNull
    private String siteCode;//to be use in indexing site and must be provided
    
    /**********************constructors*******************************/    
    public Generator(){}

    public Generator(String name, String capacity, String serialNumber, String siteCode, String operatorEmail) {
        this.name = name;
        this.capacity = capacity;
        this.serialNumber = serialNumber;
        this.siteCode=siteCode;
        this.operatorEmail=operatorEmail;
    }
    public Generator(UUID id,String name, String capacity, String serialNumber, String siteCode, String operatorEmail) {
        this.name = name;
        this.capacity = capacity;
        this.serialNumber = serialNumber;
        this.siteCode=siteCode;
        this.operatorEmail=operatorEmail;
        this.id = id;
    }    
    /*****************************Entity life cycles methods **************************/
    
    @PreRemove
    public void preRemove(){
        this.isDeleted=true;
    }
    /*****************************getters and setters methods****************************/

    
    @Override
    public String toString() {
        return "Generator internal generated id = " + id + ", \nname = " + name + ", \ncapacity = " + 
                capacity + ", \nserialNumber = " + serialNumber + ", \noperatorEmail = " + operatorEmail + ", \nsiteCode = " + siteCode;
    }
    
    
}
