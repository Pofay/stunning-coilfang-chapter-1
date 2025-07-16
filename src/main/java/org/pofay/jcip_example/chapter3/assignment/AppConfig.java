package org.pofay.jcip_example.chapter3.assignment;

public class AppConfig {
    private String dbUrl;
    private int maxConnections;
    private boolean cacheEnabled;

    public AppConfig(String dbUrl, int maxConnections, boolean cacheEnabled) {
        this.dbUrl = dbUrl;
        this.maxConnections = maxConnections;
        this.cacheEnabled = cacheEnabled;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }
}
