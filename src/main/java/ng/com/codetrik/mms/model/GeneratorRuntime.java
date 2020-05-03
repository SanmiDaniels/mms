/*
At the attempt to create generator runtime record, site code provided is use to return back to user the list of generator available of that site and to each 
generator on the list is embedded such generator serial number.The UI, present this list as a dropdown for user to select the exact generator the record 
is meant for. The serial number of selected generator by the user is return back to the server appended to the record details for persistence.

the start runtime detail provided will be store in cookie first by the frontend and both start and runtime detail will be forwarded to us once
the stop runtime detail is available

an email will be sent to the recipient mails associated to the Operator. The email will contain inter generated id of the record, which must be provided 
along with the operator email and password if such record is to be updated.

*/
package ng.com.codetrik.mms.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import ng.com.codetrik.mms.model.enumeration.Months;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Value;

@Entity @SQLDelete(sql = "update generator_runtime set is_deleted=true where id=?") @Where(clause = "is_deleted=false")
@Table(name = "generator_runtime",schema = "minigrid_management_system") @Data
public class GeneratorRuntime implements Serializable {
    /*****************************Table Fields****************************/
    @Id @GeneratedValue(generator = "UUID") @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    
    @UpdateTimestamp @Column(name="last_updated_time")
    private LocalDateTime lastUpdatedTime;//automatically picked local last updated time 
    
    @CreationTimestamp @Column(name="last_created_time")
    private LocalDateTime lastCreatedTime;//automatically picked local last created time  
    
    @Column(name="start_time")
    private ZonedDateTime startTime;//value to be derived at service-tier
    
    @Column(name="stop_time")
    private ZonedDateTime stopTime;//value to be derived at service-tier
    
    private LocalTime runtime;//value to be derived at service level
    
    @Enumerated(EnumType.STRING) @Column(name = "run_month")
    private Months enumMonth;
    
    @ManyToOne
    private Generator generator;//generator associated to the recorded runtime
            
    @Value("false") @Column(name="is_deleted")
    private boolean isDeleted;//flag for soft delete
    
    @NotNull @Column(name="diesel_start_level")
    private Double dieselStartLevel;
    
    
    @NotNull @Column(name="diesel_stop_level")
    private Double dieselStopLevel;
    
    /************************************Transient variables****************************/

/*
    siteCode will be use in indexing site which we can then get list of generator related to that site and opperator that own the site, 
    send the generators relating to such site as list to user, expecting feedback of exact generator to register this run-time to
*/
    @Transient @NotNull
    private String siteCode;
    
    @Transient @NotNull
    private String generatorSerialNumber;//set this from the feedback of selected generator at dropped down provided at UI
    
    @NotNull
    private int year;//will be the same for both start and stop runtime detail
    
    @NotNull @Transient @Min(value = 1) @Max(value = 12)
    private int Month;//will be the same for both start and stop runtime detail
    
    @NotNull @Transient @Max(value=31) @Min(value=1)
    private int StartDay;//instant day of the week the generator was started
    
    @NotNull @Transient @Max(value=23) @Min(value=0)
    private int StartHour;//instant hour of the day the generator was started
    
    @NotNull @Transient @Max(value=59) @Min(value=0)
    private int StartMinute;//instant minute of the hour the generator was started

    @NotNull @Transient @Max(value=31) @Min(value=0)
    private int StopDay;//instant day of the week the generator was stoped
    
    @NotNull @Transient @Max(value=23) @Min(value=0)
    private int StopHour;//instant hour of the day the generator was stoped
    
    @NotNull @Transient @Max(value=59) @Min(value=0)
    private int StopMinute;//instant minute of the hour the generator was started    
    
    /********************constructors ************************************/
    
    public GeneratorRuntime(){}

    public GeneratorRuntime(int year, String siteCode, String generatorSerialNumber, int Month, int StartDay, int StartHour, int StartMinute, int StopDay, int StopHour, int StopMinute,
            Double dieselStartLevel, Double dieselStopLevel) {
        this.year = year;
        this.siteCode = siteCode;
        this.generatorSerialNumber = generatorSerialNumber;
        this.Month = Month;
        this.StartDay = StartDay;
        this.StartHour = StartHour;
        this.StartMinute = StartMinute;
        this.StopDay = StopDay;
        this.StopHour = StopHour;
        this.StopMinute = StopMinute;
        this.dieselStartLevel=dieselStartLevel;
        this.dieselStopLevel=dieselStopLevel;
    }
    public GeneratorRuntime(UUID id, int year, String siteCode, String generatorSerialNumber, int Month, int StartDay, int StartHour, int StartMinute, int StopDay, int StopHour, int StopMinute,
            Double dieselStartLevel, Double dieselStopLevel) {
        this.year = year;
        this.siteCode = siteCode;
        this.generatorSerialNumber = generatorSerialNumber;
        this.Month = Month;
        this.StartDay = StartDay;
        this.StartHour = StartHour;
        this.StartMinute = StartMinute;
        this.StopDay = StopDay;
        this.StopHour = StopHour;
        this.StopMinute = StopMinute;
        this.dieselStartLevel=dieselStartLevel;
        this.dieselStopLevel=dieselStopLevel;
        this.id = id;
    }    
    
    /**************************Entity life cycle methods ******************/
    @PreRemove
    public void preRemove(){
        this.isDeleted = true;
    }
    
    /******************Getters and Setters*******************************/

    @Override
    public String toString() {
        return "Generator Runtime id = " + id + ", \nstart Time = " + startTime + ", \nstop Time = " + stopTime + ", \nruntime = " + runtime;
    }
             
}
