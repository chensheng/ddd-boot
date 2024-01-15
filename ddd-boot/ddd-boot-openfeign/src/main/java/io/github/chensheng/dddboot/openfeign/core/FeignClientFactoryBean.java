package io.github.chensheng.dddboot.openfeign.core;

import io.github.chensheng.dddboot.tools.collection.CollectionUtil;
import io.github.chensheng.dddboot.tools.reflect.ReflectionUtil;
import feign.*;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.FactoryBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class FeignClientFactoryBean implements FactoryBean<Object> {
    private static final ConcurrentHashMap<Class<? extends Encoder>, Encoder> encoderCache = new ConcurrentHashMap<Class<? extends Encoder>, Encoder>();

    private static final ConcurrentHashMap<Class<? extends Decoder>, Decoder> decoderCache = new ConcurrentHashMap<Class<? extends Decoder>, Decoder>();

    private static final ConcurrentHashMap<Class<? extends ErrorDecoder>, ErrorDecoder> errorDecoderCache = new ConcurrentHashMap<Class<? extends ErrorDecoder>, ErrorDecoder>();

    private static final ConcurrentHashMap<Class<? extends RequestInterceptor>, RequestInterceptor> interceptorCache = new ConcurrentHashMap<Class<? extends RequestInterceptor>, RequestInterceptor>();

    private static final ConcurrentHashMap<Class<? extends Retryer>, Retryer> retryerCache = new ConcurrentHashMap<Class<? extends Retryer>, Retryer>();

    private static final ConcurrentHashMap<Class<? extends Contract>, Contract> contractCache = new ConcurrentHashMap<Class<? extends Contract>, Contract>();

    private Class<?> type;

    private String url;

    private Class<? extends Encoder> encoderType;

    private Class<? extends Decoder> decoderType;

    private Class<? extends ErrorDecoder> errorDecoderType;

    private Class<? extends RequestInterceptor>[] interceptorTypes;

    private Class<? extends Retryer>[] retryerTypes;

    private Class<? extends Contract>[] contractTypes;

    private Client client;

    @Override
    public Object getObject() throws Exception {
        Feign.Builder builder = Feign.builder();

        Encoder encoder = getOrCreateEncoder(encoderType);
        if (encoder != null) {
            builder.encoder(encoder);
        }

        Decoder decoder = getOrCreateDecoder(decoderType);
        if (decoder != null) {
            builder.decoder(decoder);
        }

        ErrorDecoder errorDecoder = getOrCreateErrorDecoder(errorDecoderType);
        if(errorDecoder != null) {
            builder.errorDecoder(errorDecoder);
        }

        List<RequestInterceptor> interceptors = getOrCreateInterceptors(interceptorTypes);
        if (CollectionUtil.isNotEmpty(interceptors)) {
            builder.requestInterceptors(interceptors);
        }

        Retryer retryer = getOrCreateRetryer(retryerTypes);
        if (retryer != null) {
            builder.retryer(retryer);
        }

        Contract contract = getOrCreateContract(contractTypes);
        if (contract != null) {
            builder.contract(contract);
        }

        return builder.client(client).target(type, url);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    private Encoder getOrCreateEncoder(Class<? extends Encoder> encoderType) {
        return getOrCreate(encoderCache, encoderType);
    }

    private Decoder getOrCreateDecoder(Class<? extends Decoder> decoderType) {
        return getOrCreate(decoderCache, decoderType);
    }

    private ErrorDecoder getOrCreateErrorDecoder(Class<? extends ErrorDecoder> errorDecoderType) {
        if(errorDecoderType == null || errorDecoderType == ErrorDecoder.class) {
            return null;
        }
        return getOrCreate(errorDecoderCache, errorDecoderType);
    }

    private List<RequestInterceptor> getOrCreateInterceptors(Class<? extends RequestInterceptor>[] interceptorTypes) {
        if (interceptorTypes == null || interceptorTypes.length == 0) {
            return null;
        }

        List<RequestInterceptor> interceptors = new ArrayList<RequestInterceptor>();
        for (Class<? extends RequestInterceptor> interceptorType : interceptorTypes) {
            RequestInterceptor interceptor = getOrCreate(interceptorCache, interceptorType);
            interceptors.add(interceptor);
        }
        return interceptors;
    }

    private Retryer getOrCreateRetryer(Class<? extends Retryer>[] retryerTypes) {
        if (retryerTypes == null || retryerTypes.length == 0) {
            return null;
        }

        return getOrCreate(retryerCache, retryerTypes[0]);
    }

    private Contract getOrCreateContract(Class<? extends Contract>[] contractTypes) {
        if (contractTypes == null || contractTypes.length == 0) {
            return null;
        }

        return getOrCreate(contractCache, contractTypes[0]);
    }

    private <T> T getOrCreate(ConcurrentHashMap<Class<? extends T>, T> cache, Class<? extends T> type) {
        if (cache == null || type == null) {
            return null;
        }

        T cachedObject = cache.get(type);
        if (cachedObject != null) {
            return cachedObject;
        }

        return cache.computeIfAbsent(type, (aClass) -> ReflectionUtil.invokeConstructor(aClass));
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Class<? extends Encoder> getEncoderType() {
        return encoderType;
    }

    public void setEncoderType(Class<? extends Encoder> encoderType) {
        this.encoderType = encoderType;
    }

    public Class<? extends Decoder> getDecoderType() {
        return decoderType;
    }

    public void setDecoderType(Class<? extends Decoder> decoderType) {
        this.decoderType = decoderType;
    }

    public Class<? extends ErrorDecoder> getErrorDecoderType() {
        return errorDecoderType;
    }

    public void setErrorDecoderType(Class<? extends ErrorDecoder> errorDecoderType) {
        this.errorDecoderType = errorDecoderType;
    }

    public Class<? extends RequestInterceptor>[] getInterceptorTypes() {
        return interceptorTypes;
    }

    public void setInterceptorTypes(Class<? extends RequestInterceptor>[] interceptorTypes) {
        this.interceptorTypes = interceptorTypes;
    }

    public Class<? extends Retryer>[] getRetryerTypes() {
        return retryerTypes;
    }

    public void setRetryerTypes(Class<? extends Retryer>[] retryerTypes) {
        this.retryerTypes = retryerTypes;
    }

    public Class<? extends Contract>[] getContractTypes() {
        return contractTypes;
    }

    public void setContractTypes(Class<? extends Contract>[] contractTypes) {
        this.contractTypes = contractTypes;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
