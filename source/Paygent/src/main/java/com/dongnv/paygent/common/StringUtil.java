package com.dongnv.paygent.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.util.Map;

public class StringUtil {


    public static String convertObjectToString(Object object) {
        try {
            if (object == null) {
                return null;
            }
            if (object instanceof String) {
                return (String) object;
            }
            if (object instanceof Boolean) {
                return Boolean.toString((Boolean) object);
            }
            if (object instanceof Map) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString((Map) object);
            }
            if (object instanceof Clob) {
                Clob clob = (Clob) object;
                Reader reader = clob.getCharacterStream();
                StringWriter writer = new StringWriter();
                char[] buffer = new char[1024];
                int length;
                while ((length = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, length);
                }
                return writer.toString();
            }
            return object.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isNotNullAndNotEmpty(String value) {
        if (value != null) {
            return value.trim().length() > 0;
        }

        return false;
    }
}
