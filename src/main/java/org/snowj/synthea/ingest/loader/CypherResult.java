package org.snowj.synthea.ingest.loader;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface CypherResult {
    public boolean hasError();
    public List<String> errorMessages();
    public long executingTime(TimeUnit unit);

    public long constraintsAdded();
    public long constraintsRemoved();
    public long indexesAdded();
    public long indexesRemoved();
    public long labelsAdded();
    public long labelsRemoved();
    public long nodesCreated();
    public long nodesDeleted();
    public long propertiesSet();
    public long relationshipsCreated();
    public long relationshipsDeleted();
}
