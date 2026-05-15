package com.tmw.tracking.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class FlexibleDoubleDeserializer extends JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonParser parser, DeserializationContext context)
            throws IOException {
        String doubleString = parser.getText();

        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        Number number = null;
        try {
            number = format.parse(doubleString);
        } catch (ParseException e) {
            return null;
        }
        return number.doubleValue();
    }

}
