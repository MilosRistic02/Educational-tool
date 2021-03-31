package nl.tudelft.oopp.demo.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CharacterListConverterTest {

    private CharacterListConverter sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterListConverter();
    }

    @Test
    void convertToDatabaseColumn() {
        List<Character> chars = "success".chars()
                .mapToObj(e -> (char)e).collect(Collectors.toList());
        assertEquals("s,u,c,c,e,s,s", sut.convertToDatabaseColumn(chars));
    }

    @Test
    void convertToEntityAttribute() {
        List<Character> chars = "success".chars()
                .mapToObj(e -> (char)e).collect(Collectors.toList());
        assertEquals(chars, sut.convertToEntityAttribute("s,u,c,c,e,s,s"));
    }

}