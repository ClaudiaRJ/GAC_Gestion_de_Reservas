package com.easytable.app.AdaptersYClases;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class RawStringConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.get("text/plain; charset=UTF-8");

    public static RawStringConverterFactory create() {
        return new RawStringConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type == String.class) {
            return value -> {
                if (value.contentLength() == 0) {
                    return null;
                }
                try {
                    return value.string();
                } finally {
                    value.close();
                }
            };
        }
        return null;
    }
}