package org.snowj.synthea.ingest.parser.csvfile;

import org.neo4j.driver.Query;
import org.neo4j.driver.Result;
import org.snowj.synthea.ingest.loader.OnErrorOption;
import org.snowj.synthea.ingest.parser.IngestBean;

import java.util.List;

public class IngestBeanImpl extends org.snowj.synthea.ingest.parser.IngestBean {
    private final String cypher;
    private IngestBeanImpl(String cypher) {
        this.cypher = cypher;
    }

    @Override
    public List<String> extractErrorMessages(Result result) {
        return result.stream().filter(r -> r.get("started").asBoolean() && ! r.get("committed").asBoolean())
                .map(r -> String.format("Line %d: %s", r.get("linenumber").asInt(), r.get("errorMessage").asString()))
                .toList();
    }

    @Override
    public Query getQuery() {
        return new Query(cypher);
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String csvUrl;
        private String rowCypher;
        private int batchsize;
        private OnErrorOption option;

        public Builder csvUrl(String csvUrl) {
            this.csvUrl = csvUrl;
            return this;
        }

        public Builder rowCypher(String cypher) {
            this.rowCypher = cypher;
            return this;
        }

        public Builder batchsize(int batchsize) {
            this.batchsize = batchsize;
            return this;
        }

        public Builder onError(OnErrorOption option) {
            this.option = option;
            return this;
        }

        private String buildCsvUrl() {
            return csvUrl;
        }

        private String buildRowCypher() {
            return rowCypher;
        }

        public int buildBatchsize() {
            return batchsize;
        }

        private String buildOnErrorOption() {
            switch (option) {
                case TERMINATE_PROGRAM:
                    return "BREAK";
                case BREAK_CURRENT_LOADING:
                    return "BREAK";
                case IGNORE_LOADING_ERROR:
                    return "CONTINUE";
            }

            throw new IllegalStateException("Invalid state!");
        }

        public IngestBean build() {
            return new IngestBeanImpl(
                    String.format(
                            "LOAD CSV WITH HEADER FROM '%s' AS row\n" +
                                    "CALL {\n" +
                                    "%s\n" +
                                    "}\n" +
                                    "IN TRANSACTIONS OF %d ROWS\n" +
                                    "ON ERROR %s\n" +
                                    "REPORT STATUS AS s\n" +
                                    "RETURN linenumber() AS linenumner, s.committed AS committed, " +
                                    "       s.errorMessage AS errorMessage, s.started AS started, " +
                                    "       s.transactionId AS transactionId",
                            buildCsvUrl(),
                            buildRowCypher(),
                            buildBatchsize(),
                            buildOnErrorOption()
                    )
            );
        }
    }
}
