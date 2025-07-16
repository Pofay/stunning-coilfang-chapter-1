package org.pofay.jcip_example.chapter3.assignment;

public record AppConfigRecord (String dbUrl, int maxConnections, boolean cacheEnabled) {
}


