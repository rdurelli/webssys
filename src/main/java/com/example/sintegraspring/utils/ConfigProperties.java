package com.example.sintegraspring.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "directory")
@Data
public class ConfigProperties {

    private String name;

}
