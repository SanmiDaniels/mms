/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.com.codetrik.mms.serviceTest;

import java.util.UUID;
import ng.com.codetrik.mms.MmsApplication;
import ng.com.codetrik.mms.model.entity.Vendor;
import ng.com.codetrik.mms.model.embeddable.VendorAccountDetail;
import ng.com.codetrik.mms.model.embeddable.VendorGuarantorDetail;
import ng.com.codetrik.mms.service.VendorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.Assert;

@SpringBootTest(classes = MmsApplication.class)
public class VendorServiceTest {
    @Autowired 
    VendorService vendorService;
    private final Logger LOGGER = LoggerFactory.getLogger(SiteServiceTest.class);
    
    public VendorServiceTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void contextLoads() {
        LOGGER.info("-------->Application Context sucessfully loaded for SiteServiceTest");
    }
/*    
    @Test
    @DirtiesContext
    public void createVendorTest(){
        var vad = new VendorAccountDetail("0107841806", "Guarantee Trust Bank");
        var vgd = new VendorGuarantorDetail("Sonekan Abiola", "Kuforiji Road Gbamugbamu", "08055445409");
        var vendor = new Vendor("Ibrahim Babafemi", "mokola street gbamugbamu", "Hoppies Nigeria Limited", "BC45678", "08056789021", vad, vgd, 
                "obasa@gmail.com", "GG56782", "hamzat.adewale@rubitecsolar.net",false);
         Assert.isInstanceOf(Vendor.class, vendorService.createVendor(vendor));
    }
    @Test
    public void queryByEmailTest(){
        Assert.isInstanceOf(Vendor.class, vendorService.queryByEmail("obasa@gmail.com"));
    }
    @Test
    public void queryByIdTest(){
        Assert.isInstanceOf(Vendor.class, vendorService.queryById(UUID.fromString("3d4e8fcb-f8ac-47cc-8775-726ff80c3f13")));
    }    
    @Test
    public void updateVendorTest(){
        var vad = new VendorAccountDetail("0107841810", "Acess Diamond Bank");
        var vgd = new VendorGuarantorDetail("Ibiyemi Abiola", "Kuforiji Road Gbamugbamu", "08055445409");
        var vendor = new Vendor(UUID.fromString("3d4e8fcb-f8ac-47cc-8775-726ff80c3f13"),"Ibrahim Babafemi", "mokola street gbamugbamu", "Mofo Nigeria Limited", "BC45678", "08056789021", vad, vgd, 
                "obasa@gmail.com", "GG56782", "hamzat.adewale@rubitecsolar.net",false); 
        Assert.isInstanceOf(Vendor.class, vendorService.updateVendor(vendor));
    }
*/   
}
