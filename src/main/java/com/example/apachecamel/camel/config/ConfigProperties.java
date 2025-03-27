package com.example.apachecamel.camel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "file-transfer")
public class ConfigProperties {

    private String inputDir;
    private String outputDir;
    private String inputFileName;
    private String outputFileName;

}
