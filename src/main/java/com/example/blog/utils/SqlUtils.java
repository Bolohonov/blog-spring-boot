package com.example.blog.utils;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class SqlUtils {

    public static Long getLong(ResultSet rs, String fieldName) throws SQLException {
        Object object = rs.getObject(fieldName);
        if (object == null) return null;
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }
        throw new IllegalArgumentException(" Can't extract long value from " + fieldName);
    }

    public static String getStringOrElseEmpty(ResultSet rs, String fieldName) throws SQLException {
        Object object = rs.getObject(fieldName);
        if (object == null) return "";
        if (object instanceof String) {
            return (String) object;
        }
        throw new IllegalArgumentException(" Can't extract long value from " + fieldName);
    }

    public static Boolean getBoolean(ResultSet rs, String fieldName) throws SQLException {
        Object object = rs.getObject(fieldName);
        if (object == null) return null;
        if (object instanceof String) {
            return "Y".equalsIgnoreCase((String) object);
        }
        throw new IllegalArgumentException(" Can't extract long value from " + fieldName);
    }

    public static LocalDateTime getLocalDateTime(ResultSet rs, String fieldName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(fieldName);
        if (timestamp == null) return null;
        return timestamp.toLocalDateTime();
    }

}
