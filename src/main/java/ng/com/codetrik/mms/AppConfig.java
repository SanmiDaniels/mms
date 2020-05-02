package ng.com.codetrik.mms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan("ng.com.codetrik.mms")
public class AppConfig implements WebMvcConfigurer{
   
    @Bean 
    public ViewResolver getViewResolver(){
        InternalResourceViewResolver irvr = new InternalResourceViewResolver();
        irvr.setPrefix("/WEB-INF/JSP/");
        irvr.setSuffix(".jsp");
        return irvr;     
    }
   
}
