package test.zmh.filmtube.conf;

import com.littlenb.snowflake.sequence.IdGenerator;
import com.littlenb.snowflake.support.MillisIdGeneratorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {


    public static final long CHUNK_SIZE = 10485760; // 10MB
    public static final int MERGE_BUFFER_SIZE = 1048576; // 1MB

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowCredentials(true)
                        .maxAge(3600)
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public HttpMessageConverters gsonMessageConverters() {
        return new HttpMessageConverters(new GsonHttpMessageConverter());
    }

    @Bean
    public IdGenerator idGenerator() {
        MillisIdGeneratorFactory millisIdGeneratorFactory = new MillisIdGeneratorFactory(1483200000000L);
        return millisIdGeneratorFactory.create(1L);
    }


}
