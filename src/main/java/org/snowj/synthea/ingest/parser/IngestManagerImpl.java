package org.snowj.synthea.ingest.parser;

import org.snowj.synthea.ingest.loader.OnErrorOption;


import java.util.List;
import java.util.Map;

public class IngestManagerImpl implements IngestManager {
    private List<ExecutingBean> preLoadingBeans;
    private List<ExecutingBean> postLoadingBeans;
    private Map<String, IngestBean> ingestBeans;
    private OnErrorOption onErrorOption;

    public IngestManagerImpl(List<ExecutingBean> preLoadingBeans, List<ExecutingBean> postLoadingBeans,
                             Map<String, IngestBean> ingestBeans, OnErrorOption onErrorOption) {
        this.preLoadingBeans = preLoadingBeans;
        this.postLoadingBeans = postLoadingBeans;
        this.ingestBeans = ingestBeans;
        this.onErrorOption = onErrorOption;
    }

    @Override
    public Map<String, IngestBean> getIngestBeans() {
        return ingestBeans;
    }

    @Override
    public List<ExecutingBean> getPreLoadingBeans()
    {
        return preLoadingBeans;
    }

    @Override
    public List<ExecutingBean> getPostLoadingBeans()
    {
        return postLoadingBeans;
    }

    @Override
    public OnErrorOption getOnErrorOption() {
        return onErrorOption;
    }
}
