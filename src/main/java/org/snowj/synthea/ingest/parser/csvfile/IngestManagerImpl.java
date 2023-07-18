package org.snowj.synthea.ingest.parser.csvfile;

import org.snowj.synthea.ingest.loader.OnErrorOption;
import org.snowj.synthea.ingest.parser.IngestBean;
import org.snowj.synthea.ingest.parser.IngestManager;
import org.snowj.synthea.ingest.parser.PreloadBean;

import java.util.List;

public class IngestManagerImpl implements IngestManager {
    private PreloadBean preloadBean;
    private List<IngestBean> ingestBeans;
    private OnErrorOption onErrorOption;

    public IngestManagerImpl(PreloadBean preloadBean, List<IngestBean> ingestBeans, OnErrorOption onErrorOption) {
        this.preloadBean = preloadBean;
        this.ingestBeans = ingestBeans;
        this.onErrorOption = onErrorOption;
    }

    @Override
    public List<IngestBean> getIngestBeans() {
        return ingestBeans;
    }

    @Override
    public PreloadBean getPreloadBean() {
        return preloadBean;
    }

    @Override
    public OnErrorOption getOnErrorOption() {
        return onErrorOption;
    }
}
