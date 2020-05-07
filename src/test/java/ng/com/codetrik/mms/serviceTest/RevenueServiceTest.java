package ng.com.codetrik.mms.serviceTest;

import ng.com.codetrik.mms.MmsApplication;
import ng.com.codetrik.mms.model.Revenue;
import ng.com.codetrik.mms.repository.RevenueRepository;
import ng.com.codetrik.mms.service.RevenueService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest(classes = MmsApplication.class)
public class RevenueServiceTest {
    
    @Autowired
    RevenueService revenueService;
    
    @Autowired
    RevenueRepository revenueRepo;
    
    private final Logger LOGGER = LoggerFactory.getLogger(RevenueServiceTest.class);    
    public RevenueServiceTest() {
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
    @Test
    public void createRevenueTest(){
        Assert.isInstanceOf(Revenue.class, revenueService.createRevenue(new Revenue(300000.00,100000.00,"obasa@gmail.com")));
    }
    @Test
    public void lastRevenueTest(){
        LOGGER.info("-------->Last revenue related to the vendor  email is {} ",revenueRepo.findLastRevenue("obasa@gmail.com"));
    }
    @Test
    public void updateRevenueTest(){
        Assert.isInstanceOf(Revenue.class, revenueService.updateRevenue(new Revenue(17L,700000.00,600000.00,"obasa@gmail.com")));
    }    
}
