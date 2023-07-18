package org.snowj.synthea.ingest.loader;

import org.neo4j.driver.Query;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionConfig;

public interface CypherRunner {
    public CypherResult execute(Session session, TransactionConfig txConfig);

    public Query getQuery();
}
