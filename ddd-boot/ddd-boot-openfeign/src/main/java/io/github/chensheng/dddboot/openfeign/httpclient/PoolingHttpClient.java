package io.github.chensheng.dddboot.openfeign.httpclient;

import io.github.chensheng.dddboot.openfeign.config.OpenFeignProperties;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class PoolingHttpClient {
    private static final Logger logger = LoggerFactory.getLogger(PoolingHttpClient.class);

    private static final String[] SUPPORTED_PROTOCOLS = new String[] {"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"};

    private static final TrustStrategy TRUST_ALL_STRATEGY = new TrustStrategy() {
        @Override
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }
    };

    private OpenFeignProperties.HttpClient config;

    private CloseableHttpClient client;

    public PoolingHttpClient(OpenFeignProperties properties) {
        Assert.notNull(properties, "[openfeign] must not be null");
        Assert.notNull(properties.getHttpclient(), "[openfeign.httpclient] must not be null");
        this.config = properties.getHttpclient();
        this.initClient();
    }

    public CloseableHttpClient get() {
        return client;
    }

    private void initClient() {
        Registry<ConnectionSocketFactory> registry = createRegistry();
        PoolingHttpClientConnectionManager poolingConnMgr = new PoolingHttpClientConnectionManager(registry);
        poolingConnMgr.setMaxTotal(config.getMaxConnTotal());
        poolingConnMgr.setDefaultMaxPerRoute(config.getMaxConnPerRoute());

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(config.getSocketTimeoutMillis())
                .setTcpNoDelay(true)
                .build();

        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setSocketTimeout(config.getSocketTimeoutMillis())
                .setConnectTimeout(config.getConnectTimeoutMillis())
                .setConnectionRequestTimeout(config.getConnectionRequestTimeoutMillis())
                .build();

        poolingConnMgr.setDefaultSocketConfig(socketConfig);

        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setDefaultSocketConfig(socketConfig)
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingConnMgr);
        client = httpClientBuilder.build();
    }

    private Registry<ConnectionSocketFactory> createRegistry() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, TRUST_ALL_STRATEGY).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, SUPPORTED_PROTOCOLS, null, NoopHostnameVerifier.INSTANCE);
            return RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslConnectionSocketFactory)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Fail to create SSL connection socket", e);
        } catch (KeyStoreException e) {
            logger.error("Fail to create SSL connection socket", e);
        } catch (KeyManagementException e) {
            logger.error("Fail to create SSL connection socket", e);
        }
        return null;
    }
}
