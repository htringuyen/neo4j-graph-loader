package org.snowj.synthea.ingest.parser;

import org.snowj.synthea.ingest.loader.OnErrorOption;

import java.util.List;
import java.util.Map;

public interface IngestManager {
    public Map<String, IngestBean> getIngestBeans();

    public List<ExecutingBean> getPreLoadingBeans();

    public List<ExecutingBean> getPostLoadingBeans();

    public OnErrorOption getOnErrorOption();
}
