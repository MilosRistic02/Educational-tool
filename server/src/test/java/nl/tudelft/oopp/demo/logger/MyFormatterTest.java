package nl.tudelft.oopp.demo.logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.junit.jupiter.api.Test;

class MyFormatterTest {

    @Test
    void format() {
        LogRecord logRecord = new LogRecord(Level.INFO, "Test");
        MyFormatter formatter = new MyFormatter();
        String expected = new Date(logRecord.getMillis()) + " : Test\n";
        assertEquals(expected, formatter.format(logRecord));
    }
}