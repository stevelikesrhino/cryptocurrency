package com.example.cryptocurrency.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;

@Configuration
public class AppConfiguration {

    @Bean
    public RestTemplate getTemplate(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 1080);

        return new RestTemplate(requestFactory);
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
