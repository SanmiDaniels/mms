/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.com.codetrik.mms.serviceTest;

import java.util.UUID;
import ng.com.codetrik.mms.MmsApplication;
import ng.com.codetrik.mms.model.Recipient;
import ng.com.codetrik.mms.service.RecipientService;
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
public class RecipientServiceTest {
    
    @Autowired
    RecipientService recipientService;
    
    private final Logger LOGGER = LoggerFactory.getLogger(RecipientServiceTest.class);
    
    public RecipientServiceTest() {
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
        LOGGER.info("-------->Application Context sucessfully loaded for RecipientServiceTest");
    }

    @Test
    @DirtiesContext
    public void createRecipientTest(){
        Recipient recipient = new Recipient("Isaik Tijani", "hamzat.adewale@rubitecsolar.net", "GG Manager", "habibllahi3@gmail.com");
        Assert.isInstanceOf(Recipient.class, recipientService.createRecipient(recipient));
    }
    
    @Test
    public void updateRecipientTest(){
        Recipient recipient = new Recipient(UUID.fromString("20c3ef53-ccae-4069-bc18-665ec3dc809e"),"Hamzat Habibllahi", "hamzat.adewale@rubitecsolar.net", "GG Manager", "habibllahi3@gmail.com");
        Assert.isInstanceOf(Recipient.class, recipientService.updateRecipient(recipient));
    }
    
    @Test
    public void queryByEmailTest(){
        Assert.isInstanceOf(Recipient.class, recipientService.queryByEmail("hamzat.adewale@rubitecsolar.net"));
    }
}
