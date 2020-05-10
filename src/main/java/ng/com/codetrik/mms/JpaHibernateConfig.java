package ng.com.codetrik.mms;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ng.com.codetrik.mms.repository")  //makes spring to help create proxy instances of Spring data Jpa repositories created 
@ComponentScan("ng.com.codetrik.mms.repository")
public class JpaHibernateConfig {
    
    @Bean 
    public DataSource jpaDataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setSchema("minigrid_management_system");
        ds.setUrl("jdbc:mysql://localhost:3306/minigrid_management_system?autoReconnect=true&serverTimezone=Africa/Lagos&useLegacyDatetimeCode=false");
        ds.setUsername("root");
        ds.setPassword("codeTRIK1992.");
        return ds;
    }  
 
   @Bean
   public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
      LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
      em.setDataSource(jpaDataSource());
      em.setPackagesToScan(new String[] { "ng.com.codetrik.mms.model.entity" });
      em.setPersistenceUnitName("punit");
      JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
      em.setJpaVendorAdapter(vendorAdapter);
      em.setJpaProperties(additionalProperties());
 
      return em;
   }
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }
 
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
    
    @Bean 
    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor(){
        return new PersistenceAnnotationBeanPostProcessor();
    }
    
    public Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        properties.setProperty("hibernate.format_sql", "true");

        return properties;
    }
}
