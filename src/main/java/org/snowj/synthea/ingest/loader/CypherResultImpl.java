package org.snowj.synthea.ingest.loader;

import org.neo4j.driver.summary.SummaryCounters;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CypherResultImpl implements CypherResult {
    private final boolean successful;
    private final List<String> errorMessages;
    private final long executingTime;
    private final TimeUnit executingTimeUnit;

    private final SummaryCounters counters;

    public CypherResultImpl(
            boolean successful, List<String> errorMessages,
            SummaryCounters counters, long executingTime, TimeUnit executingTimeUnit) {
        this.successful = successful;
        this.errorMessages = errorMessages;
        this.counters = counters;
        this.executingTime = executingTime;
        this.executingTimeUnit = executingTimeUnit;
    }

    @Override
    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public List<String> errorMessages() {
        return errorMessages;
    }

    @Override
    public long executingTime(TimeUnit unit) {
        return unit.convert(executingTime, executingTimeUnit);
    }

    @Override
    public long constraintsAdded() {
        return counters.constraintsAdded();
    }

    @Override
    public long constraintsRemoved() {
        return counters.constraintsRemoved();
    }

    @Override
    public long indexesAdded() {
        return counters.indexesAdded();
    }

    @Override
    public long indexesRemoved() {
        return counters.indexesRemoved();
    }

    @Override
    public long labelsAdded() {
        return counters.labelsAdded();
    }

    @Override
    public long labelsRemoved() {
        return counters.labelsRemoved();
    }

    @Override
    public long nodesCreated() {
        return counters.nodesCreated();
    }

    @Override
    public long nodesDeleted() {
        return counters.nodesDeleted();
    }

    @Override
    public long propertiesSet() {
        return counters.propertiesSet();
    }

    @Override
    public long relationshipsCreated() {
        return counters.relationshipsCreated();
    }

    @Override
    public long relationshipsDeleted() {
        return counters.relationshipsDeleted();
    }

}
