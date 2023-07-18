package org.snowj.synthea.ingest.parser.csvfile;

import org.snowj.synthea.ingest.loader.OnErrorOption;
import org.snowj.synthea.ingest.parser.IngestBean;
import org.snowj.synthea.ingest.parser.IngestParsingException;
import org.snowj.synthea.ingest.parser.PreloadBean;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IngestParser {
    public static PreloadBean parsePreloadBean(File cypher) throws IngestParsingException {
        return new PreloadBean(parseCypher(cypher));
    }

    public static List<IngestBean> parseIngestBeans(File csvDir, File cypherDir, int batchsize, OnErrorOption option) throws IngestParsingException {
        var csvNames = csvDir.list();
        if (csvNames == null) {
            throw new IngestParsingException("Error listing csv directory: " + csvDir.getAbsolutePath());
        }

        return Arrays.stream(csvNames)
                .filter(name -> name.endsWith(".csv"))
                .map(name -> name.substring(0, name.length() - 4))
                .map(name -> {
                    return IngestBeanImpl.builder()
                            .csvUrl(csvDir.getPath() + File.separator + name + ".csv")
                            .rowCypher(parseCypher(new File(cypherDir, name + ".cypher"),
                                    "//TODO: " + name + ".cypher"))
                            .batchsize(batchsize)
                            .onError(option)
                            .build();
                }).toList();
    }

    private static String parseCypher(File cypher) throws IngestParsingException {
        try (var lines = Files.lines(cypher.toPath())) {
            return lines.collect(Collectors.joining("\n"));
        }
        catch (Exception e) {
            throw new IngestParsingException("Error parsing cypher file: " + cypher.getAbsolutePath(), e);
        }
    }

    private static String parseCypher(File cypher, String defaultCypher) {
        try (var lines = Files.lines(cypher.toPath())) {
            return lines.collect(Collectors.joining("\n"));
        }
        catch (Exception e) {
            return defaultCypher;
        }

    }


}
