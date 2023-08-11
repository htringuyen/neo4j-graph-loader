package org.snowj.synthea.ingest.parser.csvfile;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snowj.synthea.ingest.loader.GraphLoader;
import org.snowj.synthea.ingest.loader.OnErrorOption;
import org.snowj.synthea.ingest.parser.InvalidConfigException;
import org.springframework.core.env.Environment;

import java.io.File;

public class CsvIngestProperty {
    private static final Logger logger = LoggerFactory.getLogger(GraphLoader.class);

    private final Environment env;

    public CsvIngestProperty(Environment env) {
        this.env = env;
    }

    public File getCsvDir() throws InvalidConfigException {
        var path = env.getProperty("csv.dir.base");
        if (path == null) {
            throw new InvalidConfigException("csv.dir.base is not set", "csv.dir.base");
        }

        var dir = new File(path);

        if (!dir.isDirectory()) {
            throw new InvalidConfigException("csv.dir.base [" + getPreLoadingFile().getAbsolutePath() + "] is not a directory", "csv.dir.base");
        }

        return dir;
    }

    public File getCypherDir() {
        var path = env.getProperty("cypher.dir.base");
        if (path == null) {
            return null;
        }

        var dir = new File(path);

        if (!dir.isDirectory()) {
            return null;
        }

        return dir;
    }

    public int getBatchsize() {
        try {
            return Integer.parseInt(env.getProperty("ingesting.batchsize"));
        }
        catch (NumberFormatException | NullPointerException e) {
            return 1000;
        }
    }

    public OnErrorOption getOnErrorOption() {
        var option = OnErrorOption.fromString(env.getProperty("csv.loading.onerror"));

        return option != null ? option : OnErrorOption.TERMINATE_PROGRAM;
    }

    public File getPreLoadingFile() throws InvalidConfigException{
        var fileName = env.getProperty("csv.preloading.name");
        if (fileName == null) {
            return null;
        }

        var file = new File(getCypherDir(), fileName);

        if (!file.isFile()) {
            throw new InvalidConfigException("csv.preloading.name ["
                    + file.getAbsolutePath() + "] is not a file", "csv.preload.name");
        }

        return file;
    }

    public File getPostLoadingFile() throws InvalidConfigException{
        var fileName = env.getProperty("csv.postloading.name");
        if (fileName == null) {
            return null;
        }

        var file = new File(getCypherDir(), fileName);

        if (!file.isFile()) {
            throw new InvalidConfigException("csv.postloading.name ["
                    + file.getAbsolutePath() + "] is not a file", "csv.postloading.name");
        }

        return file;
    }

    @PostConstruct
    public void validate() throws InvalidConfigException {
        logger.info("csv.dir.base: {}", this.getCsvDir());
        logger.info("cypher.dir.base: {}", this.getCypherDir());
        logger.info("csv.batchsize: {}", this.getBatchsize());
        logger.info("csv.loading.onerror: {}", this.getOnErrorOption());
        logger.info("csv.preloading.file: {}", this.getPreLoadingFile());
        logger.info("csv.postloading.file: {}", this.getPostLoadingFile());
    }
}
