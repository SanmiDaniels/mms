/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.com.codetrik.mms.serviceTest;

import java.util.UUID;
import javax.transaction.Transactional;
import ng.com.codetrik.mms.MmsApplication;
import ng.com.codetrik.mms.model.Operator;
import ng.com.codetrik.mms.model.Site;
import ng.com.codetrik.mms.service.SiteService;
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
public class SiteServiceTest {
    
    @Autowired 
    SiteService siteService;
    
    private final Logger LOGGER = LoggerFactory.getLogger(SiteServiceTest.class);
    public SiteServiceTest() {
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
    public void createSiteTest(){
        Site site = new Site("Yewo Mini-grid", "85KWp", "Ogun State", "Ijebu-East", 
                "Agboro", 320, 265, 12, 4, 3, 3, 1152000, 288000, 4, "SMA", "MODEL2356", 
                "MODEL4567", "SMA", "Hamzat Habibllahi Adewale", "hamzat.adewale@rubitecsolar.net","YY56798");
        Site s = siteService.createSite(site);
        Assert.isInstanceOf(Site.class, s);
        
    }
    @Test
    @DirtiesContext
    @Transactional
    public void queryByIdTest(){
        UUID uuid = UUID.fromString("3c596cfb-9ebf-4978-99a1-ca17418a9208");
        LOGGER.info("-------> UUID derived from the string is of variant {}",uuid.variant());
        LOGGER.info("-------> UUID derived from the string is of version {}",uuid.version());
        Site site = siteService.queryById(uuid);       
        LOGGER.info("-------> operator {}",site.toString());
        Assert.isInstanceOf(Site.class, site);
    }    
    
    @Test
    @DirtiesContext
    @Transactional
    public void updateSiteTestByFetchingBySitecode(){
        String siteCode = "YY56798";
        Site ses = siteService.queryBySiteCode(siteCode);//assumed site was sent to the frontend ealier with this line and get returned
        LOGGER.info("-------> operator {}",ses.toString());
        ses.setSettlement("Yewo");
        Site s =siteService.updateSite(ses);
        LOGGER.info("-------> operator {}",s);
        
        
    }
    @Test
    @DirtiesContext
    @Transactional
    public void queryBySiteCodeTest(){
        String siteCode = "GG56782";
        Site se = siteService.queryBySiteCode(siteCode);
        LOGGER.info("-------> operator {}",se.toString());
        se.setLga("Ilupeju");
        LOGGER.info("-------> operator {}",se.toString());
    }
*/
}
