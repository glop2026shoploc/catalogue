package com.glop.catalogue.out.garage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "garage")
public record GarageProperties(
        String endpoint,
        String accessKey,
        String secretKey,
        String regionName,
        String bucket
) {}