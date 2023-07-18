package org.snowj.synthea.ingest.parser;

import org.snowj.synthea.ingest.loader.OnErrorOption;

import java.util.List;

public interface IngestManager {
    public List<IngestBean> getIngestBeans();

    public PreloadBean getPreloadBean();

    public OnErrorOption getOnErrorOption();
}
