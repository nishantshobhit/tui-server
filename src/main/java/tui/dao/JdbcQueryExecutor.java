package tui.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Executes queries using the JDBC interface
 *
 * @author nishant.s
 */
@Component
public class JdbcQueryExecutor {

    @Autowired
    private DatabaseConstants constants;

    private Class<?> driverClass;
    private static final Logger logger = LoggerFactory.getLogger(JdbcQueryExecutor.class);

    @PostConstruct
    public void setup() {
        try {
            driverClass = Class.forName(constants.driver);
        } catch (ClassNotFoundException cause) {
            logger.error("!!!!!! NO DATABASE DRIVER FOUND !!!!!");
        }
    }

    protected PreparedStatement getPreparedStatement(Connection conn, String sql, Object... params)
            throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        int idx = 1;
        for (Object param : params) {
            if (param instanceof String) {
                stmt.setString(idx, (String)param);
            } else if (param instanceof Date) {
                stmt.setDate(idx, (Date)param);
            } else if (param != null) {
                throw new DaoException("Unsupported parameter passed to prepared statement: "+ sql);
            }
            idx++;
        }
        return stmt;
    }

    public int executeWrite(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(constants.databaseUrl, constants.user, constants.password);
            stmt = getPreparedStatement(conn, sql, params);
            int updates = stmt.executeUpdate();
            logger.info("{} rows updated", updates);
            return updates;
        } catch (SQLException se){
            throw new DaoException("executeQuery threw exception", se);
        } finally {
            try{
                if(stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException se2){
                // can't do anything
            }
        }
    }

    public List<Map<String, Object>> executeRead(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(constants.databaseUrl, constants.user, constants.password);
            stmt = getPreparedStatement(conn, sql, params);
            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> results = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> result = new LinkedHashMap<>();
                ResultSetMetaData meta = rs.getMetaData();
                for (int i=1; i<=meta.getColumnCount(); i++) {
                    String col = meta.getColumnName(i);
                    result.put(col, rs.getObject(col));
                }
                results.add(result);
            }
            rs.close();
            return results;
        } catch (SQLException se){
            throw new DaoException("executeQuery threw exception", se);
        } finally {
            try{
                if(stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch(SQLException se2){
                // can't do anything
            }
        }
    }
}