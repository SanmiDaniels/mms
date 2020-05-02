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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "site",schema = "minigrid_management_system")
@SQLDelete(sql = "update sites set is_deleted=true where id=?")
@Where(clause = "is_deleted=false")
@Cacheable
@Data
public class Site implements Serializable{
    
    /*****************************Table Fields**********************************/
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    
    @UpdateTimestamp
    @Column(name="last_updated_time")
    private LocalDateTime lastUpdatedTime;//automatically picked local last updated time 
    
    @CreationTimestamp
    @Column(name="last_created_time")
    private LocalDateTime lastCreatedTime;//automatically picked local last created time 
    
    @NotNull(message = "name property cant be null")
    private String name;//name of the operator site
    
    @NotNull
    private String capacity;//capacity of the site 
    
    @NotNull
    private String province;//state in which the site is located 
    
    @NotNull
    private String lga;//local goverment of the site
    
    @NotNull
    private String settlement;//community the site is located
     
    @NotNull
    @Column(name="number_of_panel")
    private int numberOfPV;//number of phot-voltaic cell used to develop the mini-grid
    
    @Column(name="peak_watt_per_panel")
    @NotNull
    private int peakWattPerPV;//peak watt output per PV
    
    @Column(name="number_of_battery_inverter")
    @NotNull
    private int numberOfBatteryInverter;//number of battery inverter used 
    
    @Column(name="number_of_pv_inverter")
    @NotNull
    private int numberOfPVInverter;//unber of PV inverter used 
    
    @Column(name="pv_inverter_phase_count")
    @NotNull
    private int numberOfPhasePerPVInverter;//umber of phase per pV inverter
    
    @Column(name="battery_inverter_phase_count")
    @NotNull
    private int numberOfPhasePerBatteryInverter;//number of phase per battery inverter 
    
    @Column(name="total_bank_power")
    @NotNull
    private int totalBankPower;//total power of the bank in watt
    
    @Column(name="per_cluster_bank_power")
    @NotNull
    private int perClusterBankPower;//power of bank serving a single cluster 
    
    @Column(name="number_of_cluster")
    @NotNull
    private int numberOfCluster;//number of cluster 
    
    @Column(name="battery_inverter_brand")
    @NotNull
    private String BatteryInverterBrand;//brand of battery inverter
    
    @Column(name="battery_inverter_model")
    @NotNull
    private String batteryInverterModel;//model of battery inverter 
    
    @Column(name="pv_inverter_model")
    @NotNull
    private String pvInverterModel;//model of pv inverter 
    
    @Column(name="pv_inverter_brand")
    @NotNull
    private String PVInverterBrand;//brand of pv inveter
    
    @Value("false")
    @Column(name="is_deleted")
    private boolean isDeleted;//flag for soft delete 
    
    @NotNull
    @Column(name="current_site_manager")
    private String currentSiteManager;
    
    @OneToMany(mappedBy = "site")
    private List<Generator> generator;
    
    @OneToMany(mappedBy = "site")
    private List<Vendor> vendor;
    
    @OneToMany(mappedBy = "site")
    private List<Expenditure> expenditure;

    @ManyToOne
    private Operator operator;//opperator related to the site
       
    @NotNull
    @Column(name="site_code",unique = true)
    private String siteCode;
    
    /**********************Transient  Variables********************************/
    
    @Transient
    @NotNull(message = "email property cant be null, required for indexing and can be supplied from cookie")
    private String operatorEmail;//to be use in indexing Operator table
    
    /***********************Constructors**************************************/
    public Site(){ }

    public Site(String name, String capacity, String province, String lga, String settlement, int numberOfPV, int peakWattPerPV, 
            int numberOfBatteryInverter, int numberOfPVInverter, int numberOfPhasePerPVInverter, int numberOfPhasePerBatteryInverter, 
            int totalBankPower, int perClusterBankPower, int numberOfCluster, String BatteryInverterBrand, String batteryInverterModel, 
            String pvInverterModel, String PVInverterBrand, String currentSiteManager, String operatorEmail, String siteCode) {
        this.name = name;
        this.capacity = capacity;
        this.province = province;
        this.lga = lga;
        this.settlement = settlement;
        this.numberOfPV = numberOfPV;
        this.peakWattPerPV = peakWattPerPV;
        this.numberOfBatteryInverter = numberOfBatteryInverter;
        this.numberOfPVInverter = numberOfPVInverter;
        this.numberOfPhasePerPVInverter = numberOfPhasePerPVInverter;
        this.numberOfPhasePerBatteryInverter = numberOfPhasePerBatteryInverter;
        this.totalBankPower = totalBankPower;
        this.perClusterBankPower = perClusterBankPower;
        this.numberOfCluster = numberOfCluster;
        this.BatteryInverterBrand = BatteryInverterBrand;
        this.batteryInverterModel = batteryInverterModel;
        this.pvInverterModel = pvInverterModel;
        this.PVInverterBrand = PVInverterBrand;
        this.currentSiteManager = currentSiteManager;
        this.operatorEmail = operatorEmail;
        this.siteCode=siteCode;
    }
    

    /**************************Entity Life Cycle methods**************************/
    @PreRemove
    public void preRemove(){
        this.isDeleted=true;
    }
    
    /*************************Getters and Setters ******************************/

    @Override
    public String toString() {
        return "Site internal generated id = " + id + ", \nname = " + name + ", \ncapacity = " + capacity + ", \nprovince = " + province + 
                ", \nlga = " + lga + ", \nsettlement = " + settlement + 
                ", \nnumberOfPV = " + numberOfPV + ", \npeakWattPerPV = " + peakWattPerPV + 
                ", \nnumberOfBatteryInverter = " + numberOfBatteryInverter + ", \nnumberOfPVInverter = " + numberOfPVInverter + 
                ", \nnumberOfPhasePerPVInverter = " + numberOfPhasePerPVInverter + 
                ", \nnumberOfPhasePerBatteryInverter = " + numberOfPhasePerBatteryInverter + ", \ntotalBankPower = " + totalBankPower + 
                ", \nperClusterBankPower = " + perClusterBankPower + ", \nnumberOfCluster = " + numberOfCluster + 
                ", \nBatteryInverterBrand = " + BatteryInverterBrand + ", \nbatteryInverterModel = " + batteryInverterModel +
                ", \npvInverterModel = " + pvInverterModel + ", \nPVInverterBrand = " + PVInverterBrand + 
                ", \ncurrentSiteManager = " + currentSiteManager + 
                ", \nsiteCode = " + siteCode + ", \nOperator Email = " + operatorEmail;
    }
    
}
