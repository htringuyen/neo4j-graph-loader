package org.snowj.synthea.ingest.loader;

import org.neo4j.driver.Driver;
import org.snowj.synthea.ingest.parser.PreloadBean;

import java.util.List;
import org.snowj.synthea.ingest.parser.IngestBean;

public class GraphLoader {
    private List<IngestBean> ingestedBeans;
    private PreloadBean preloadBean;
    private final Driver driver;

    public GraphLoader(Driver driver) {
        this.driver = driver;

    }




}
