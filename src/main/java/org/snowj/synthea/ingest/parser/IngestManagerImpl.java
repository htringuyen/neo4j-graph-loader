package org.snowj.synthea.ingest.parser;

import org.snowj.synthea.ingest.loader.OnErrorOption;


import java.util.List;
import java.util.Map;

public class IngestManagerImpl implements IngestManager {
    private List<PreloadBean> preloadBeans;
    private Map<String, IngestBean> ingestBeans;
    private OnErrorOption onErrorOption;

    public IngestManagerImpl(List<PreloadBean> preloadBeans, Map<String, IngestBean> ingestBeans, OnErrorOption onErrorOption) {
        this.preloadBeans = preloadBeans;
        this.ingestBeans = ingestBeans;
        this.onErrorOption = onErrorOption;
    }

    @Override
    public Map<String, IngestBean> getIngestBeans() {
        return ingestBeans;
    }

    @Override
    public List<PreloadBean> getPreloadBeans() {
        return preloadBeans;
    }

    @Override
    public OnErrorOption getOnErrorOption() {
        return onErrorOption;
    }
}
