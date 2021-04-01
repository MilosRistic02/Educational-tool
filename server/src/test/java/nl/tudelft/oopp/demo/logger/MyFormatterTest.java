package nl.tudelft.oopp.demo.logger;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.*;

class MyFormatterTest {

    @Test
    void format() {
        LogRecord logRecord = new LogRecord(Level.INFO, "Test");
        MyFormatter formatter = new MyFormatter();
        String expected = new Date(logRecord.getMillis()) + " : Test\n";
        assertEquals(expected, formatter.format(logRecord));
    }
}