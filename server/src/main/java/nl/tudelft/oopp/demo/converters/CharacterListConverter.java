package nl.tudelft.oopp.demo.converters;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CharacterListConverter implements AttributeConverter<List<Character>, String> {
    @Override
    public String convertToDatabaseColumn(List<Character> attribute) {
        String res = "";
        for (int i = 0; i < attribute.size(); i++) {
            res += attribute.get(i);
            if (i < attribute.size() - 1) {
                res += ",";
            }
        }
        return res;
    }

    @Override
    public List<Character> convertToEntityAttribute(String dbData) {
        List<Character> res = new ArrayList<>();
        String[] entries = dbData.split(",");
        for (int i = 0; i < entries.length; i++) {
            res.add(entries[i].charAt(0));
        }
        return res;
    }
}
