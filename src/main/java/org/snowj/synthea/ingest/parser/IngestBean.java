package org.snowj.synthea.ingest.parser;


import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionConfig;
import org.snowj.synthea.ingest.loader.CypherResult;
import org.snowj.synthea.ingest.loader.CypherResultImpl;
import org.snowj.synthea.ingest.loader.CypherRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class IngestBean implements CypherRunner {

    public abstract List<String> extractErrorMessages(Result result);

    @Override
    public CypherResult execute(Session session, TransactionConfig txConfig) {
        var result = session.run(getQuery(), txConfig);
        var errorMessages = extractErrorMessages(result);
        return new CypherResultImpl(
                ! errorMessages.isEmpty(),
                errorMessages,
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
