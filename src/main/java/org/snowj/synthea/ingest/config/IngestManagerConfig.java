package org.snowj.synthea.ingest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snowj.synthea.ingest.loader.GraphLoader;
import org.snowj.synthea.ingest.parser.IngestManager;
import org.snowj.synthea.ingest.parser.IngestManagerImpl;
import org.snowj.synthea.ingest.parser.IngestParsingException;
import org.snowj.synthea.ingest.parser.InvalidConfigException;
import org.snowj.synthea.ingest.parser.csvfile.CsvIngestParser;
import org.snowj.synthea.ingest.parser.csvfile.CsvIngestProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:ingest.properties")
@Profile("filecsv")
public class IngestManagerConfig {
    private static final Logger logger = LoggerFactory.getLogger(GraphLoader.class);
    @Autowired
    private Environment env;

    @Bean
    public IngestManager ingestManager() throws InvalidConfigException, IngestParsingException {
        var prop = ingestProperty();
        var onErrorOption = prop.getOnErrorOption();
        var preloadBeans = CsvIngestParser.parsePreloadBeans(prop.getPreloadFile());
        var ingestBeans = CsvIngestParser.parseIngestBeans(
                prop.getCsvDir(), prop.getCypherDir(), prop.getBatchsize(), onErrorOption);
        return new IngestManagerImpl(preloadBeans, ingestBeans, onErrorOption);
    }

    @Bean
    public CsvIngestProperty ingestProperty() {
        return new CsvIngestProperty(env);
    }


}
