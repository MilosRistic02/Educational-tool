package nl.tudelft.oopp.demo.converters;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntegerArrayConverterTest {

    private IntegerArrayConverter sut;

    @BeforeEach
    void setup() {
        sut = new IntegerArrayConverter();
    }

    @Test
    void convertToDatabaseColumn() {
        int[] ints = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertEquals("1,2,3,4,5,6,7,8,9", sut.convertToDatabaseColumn(ints));
    }

    @Test
    void convertToEntityAttribute() {
        int[] ints = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(ints, sut.convertToEntityAttribute("0,1,2,3,4,5,6,7,8,9"));
    }
}