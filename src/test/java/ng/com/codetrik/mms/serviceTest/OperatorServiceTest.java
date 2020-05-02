package ng.com.codetrik.mms.serviceTest;

import java.util.UUID;
import ng.com.codetrik.mms.MmsApplication;
import ng.com.codetrik.mms.model.Operator;
import ng.com.codetrik.mms.service.OperatorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.Assert;

@SpringBootTest(classes = MmsApplication.class)
public class OperatorServiceTest {
    @Autowired     
    OperatorService operatorService;
    
    private final Logger LOGGER = LoggerFactory.getLogger(OperatorServiceTest.class);
    
    public OperatorServiceTest() {
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
        LOGGER.info("-------->Application Context sucessfully loaded for OperatorServiceTest");
    }
    /*
    @Test
    @DirtiesContext
    public void createOperatorTest(){
        Operator operator = new Operator("Rubitec Nigeria Limited", "habibllahi3@gmail.com", "rubitectsolar2020", 1, "5 Talabi Street, Ikeja Lagos");
        operatorService.createOperator(operator);
    }
    @Test
    @DirtiesContext
    public void queryByEmailTest(){
        Operator opp = operatorService.queryByEmail("hamzat.adewale@rubitecsolar.net");
        LOGGER.info("-------> ID of the queried opperator is {}",opp.getId());
        LOGGER.info("-------> ID of the queried opperator is of variant {}",opp.getId().variant());
        LOGGER.info("-------> ID of the queried opperator is of version {}",opp.getId().version());
        Assert.isInstanceOf(Operator.class, opp);
    }
    
    @Test
    @DirtiesContext
    public void queryByIdTest(){
        UUID uuid = UUID.fromString("c03449f2-b641-49b7-903e-2374b2a1ea88");
        LOGGER.info("-------> UUID derived from the string is of variant {}",uuid.variant());
        LOGGER.info("-------> UUID derived from the string is of version {}",uuid.version());
        Operator opp = operatorService.queryById(uuid);       
        LOGGER.info("-------> operator is  is {}",opp);
        Assert.isInstanceOf(Operator.class, opp);
    }   
    @Test
    @DirtiesContext
    public void updateOperatorTest(){
        Operator opp = operatorService.queryByEmail("hamzat.adewale@rubitecsolar.net");//assumed this Operator istance was returned from the frontend
        opp.setSiteCount(1);//assumed it got returned with sitcoutn uodated 
        Assert.isInstanceOf(Operator.class, operatorService.updateOperator(opp));
    }   
    */
}
