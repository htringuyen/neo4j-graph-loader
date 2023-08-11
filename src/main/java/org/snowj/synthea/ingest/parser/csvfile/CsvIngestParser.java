package org.snowj.synthea.ingest.parser.csvfile;

import org.snowj.synthea.ingest.loader.OnErrorOption;
import org.snowj.synthea.ingest.parser.ExecutingBean;
import org.snowj.synthea.ingest.parser.IngestBean;
import org.snowj.synthea.ingest.parser.IngestParsingException;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.TreeMap;

public class CsvIngestParser {
    private static final String FILE_PROTOCOL = "file://";

    private static final String CYPHER_NAME_REGEX = "^\\d{2}-.+\\.cql$";
    private static final String CYPHER_EXTRACT_NAME_REGEX = "^\\d{2}-(.*?)\\.cql$";

    public static List<ExecutingBean> parseExecutingBeans(File cypher) throws IngestParsingException {
        return Arrays.stream(parseCypher(cypher).split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(ExecutingBean::new)
                .collect(Collectors.toList());
    }

    public static Map<String, IngestBean> parseIngestBeans(File csvDir, File cypherDir, int batchsize, OnErrorOption option) throws IngestParsingException {
        var cypherNames = cypherDir.list();
        if (cypherNames == null) {
            throw new IngestParsingException("Error listing csv directory: " + csvDir.getAbsolutePath());
        }
        Arrays.sort(cypherNames);

        return Arrays.stream(cypherNames)
                .filter(name -> name.matches(CYPHER_NAME_REGEX))
                .collect(TreeMap::new, (map, cypherName) ->
                {
                    var pattern = Pattern.compile(CYPHER_EXTRACT_NAME_REGEX);
                    var matcher = pattern.matcher(cypherName);
                    String csvName = null;
                    if (matcher.find()) {
                        csvName = matcher.group(1) + ".csv";
                    }
                    var ingestBean =
                            IngestBeanImpl.builder()
                                    .csvUrl(FILE_PROTOCOL + csvDir.getPath() + File.separator + csvName)
                                    .rowCypher(parseCypher(new File(cypherDir, cypherName),
                                            "//TODO: " + cypherName))
                                    .batchsize(batchsize)
                                    .onError(option)
                                    .build();
                    map.put(cypherName, ingestBean);
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
