package ng.com.codetrik.mms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;

@Configuration
@EnableSwagger2WebMvc
@Import(SpringDataRestConfiguration.class)
public class SpringFoxConfig {

        @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiEndPointsInfo())
                .select().paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.any()) 
                .build()
                .pathMapping("/");
    }
    
    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title("Mini Grid Management System")
                .description("RESTful APIs for MMS")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }

}
