/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.com.codetrik.mms.serviceTest;

import java.util.UUID;
import javax.transaction.Transactional;
import ng.com.codetrik.mms.MmsApplication;
import ng.com.codetrik.mms.model.Generator;
import ng.com.codetrik.mms.service.GeneratorService;
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
import org.springframework.util.Assert;

@SpringBootTest(classes = MmsApplication.class)
public class GeneratorServiceTest {
    
    @Autowired
    GeneratorService genService;
    
    private final Logger LOGGER = LoggerFactory.getLogger(GeneratorServiceTest.class);
    
    public GeneratorServiceTest() {
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
        LOGGER.info("-------->Application Context sucessfully loaded for GeneratorServiceTest");
    }
   
    @Test
    public void createGenerator(){
        Generator gen = new Generator("Honda", "85KVA", "HON1789856","GG56783","hamzat.adewale@rubitecsolar.net");
        Generator g = genService.createGenerator(gen);
        Assert.isInstanceOf(Generator.class, g);
    }
    @Test
    @Transactional
    public void updateGenerator(){
        Generator generator = new Generator(UUID.fromString("8d448245-8d42-4a9a-bc07-ac6166566ad6"),"Honda", "50KVA", "HON1789856","GG56783","hamzat.adewale@rubitecsolar.net");
        Generator g = genService.updateGenerator(generator);
        Assert.isInstanceOf(Generator.class, g);
    }
    @Test
    public void  queryBySerialNumberTest(){
        Generator gen = genService.queryBySerialNumber("MIK12890990");
        LOGGER.info("-------> Generator {}",gen.toString());
        Assert.isInstanceOf(Generator.class, gen);
    }
    
}
