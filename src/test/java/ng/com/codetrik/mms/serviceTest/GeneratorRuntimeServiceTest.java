/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.com.codetrik.mms.serviceTest;

import ng.com.codetrik.mms.MmsApplication;
import ng.com.codetrik.mms.model.GeneratorRuntime;
import ng.com.codetrik.mms.service.GeneratorRuntimeService;
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

@SpringBootTest(classes = MmsApplication.class)
public class GeneratorRuntimeServiceTest {
    
    @Autowired 
    GeneratorRuntimeService genRunService;
    private final Logger LOGGER = LoggerFactory.getLogger(GeneratorRuntimeServiceTest.class);
    
    public GeneratorRuntimeServiceTest() {
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
        LOGGER.info("-------->Application Context sucessfully loaded for GeneratorRuntimeServiceTest");
    }
 /*
    @Test
    public void createRuntimeTest(){
        GeneratorRuntime genRun = new GeneratorRuntime(2020, "GG56783", "MIK12890990", 5, 1, 12, 8, 1, 17, 50);
        genRunService.createRuntime(genRun);
    }
*/
}
