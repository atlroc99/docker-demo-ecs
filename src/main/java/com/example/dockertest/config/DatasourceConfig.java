package com.example.dockertest.config;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.example.dockertest.config.DSConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;

@Configuration
@Profile("dev")
public class DatasourceConfig {
    private final Logger LOGGER = LoggerFactory.getLogger(DSConfig.class);
    private final Environment environment;
    private final DSConfig dsConfig;
    String propertiesFile = "application.properties";

    @Value("${application.secretName}")
    private String secretName;

    public DevDatasourceConfig(Environment environment, DSConfig dsConfig) {
        this.environment = environment;
        this.dsConfig = dsConfig;
    }

    @Bean
    @Profile("dev")
    public DataSource postgresDatasource() throws Exception {
        String activeProfile = "";

        if (environment.getActiveProfiles()[0].length() != 0) {
            activeProfile = environment.getActiveProfiles()[0];
        }
        if (!activeProfile.equals("")) {
            propertiesFile = "application-" + activeProfile + ".properties";
        }

        JsonObject jsonObject = getSecret();
        if (jsonObject.size() == 0) {
            throw new Exception();
        }

        Class.forName(jsonObject.get("driverClassName").getAsString());
        String jdbcUrlPrefix = jsonObject.get("jdbcUrl").getAsString();
        String hostname = jsonObject.get("host").getAsString();
        String port = jsonObject.get("port").getAsString();
        String dbName = jsonObject.get("dbname").getAsString();
        String userName = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        String dbInstanceIdentifier = jsonObject.get("dbInstanceIdentifier").getAsString();
        String engine = jsonObject.get("engine").getAsString();

//        String dsUrl = "jdbc:" + jsonObject.get("engine").getAsString() + "://" + jsonObject.get("host").getAsString() + ":" + jsonObject.get("port").getAsString() + "/" + jsonObject.get("dbname").getAsString();
        // String jdbcUrl = "jdbc:postgresql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
        // String dsUrl = jdbcUrl + host + ":" + port + "/" + dbName + "?" + username;
        String jdbcUrl = jdbcUrlPrefix + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;

        LOGGER.info("DB STRING: " + jdbcUrl);
        DataSource dataSource = DataSourceBuilder.create()
                .url(jdbcUrl)
                .build();
        Connection connection = null;
        try{
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            if (connection == null) {
                LOGGER.info("Connection failed");
            }
            e.printStackTrace(System.err);
        }
        dataSource.getConnection();
        return dataSource;
    }

    private JsonObject getSecret() throws Exception {
        AWSSecretsManager secretsManagerClient = AWSSecretsManagerClientBuilder.standard()
                .withRegion(dsConfig.getRegion())
                .build();

        String secretName = dsConfig.getSecretName();
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);
        GetSecretValueResult secretValueResult = null;

        try {
            secretValueResult = secretsManagerClient.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw e;
        }

        if (secretValueResult == null) {
            return new JsonObject();
        }

        String secrets = "";
        if (secretValueResult.getSecretString() != null) {
            secrets = secretValueResult.getSecretString();
        } else {
            byte[] b = Base64.getDecoder().decode(secretValueResult.getSecretBinary()).array();
            secrets = new String(b);
        }

        JsonObject jsonObject = new Gson().fromJson(secrets, JsonObject.class);
        return jsonObject;
    }
}
