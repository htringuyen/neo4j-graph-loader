package org.snowj.synthea.ingest.config;

import org.snowj.synthea.ingest.loader.GraphLoader;
import org.snowj.synthea.ingest.parser.IngestManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:ingest.properties")
public class GraphLoaderConfig {
    @Autowired
    private Environment env;

    @Autowired
    private IngestManager ingestManager;

    @Bean
    public GraphLoader graphLoader() {
        return new GraphLoader(ingestManager, env.getProperty("neo4j.url"),
                env.getProperty("neo4j.username"), env.getProperty("neo4j.password"),
                env.getProperty("neo4j.database"), Boolean.valueOf(env.getProperty("neo4j.graph.clean")));
    }
}
