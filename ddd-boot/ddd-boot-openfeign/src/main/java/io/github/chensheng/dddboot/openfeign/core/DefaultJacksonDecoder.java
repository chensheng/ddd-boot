package io.github.chensheng.dddboot.openfeign.core;

import io.github.chensheng.dddboot.tools.mapper.JsonMapper;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.apache.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

public class DefaultJacksonDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if (response.status() == HttpStatus.SC_NOT_FOUND) {
            return Util.emptyValueOf(type);
        }

        if (response.body() == null) {
            return null;
        }

        Reader reader = response.body().asReader();
        if (!reader.markSupported()) {
            reader = new BufferedReader(reader);
        }

        reader.mark(1);
        if (reader.read() == -1) {
            return null;
        }

        reader.reset();
        return JsonMapper.nonNullMapper().fromJson(reader, type);
    }
}
