package com.example.purchaserequests;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Подсчитываем количество строк в результирующем заппросе
 */
public class GetResultSetRowCount {
    public static int getResultSetRowCount(ResultSet resultSet) {
        int size = 0;
        try {
            resultSet.last();
            size = resultSet.getRow();
            resultSet.beforeFirst();
        }
        catch(SQLException ex) {
            return 0;
        }
        return size;
    }
}
