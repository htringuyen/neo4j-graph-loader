package org.snowj.synthea.ingest.loader;

import org.neo4j.driver.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snowj.synthea.ingest.parser.ExecutingBean;
import org.snowj.synthea.ingest.parser.IngestManager;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.snowj.synthea.ingest.parser.IngestBean;

public class GraphLoader implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(GraphLoader.class);
    private Driver driver;
    private String database;
    private IngestManager ingestManager;
    private boolean needToCleanGraph;

    public GraphLoader(IngestManager ingestManager, String dbUri, String dbUser, String dbPassword, String database, boolean toCleanGraph) {
        this.driver = GraphDatabase.driver(dbUri, AuthTokens.basic(dbUser, dbPassword));
        this.ingestManager = ingestManager;
        this.database = database;
        this.needToCleanGraph = toCleanGraph;
    }

    public void loadNeo4jGraph() {
        try (Session session = driver.session(SessionConfig.forDatabase(database))) {
            var timeCounter = new TimeCounter();

            timeCounter.start();
            if (needToCleanGraph) {
                cleanGraph(session);
            }
            timeCounter.stopAndLogElapsedTime();


            var txConfig =
                    TransactionConfig.builder()
                    .withTimeout(Duration.ofSeconds(100))
                    .build();

            logHeader("Execute pre-loading cypher before ingesting");
            runExecutingBeans(session, txConfig, ingestManager.getPreLoadingBeans());

            for (Map.Entry<String, IngestBean> ingestBean: ingestManager.getIngestBeans().entrySet()) {
                logHeader("Execute ingest cypher: " + ingestBean.getKey());
                logCypher(ingestBean.getValue().getQuery().text());
                timeCounter.start();
                var result = ingestBean.getValue().execute(session, txConfig);
                timeCounter.stopAndLogElapsedTime();
                logIngestResult(result);
                logErrorMessages(result.errorMessages());
                if (result.hasError() && ingestManager.getOnErrorOption() == OnErrorOption.TERMINATE_PROGRAM) {
                    throw new CypherExecutingException("There are errors in ingest cypher execution [OnErrorOption.TERMINATE_PROGRAM]");
                }
            }

            logHeader("Execute post-loading cypher after ingesting");
            timeCounter.start();
            runExecutingBeans(session, txConfig, ingestManager.getPostLoadingBeans());
            timeCounter.stopAndLogElapsedTime();
            timeCounter.countAndLogTotalElapsedTime();
        }
        catch (Exception e) {
            logger.error("Error while loading graph");
            logger.error("Caused by: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void runExecutingBeans(Session session, TransactionConfig txConfig, List<ExecutingBean> executingBeans) throws CypherExecutingException {
        for (var executingBean: executingBeans) {
            logCypher(executingBean.getQuery().text());
            var result = executingBean.execute(session, txConfig);
            logExecutingResult(result);
            logErrorMessages(result.errorMessages());
            if (result.hasError()) {
                throw new CypherExecutingException("There are errors when executing cypher: " + executingBean.getQuery().text());
            }
        }
    }

    private void cleanGraph(Session session) {
        logger.info("Cleaning graph");
        String cypher = "MATCH (n) CALL {WITH n DETACH DELETE n} IN TRANSACTIONS OF 1000 ROWS";
        logCypher(cypher);
        var result = session.run(cypher);
        logger.info("Nodes deleted: {}", result.consume().counters().nodesDeleted());
        logger.info("Relationships deleted: {}", result.consume().counters().relationshipsDeleted());
    }

    private void logHeader(String header) {
        logger.info("==================================================");
        logger.info(header);
        logger.info("==================================================");
    }

    private void logCypher(String cypher) {
        logger.info("Executing cypher:");
        logger.info("--------------------------------------------------");
        logger.info(cypher);
        logger.info("--------------------------------------------------");
    }

    private void logExecutingResult(CypherResult result) {
        logger.info("Executing cypher result:");
        logger.info("--------------------------------------------------");
        logger.info("Has error: {}", result.hasError());
        logger.info("Executing time: {} ms", result.executingTime(TimeUnit.MILLISECONDS));
        logger.info("Constraints added: {}", result.constraintsAdded());
        logger.info("Constraints removed: {}", result.constraintsRemoved());
        logger.info("Indexes added: {}", result.indexesAdded());
        logger.info("Indexes removed: {}", result.indexesRemoved());
        logger.info("--------------------------------------------------");
    }

    private void logIngestResult(CypherResult result) {
        logger.info("Ingest cypher result:");
        logger.info("--------------------------------------------------");
        logger.info("Has error: {}", result.hasError());
        logger.info("Executing time: {} ms", result.executingTime(TimeUnit.MILLISECONDS));
        logger.info("Nodes created: {}", result.nodesCreated());
        logger.info("Nodes deleted: {}", result.nodesDeleted());
        logger.info("Relationships created: {}", result.relationshipsCreated());
        logger.info("Relationships deleted: {}", result.relationshipsDeleted());
        logger.info("Properties set: {}", result.propertiesSet());
        logger.info("Labels added: {}", result.labelsAdded());
        logger.info("Labels removed: {}", result.labelsRemoved());
        logger.info("Indexes added: {}", result.indexesAdded());
        logger.info("Indexes removed: {}", result.indexesRemoved());
        logger.info("Constraints added: {}", result.constraintsAdded());
        logger.info("Constraints removed: {}", result.constraintsRemoved());
        logger.info("--------------------------------------------------");
    }

    private void logErrorMessages(List<String> errorMessages) {
        if (errorMessages == null) {
            return;
        }
        logger.info("Error messages:");
        logger.info("--------------------------------------------------");
        for (String errorMessage: errorMessages) {
            logger.info(errorMessage);
        }
        logger.info("--------------------------------------------------");
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }






}
