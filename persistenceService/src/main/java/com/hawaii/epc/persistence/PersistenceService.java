package com.hawaii.epc.persistence;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Stateless
@Slf4j
public class PersistenceService {

    PreparedStatement stmt = null;
    ResultSet rs = null;
    String description ="No description found.";
    //log.info("INFO HAWAII: "+description);
    String selectHawaii = "select * from hawaii.hawaii";
    public Connection getSQLiteConnection() throws SQLException, NamingException {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/SQLiteDS");
        return ds.getConnection();
    }

    public Connection getPostgreSQLConnection() throws SQLException, NamingException {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/PostgresDS");
        return ds.getConnection();
    }


    public void querySQLite() {
        try (Connection connection = getSQLiteConnection()) {
                stmt = connection.prepareStatement(selectHawaii);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    description = rs.getString("location");
                    log.info("INFO HAWAII: "+description);
                }

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }


    public void queryPostgreSQL() {
        try (Connection connection = getPostgreSQLConnection()) {

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
    }
}
