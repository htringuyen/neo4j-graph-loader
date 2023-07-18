package org.snowj.synthea.ingest.parser;

import org.snowj.synthea.ingest.loader.OnErrorOption;
import org.springframework.core.env.Environment;

import java.io.File;

public class IngestProperty {
    private final Environment env;

    public IngestProperty(Environment env) {
        this.env = env;
    }

    public File getCsvDir() throws InvalidConfigException {
        var path = env.getProperty("csv.dir.base");
        if (path == null) {
            throw new InvalidConfigException("csv.dir.base is not set", "csv.dir.base");
        }

        var dir = new File(path);

        if (!dir.isDirectory()) {
            throw new InvalidConfigException("csv.dir.base is not a directory", "csv.dir.base");
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
            return Integer.parseInt(env.getProperty("csv.batchsize"));
        }
        catch (NumberFormatException | NullPointerException e) {
            return 1000;
        }
    }

    public OnErrorOption getOnErrorOption() {
        var option = OnErrorOption.fromString(env.getProperty("csv.loading.onerror"));

        return option != null ? option : OnErrorOption.TERMINATE_PROGRAM;
    }

    public File getPreloadFile() throws InvalidConfigException{
        var fileName = env.getProperty("csv.preload.name");
        if (fileName == null) {
            return null;
        }

        var file = new File(getCypherDir() + File.separator + fileName);

        if (!file.isFile()) {
            throw new InvalidConfigException("csv.preload.name is not a file", "csv.preload.name");
        }

        return file;
    }
}
