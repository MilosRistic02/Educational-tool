package nl.tudelft.oopp.demo.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class IntegerArrayConverter implements AttributeConverter<int[], String> {
    @Override
    public String convertToDatabaseColumn(int[] list) {
        String res = "";
        for (int i = 0; i < list.length; i++) {
            res += list[i];
            if (i < list.length - 1) {
                res += ",";
            }
        }
        return res;
    }

    @Override
    public int[] convertToEntityAttribute(String string) {
        int[] res = new int[10];
        String[] entries = string.split(",");
        for (int i = 0; i < 10; i++) {
            res[i] = Integer.parseInt(entries[i]);
        }
        return res;
    }
}
