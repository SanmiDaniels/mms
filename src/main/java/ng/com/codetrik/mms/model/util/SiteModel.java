
package ng.com.codetrik.mms.model.util;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SiteModel {
    private UUID id;
    private String name;
    private String siteCode;
    private String operatorEmail;
    private String capacity;
    private String province;
    private String lga;
    private String settlement;
    private int numberOfPV;
    private int peakWattPerPV;
    private int numberOfBatteryInverter;
    private int numberOfPVInverter;
    private int numberOfPhasePerPVInverter;
    private int numberOfPhasePerBatteryInverter;
    private int totalBankPower;
    private int perClusterBankPower;
    private int numberOfCluster;
    private String BatteryInverterBrand;
    private String batteryInverterModel;
    private String pvInverterModel;
    private String PVInverterBrand;
    private String currentSiteManager;
    
}
