package com.exasol.adapter.dialects;


import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class IntegrationTestConfig {

    Map config;

    public IntegrationTestConfig() throws FileNotFoundException {
        this(getMandatorySystemProperty("integrationtest.configfile"));
    }

    public IntegrationTestConfig(String configFile) throws FileNotFoundException {
        try {
            config = loadConfig(configFile);
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("The specified integration test config file could not be found: " + configFile);
        } catch (Exception ex) {
            throw new RuntimeException("The specified integration test config file could not be parsed: " + configFile, ex);
        }
    }

    public String getJdbcAdapterPath() {
        return getProperty("general", "jdbcAdapterPath");
    }

    public String getScpTargetPath() {
        return getProperty("general", "scpTargetPath");
    }

    public boolean exasolTestsRequested() {
        return getProperty("exasol", "runIntegrationTests", false);
    }

    public String getExasolAddress() {
        return getProperty("exasol", "address");
    }

    public String getExasolUser() {
        return getProperty("exasol", "user");
    }

    public String getExasolPassword() {
        return getProperty("exasol", "password");
    }

    public boolean isDebugOn() {
        return getProperty("general", "debug", false);
    }

    public String debugAddress() {
        return getProperty("general", "debugAddress", "");
    }

    public boolean impalaTestsRequested() {
        return getProperty("impala", "runIntegrationTests", false);
    }

    public String getImpalaJdbcConnectionString() {
        return getProperty("impala", "connectionString");
    }

    public String getImpalaJdbcPrefixPath() {
        return getProperty("impala", "jdbcDriverPath");
    }

    public List<String> getImpalaJdbcJars() {
        return getProperty("impala", "jdbcDriverJars");
    }

    public boolean kerberosTestsRequested() {
        return getProperty("kerberos", "runIntegrationTests", false);
    }

    public String getKerberosJdbcConnectionString() {
        return getProperty("kerberos", "connectionString");
    }

    public String getKerberosJdbcPrefixPath() {
        return getProperty("kerberos", "jdbcDriverPath");
    }

    public String getKerberosUser() {
        return getProperty("kerberos", "user");
    }

    public String getKerberosPassword() {
        return getProperty("kerberos", "password");
    }

    public List<String> getKerberosJdbcJars() {
        return getProperty("kerberos", "jdbcDriverJars");
    }

    public boolean oracleTestsRequested() {
        return getProperty("oracle", "runIntegrationTests", false);
    }

    public String getOracleJdbcDriverPath() {
        return getProperty("oracle", "jdbcDriverPath");
    }

    public String getOracleJdbcConnectionString() {
        return getProperty("oracle", "connectionString");
    }

    public String getOracleUser() {
        return getProperty("oracle", "user");
    }

    public String getOraclePassword() {
        return getProperty("oracle", "password");
    }

    public boolean genericTestsRequested() {
        return getProperty("generic", "runIntegrationTests", false);
    }

    public String getGenericJdbcDriverPath() {
        return getProperty("generic", "jdbcDriverPath");
    }

    public String getGenericJdbcConnectionString() {
        return getProperty("generic", "connectionString");
    }

    public String getGenericUser() {
        return getProperty("generic", "user");
    }

    public String getGenericPassword() {
        return getProperty("generic", "password");
    }

    private Map loadConfig(String configFile) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        File file = new File(configFile);
        InputStream inputStream = null;
        inputStream = new FileInputStream(file);
        return (Map) yaml.load(inputStream);
    }

    private <T> T getProperty(String section, String key, T defaultValue) {
        try {
            return getProperty(section, key);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    private <T> T getProperty(String section, String key) {
        if (!config.containsKey(section)) {
            throw new RuntimeException("Integration test config file has no section '" + section + "'");
        }
        Map sectionMap = (Map)config.get(section);
        if (!sectionMap.containsKey(key)) {
            throw new RuntimeException("Integration test config file has no key '" + key + "' in section '" + section + "'");
        }
        return (T)sectionMap.get(key);
    }

    private static String getMandatorySystemProperty(String propertyName) {
        String value = System.getProperty(propertyName);
        if (value == null) {
            throw new RuntimeException("Integration tests requires system property '" + propertyName + "' to be set.");
        }
        return value;
    }
}
