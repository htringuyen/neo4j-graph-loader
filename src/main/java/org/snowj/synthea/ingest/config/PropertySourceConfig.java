package org.snowj.synthea.ingest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:ingest.properties")
public class PropertySourceConfig {
}
