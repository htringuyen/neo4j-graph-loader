package org.snowj.synthea.ingest.parser;

import org.neo4j.driver.Query;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionConfig;
import org.snowj.synthea.ingest.loader.CypherResult;
import org.snowj.synthea.ingest.loader.CypherResultImpl;
import org.snowj.synthea.ingest.loader.CypherRunner;

import java.util.concurrent.TimeUnit;

public class PreloadBean implements CypherRunner {
    private final String cypher;

    public PreloadBean(String cypher) {
        this.cypher = cypher;
    }

    @Override
    public Query getQuery() {
        return new Query(cypher);
    }

    @Override
    public CypherResult execute(Session session, TransactionConfig txConfig) {
        var result = session.run(getQuery(), txConfig);
        return new CypherResultImpl(
                false, // successful
                null, // errorMessages
                result.consume().counters(),
                result.consume().resultAvailableAfter(TimeUnit.MILLISECONDS),
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public String toString() {
        return getQuery().text();
    }
}
