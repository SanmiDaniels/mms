/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.com.codetrik.mms.model.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Detail {
    private String siteCode;
    private String vendorEmail;
    private String serialNumber;//serial number associated with the generator
}
