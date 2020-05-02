package ng.com.codetrik.mms.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

@Entity
@SQLDelete(sql = "update operators set is_deleted=true where id=?")
@Where(clause = "is_deleted=false")
@Table(name = "operator",schema = "minigrid_management_system")
@Cacheable(value = true)
@Data
public class Operator implements Serializable {
    
    /*****************Table Fields**********************************/
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")//generates Version4 UUID which is Random number based UUID
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    
    private String name;//name of the mini-grid operator|developer
    
    @Email(message = "email address not valid")
    @Column(unique = true)
    @NotNull
    private String email;//email address of the operator|developer
    
    @Length(min = 8,message = "password cant be less than 8 characters")
    private String password;//password of the operator for authentification 
    
    @Column(name = "site_count")
    private int siteCount;//number of site currently owned by operator
    
    private String address;//head office address of the opperator 
    
    @UpdateTimestamp
    @Column(name="last_updated_time")
    private LocalDateTime lastUpdatedTime;//automatically picked local last updated time 
    
    @CreationTimestamp
    @Column(name="last_created_time")
    private LocalDateTime lastCreatedTime;//automatically picked local last created time 
    
    @Value("false")
    @Column(name="is_deleted")
    private boolean isDeleted;//flag for soft delete
    
    @OneToMany(mappedBy = "operator")
    private List<Site> site;//site(s) associated to this operator
    
    @OneToMany(mappedBy = "operator")
    private List<Vendor> vendor;//vendor(s) associated to this operator
    
    @OneToMany(mappedBy = "operator")
    private List<Generator> generator;//generator(s) associated to this opperator
    
    @OneToMany(mappedBy = "operator")
    private List<Expenditure> expenditure;
    
    @OneToMany(mappedBy = "operator")
    private List<Recipient> recipient;
    
    /*****************************Constructors********************************/
    public Operator(){}

    public Operator(String name, String email, String password, int siteCount, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.siteCount = siteCount;
        this.address = address;
    }

    public Operator(UUID id, String name, String email, String password, int siteCount, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.siteCount = siteCount;
        this.address = address;
    }    

    /***************************Entity life cycle methods*********************/
    @PreRemove  
    public void preRemove(){
        this.isDeleted=true;
    }
    
    /*******************************Getters and Setters ************************/
    

    @Override
    public String toString() {
        return "Operator Internal generated id = " + id + ", \nname = " + name + ", \nemail = " + email + ", \npassword = " + password + 
                ", \nsiteCount = " + siteCount + ", \naddress = " + address;
    }
            
}
