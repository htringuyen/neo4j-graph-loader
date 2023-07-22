package org.snowj.synthea.ingest.parser.csvfile;

import org.snowj.synthea.ingest.loader.OnErrorOption;
import org.snowj.synthea.ingest.parser.IngestBean;
import org.snowj.synthea.ingest.parser.IngestParsingException;
import org.snowj.synthea.ingest.parser.PreloadBean;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.TreeMap;

public class CsvIngestParser {
    private static final String FILE_PROTOCOL = "file://";
    public static List<PreloadBean> parsePreloadBeans(File cypher) throws IngestParsingException {
        return Arrays.stream(parseCypher(cypher).split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(PreloadBean::new)
                .collect(Collectors.toList());
    }

    public static Map<String, IngestBean> parseIngestBeans(File csvDir, File cypherDir, int batchsize, OnErrorOption option) throws IngestParsingException {
        var csvNames = csvDir.list();
        if (csvNames == null) {
            throw new IngestParsingException("Error listing csv directory: " + csvDir.getAbsolutePath());
        }

        return Arrays.stream(csvNames)
                .filter(name -> name.endsWith(".csv"))
                .collect(TreeMap::new, (map, csvName) -> {
                    var ingestBean =
                            IngestBeanImpl.builder()
                                    .csvUrl(FILE_PROTOCOL + csvDir.getPath() + File.separator + csvName)
                                    .rowCypher(parseCypher(new File(cypherDir, csvName.replace(".csv", ".cql")),
                                            "//TODO: " + csvName.replace(".csv", ".cql")))
                                    .batchsize(batchsize)
                                    .onError(option)
                                    .build();
                    map.put(csvName, ingestBean);
                }, TreeMap::putAll);
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
