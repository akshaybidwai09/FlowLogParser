package org.example.Parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FlowLogParser {

    private static final Logger logger = LoggerFactory.getLogger(FlowLogParser.class);
    private String UNTAGGED = "Untagged";
    private final String ERROR = "Error: Please correct flow log entries. Check if the input files are correct.\n";
    private Map<LookupTableEntry, String> lookUpTable = new HashMap<>();
    private Map<String, Integer> tagCount = new HashMap<>();
    private Map<String, Integer> portProtocolCount = new HashMap<>();
    private Map<String, String> protocolMap = Map.of(
            "6","tcp","17","udp","1","icmp"
    );
    private final long MAX_FLOW_LOG_SIZE = 10 * 1024 * 1024;

    public void loadLookUpTable(String lookupFilePath) throws IOException {

        logger.info("Loading lookup table from CSV file: {}", lookupFilePath);

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(lookupFilePath))) {
            String line;

            reader.readLine();

            // Reading and parsing each line in the CSV
            while ((line = reader.readLine()) != null) {
                // Split the line by comma
                String[] columns = line.split(",");

                if (columns.length == 3) {
                    String dstPort = columns[0].trim();
                    String protocol = columns[1].trim().toLowerCase();
                    String tag = columns[2].trim().toLowerCase();

                    LookupTableEntry entry = new LookupTableEntry(dstPort, protocol);
                    lookUpTable.put(entry, tag);
                } else {
                    logger.warn("Malformed CSV entry skipped: {}", line);
                }
            }
        }

        logger.info("Lookup table loaded with {} entries.", lookUpTable.size());
    }

    public void processFlowLogs(String flowLogFilePath) throws IOException {

        Path filePath = Paths.get(flowLogFilePath);

        long fileSize = Files.size(filePath);

        if (fileSize > MAX_FLOW_LOG_SIZE) {
            logger.error("Flow log file exceeds the 10 MB limit. File size: {} MB", fileSize / (1024 * 1024));
            return;
        }

        logger.info("Processing flow log file: {}", flowLogFilePath);
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                processFlowLogEntry(line);
            }
        }
    }

    private void processFlowLogEntry(String logEntry) {
        String[] columns = logEntry.split(" ");
        if (columns.length < 13) {
            logger.warn("Malformed flow log entry skipped: {}", logEntry);
            return;
        }

        String version = columns[0].trim();
        if (!"2".equals(version)) {
            logger.warn("Unsupported flow log version: {}. Skipping entry: {}", version, logEntry);
            return;
        }

        try{
            String dstPort = columns[6].trim();
            String protocol = protocolMap.getOrDefault(columns[7].trim(),"Unknown");
            LookupTableEntry entry = new LookupTableEntry(dstPort, protocol);

            String tag = lookUpTable.getOrDefault(entry, UNTAGGED);
            incrementTagCount(tag);
            incrementPortProtocolCount(dstPort.toLowerCase(), protocol.toLowerCase());

        } catch(NumberFormatException e){
            logger.debug("Parameters could not be parsed due to Datatype Mismatch", e.getMessage());
        }


    }

    private void incrementTagCount(String tag) {
        tagCount.put(tag, tagCount.getOrDefault(tag, 0) + 1);
    }

    private void incrementPortProtocolCount(String port, String protocol) {
        String key = port + "-" + protocol;
        portProtocolCount.put(key, portProtocolCount.getOrDefault(key, 0) + 1);
    }

    public Map<LookupTableEntry, String> getLookUpTable() {
        return lookUpTable;
    }

    public Map<String, Integer> getTagCount() {
        return tagCount;
    }

    public Map<String, Integer> getPortProtocolCount() {
        return portProtocolCount;
    }

    public void generateReport(String outputFilePath) throws IOException {
        logger.info("Generating report to file: {}", outputFilePath);
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {

            if (portProtocolCount.isEmpty()) {
                writer.write(ERROR);
                logger.error("No valid flow log entries were processed.");
                return;
            }
            writer.write("Tag Counts:\n");
            writer.write("Tag,Count\n");
            for (Map.Entry<String, Integer> entry : tagCount.entrySet()) {
                writer.write(String.format("%s,%d\n", entry.getKey(), entry.getValue()));
            }

            writer.write("\nPort/Protocol Combination Counts:\n");
            writer.write("Port,Protocol,Count\n");
            for (Map.Entry<String, Integer> entry : portProtocolCount.entrySet()) {
                String[] portProtocol = entry.getKey().split("-");
                writer.write(String.format("%s,%s,%d\n", portProtocol[0], portProtocol[1], entry.getValue()));
            }
        }
    }


}
