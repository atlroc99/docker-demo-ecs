package com.example.dockertest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Profile("localPg")
@Configuration
public class LocalDSConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(DSConfig.class);
    private final Environment environment;
    private final DSConfig dsConfig;

    public LocalPgDSConfig(Environment environment, DSConfig dsConfig) {
        this.environment = environment;
        this.dsConfig = dsConfig;
    }

    @Bean
    @Profile("localPg")
    public DataSource getLocalPgDB() {
        // String jdbcUrl = "jdbc:postgresql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
        String[] activeProfiles = environment.getActiveProfiles();
        String activeProfile = "";
        for (String ap : activeProfiles) {
            activeProfile = ap;
        }
        LOGGER.info("*** LOCAL-PG DATABASE... Active Profile: " + activeProfile);
        String pgDriverClassName = "org.postgresql.Driver";
        String jdbcURl = "jdbc:postgresql://localhost:5432/autobots_dev_db?user=postgres&password=password";
        DataSource dataSource = DataSourceBuilder.create().url(jdbcURl).build();
        Connection connection = null;
        boolean isClosed = true;
        try {
            connection = dataSource.getConnection();
            isClosed = connection.isClosed();
        } catch (SQLException e) {
            LOGGER.error("NOT ABLE TO CONNECT TO THE POSTGRES DB");
            e.printStackTrace(System.err);
        }
        if (connection == null) {
            LOGGER.error("NOT ABLE TO CONNECT TO THE POSTGRES DB");
            return null;
        }

        LOGGER.info("**** Connection successful *** connection is CLOSED: " + isClosed);


        return dataSource;
    }
}
