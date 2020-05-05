
package ng.com.codetrik.mms.model;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Value;

@Entity @SQLDelete(sql = "update recipient set is_deleted=true where id=?") @Where(clause = "is_deleted=false")
@Table(name = "recipient" ,schema = "minigrid_management_system") @Data
public class Recipient implements Serializable {
    /********************Table Fields******************/    
    @Id @GeneratedValue(generator = "UUID") @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    
    @NotNull
    private String name;
    
    @Email(message="value provided for email does not look like an email")
    private String email;
    
    @NotNull
    private String role;
    
    @ManyToOne
    private Operator operator;
    
    @Value("false")
    @Column(name="is_deleted")
    private boolean isDeleted;//flag for soft delete    
    
    
    /****Transient variables***********/
    @Transient @Email(message="value provided for operatorEmail does not look like an email") @NotNull
    private String operatorEmail;//to be use in indexing the operator that owns this recipient
    
    /********constructor**************/
    public Recipient(){}

    public Recipient(String name, String email, String role, String operatorEmail) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.operatorEmail = operatorEmail;
    }
    public Recipient(UUID id, String name, String email, String role, String operatorEmail) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.operatorEmail = operatorEmail;
        this.id = id;
    }    
    /*****************************Entity life cycles methods **************************/
    
    @PreRemove
    public void preRemove(){
        this.isDeleted=true;
    }    

    /******************************Getter and setter*******************************/
    @Override
    public String toString() {
        return "Recipient internal generated id = " + id + ", \nname = " + name + ", \nemail = " + email + ", \nrole = " + role +
                ", \noperatorEmail = " + operatorEmail;
    }
    
}
