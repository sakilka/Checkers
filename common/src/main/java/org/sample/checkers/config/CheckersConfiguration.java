package org.sample.checkers.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class CheckersConfiguration {

    private String testConfig;

    @PostConstruct
    public void init() {
        this.testConfig = "load";
    }

    public String getTest(){
        return testConfig;
    }
}
