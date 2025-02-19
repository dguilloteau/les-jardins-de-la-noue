package org.dg.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import org.dg.errors.ShopsErrors;
import org.dg.errors.ShopsException;
import org.eclipse.microprofile.config.ConfigProvider;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.Getter;

@Getter
@Singleton
public class DBDriver implements java.sql.Driver {

    private final org.mariadb.jdbc.Driver mariadbDriver;
    private static Connection connection;

    private static final String DATABASE_URL = ConfigProvider.getConfig().getValue("quarkus.datasource.jdbc.url",
            String.class);
    private static final String DATABASE_USERNAME = ConfigProvider.getConfig().getValue("quarkus.datasource.username",
            String.class);
    private static final String DATABASE_PASSWORD = ConfigProvider.getConfig().getValue("quarkus.datasource.password",
            String.class);

    static {
        try {
            Log.debug("Register driver...");
            DriverManager.registerDriver(new DBDriver());
            getConnection();
        } catch (SQLException e) {
            throw new ShopsException(ShopsErrors.SQL_ERROR, "Could not register driver", e);
        }
    }

    @Inject
    public DBDriver() {
        this.mariadbDriver = new org.mariadb.jdbc.Driver();
    }

    public static Connection getConnection() {

        Log.debug("DATABASE_URL = " + DATABASE_URL);
        Log.debug("DATABASE_USERNAME = " + DATABASE_USERNAME);
        Log.debug("DATABASE_PASSWORD = " + DATABASE_PASSWORD);

        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            } catch (SQLException e) {
                throw new ShopsException(ShopsErrors.SQL_ERROR, "Unable to load connection", e);
            }
        }

        return connection;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        Log.debug("Connection...");
        return mariadbDriver.connect(url, info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return mariadbDriver.acceptsURL(url);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return mariadbDriver.getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return mariadbDriver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return mariadbDriver.getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return mariadbDriver.jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

}
