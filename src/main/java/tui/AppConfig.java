package tui;

import com.github.tuxychandru.spring.mustache.MustacheViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = "tui")
@PropertySource(value = "classpath:/application.properties")
@EnableWebMvc
public class AppConfig {

    @Bean
    public ViewResolver mustacheViewResolver() {
        MustacheViewResolver resolver = new MustacheViewResolver();
        resolver.setPrefix("views/");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertiesConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public static MultipartResolver multipartResolver() {
        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        cmr.setMaxUploadSize(1024 * 1024 * 10);
        return cmr;
    }
}
