package io.github.chensheng.dddboot.openfeign.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openfeign")
public class OpenFeignProperties {
    public static final String PROP_BASE_PACKAGE = "openfeign.base-package";

    private String basePackage;

    private HttpClient httpclient = new HttpClient();

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public HttpClient getHttpclient() {
        return httpclient;
    }

    public void setHttpclient(HttpClient httpclient) {
        this.httpclient = httpclient;
    }

    public static class HttpClient {
        private int maxConnTotal = 200;

        private int maxConnPerRoute = 50;

        private boolean tcpNoDelay = true;

        private int socketTimeoutMillis = 3000;

        private int connectTimeoutMillis = 3000;

        private int connectionRequestTimeoutMillis = 2000;

        public int getMaxConnTotal() {
            return maxConnTotal;
        }

        public void setMaxConnTotal(int maxConnTotal) {
            this.maxConnTotal = maxConnTotal;
        }

        public int getMaxConnPerRoute() {
            return maxConnPerRoute;
        }

        public void setMaxConnPerRoute(int maxConnPerRoute) {
            this.maxConnPerRoute = maxConnPerRoute;
        }

        public boolean isTcpNoDelay() {
            return tcpNoDelay;
        }

        public void setTcpNoDelay(boolean tcpNoDelay) {
            this.tcpNoDelay = tcpNoDelay;
        }

        public int getSocketTimeoutMillis() {
            return socketTimeoutMillis;
        }

        public void setSocketTimeoutMillis(int socketTimeoutMillis) {
            this.socketTimeoutMillis = socketTimeoutMillis;
        }

        public int getConnectTimeoutMillis() {
            return connectTimeoutMillis;
        }

        public void setConnectTimeoutMillis(int connectTimeoutMillis) {
            this.connectTimeoutMillis = connectTimeoutMillis;
        }

        public int getConnectionRequestTimeoutMillis() {
            return connectionRequestTimeoutMillis;
        }

        public void setConnectionRequestTimeoutMillis(int connectionRequestTimeoutMillis) {
            this.connectionRequestTimeoutMillis = connectionRequestTimeoutMillis;
        }
    }
}
