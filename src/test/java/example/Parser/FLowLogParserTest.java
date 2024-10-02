package example.Parser;

import org.example.Parser.FlowLogParser;
import java.io.IOException;

import org.example.Parser.Tags;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FLowLogParserTest {

    private FlowLogParser processor;

    @BeforeEach
    void setUp() {
        processor = new FlowLogParser();
    }

    @Test
    @DisplayName("This is the Sample Test which was Already Given")
    void testTagCountAndPortProtocol() throws IOException {

        processor.loadLookUpTable("src/test/resources/Testcase1/lookup_table_test_1.csv");
        processor.processFlowLogs("src/test/resources/Testcase1/flow_logs_test_1.txt");

        assertEquals(2, processor.getTagCount().getOrDefault(Tags.sv_P1.toString().toLowerCase(), -1));
        assertEquals(3, processor.getTagCount().getOrDefault(Tags.email.toString().toLowerCase(), -1));
        assertEquals(1, processor.getTagCount().getOrDefault(Tags.sv_P2.toString().toLowerCase(), -1));
        assertEquals(8, processor.getTagCount().getOrDefault(Tags.Untagged.toString(), -1));


        assertEquals(1, processor.getPortProtocolCount().getOrDefault("49153-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("49154-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("49155-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("49156-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("49157-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("49158-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("80-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("1024-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("443-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("23-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("25-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("110-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("993-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("143-tcp", 0).intValue());


        processor.generateReport("src/test/resources/Testcase1/outputfile_test_1.csv");
    }




    @Test
    @DisplayName("Testcase no 2")
    void testcase2() throws IOException {

        processor.loadLookUpTable("src/test/resources/Testcase2/lookup_table_test_2.csv");
        processor.processFlowLogs("src/test/resources/Testcase2/flowlog_tests_2.txt");

        assertEquals(2, processor.getTagCount().getOrDefault("sv_p2", 0).intValue());
        assertEquals(1, processor.getTagCount().getOrDefault("sv_p3", 0).intValue());
        assertEquals(2, processor.getTagCount().getOrDefault(Tags.Untagged.toString(), 0).intValue());

        assertEquals(1, processor.getPortProtocolCount().getOrDefault("3389-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("8080-udp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("443-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("8443-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("1234-udp", 0).intValue());


        processor.generateReport("src/test/resources/Testcase2/outputfile_test_2.csv");
    }

    @Test
    public void testCase3_EmptyLookupTable() throws IOException {
        FlowLogParser processor = new FlowLogParser();

        processor.loadLookUpTable("src/test/resources/Testcase3/lookup_table_test_3.csv");
        processor.processFlowLogs("src/test/resources/Testcase3/flowlog_tests_3.txt");

        // Check tag counts
        assertEquals(5, processor.getTagCount().getOrDefault(Tags.Untagged.toString(), 0).intValue());

        // Check port/protocol counts
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("443-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("80-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("1024-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("22-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("8080-udp", 0).intValue());

        processor.generateReport("src/test/resources/Testcase3/outputfile_test_3.csv");
    }
    @Test
    public void testCase4_ICMPProtocol() throws IOException {
        FlowLogParser processor = new FlowLogParser();

        processor.loadLookUpTable("src/test/resources/Testcase4/lookup_table_test_4.csv");
        processor.processFlowLogs("src/test/resources/Testcase4/flowlog_tests_4.txt");

        // Check tag counts
        assertEquals(1, processor.getTagCount().getOrDefault("sv_p5", 0).intValue());
        assertEquals(1, processor.getTagCount().getOrDefault("sv_p2", 0).intValue());
        assertEquals(1, processor.getTagCount().getOrDefault("sv_p4", 0).intValue());
        assertEquals(2, processor.getTagCount().getOrDefault(Tags.Untagged.toString(), 0).intValue());

        // Check port/protocol counts
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("0-icmp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("80-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("1024-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("22-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("443-tcp", 0).intValue());

        processor.generateReport("src/test/resources/Testcase4/outputfile_test_4.csv");
    }

    @Test
    public void testCase5_UnlistedPortsInLookupTable() throws IOException {
        FlowLogParser processor = new FlowLogParser();

        processor.loadLookUpTable("src/test/resources/Testcase5/lookup_table_test_5.csv");
        processor.processFlowLogs("src/test/resources/Testcase5/flowlog_tests_5.txt");

        assertEquals(1, processor.getTagCount().getOrDefault("sv_p4", 0).intValue());
        assertEquals(4, processor.getTagCount().getOrDefault(Tags.Untagged.toString(), 0).intValue());


        assertEquals(1, processor.getPortProtocolCount().getOrDefault("3306-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("2080-udp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("3389-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("22-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("9999-tcp", 0).intValue());

        processor.generateReport("src/test/resources/Testcase5/outputfile_test_5.csv");
    }

    @Test
    public void testCase6_DuplicateEntries() throws IOException {
        FlowLogParser processor = new FlowLogParser();

        processor.loadLookUpTable("src/test/resources/Testcase6/lookup_table_test_6.csv");
        processor.processFlowLogs("src/test/resources/Testcase6/flowlog_tests_6.txt");

        // Check tag counts
        assertEquals(2, processor.getTagCount().getOrDefault("sv_p2", 0).intValue());
        assertEquals(2, processor.getTagCount().getOrDefault("sv_p1", 0).intValue());
        assertEquals(1, processor.getTagCount().getOrDefault("sv_p4", 0).intValue());

        // Check port/protocol counts
        assertEquals(2, processor.getPortProtocolCount().getOrDefault("443-tcp", 0).intValue());
        assertEquals(2, processor.getPortProtocolCount().getOrDefault("80-tcp", 0).intValue());
        assertEquals(1, processor.getPortProtocolCount().getOrDefault("22-tcp", 0).intValue());

        processor.generateReport("src/test/resources/Testcase6/outputfile_test_6.csv");
    }

    @Test
    public void testCase7_isCorrectVersion() throws IOException {
        FlowLogParser processor = new FlowLogParser();

        processor.loadLookUpTable("src/test/resources/Testcase7/lookup_table_test_7.csv");
        processor.processFlowLogs("src/test/resources/Testcase7/flowlog_tests_7.txt");

        processor.generateReport("src/test/resources/Testcase7/outputfile_test_7.csv");
    }

}
