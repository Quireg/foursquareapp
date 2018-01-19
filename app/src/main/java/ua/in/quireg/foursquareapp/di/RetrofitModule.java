package ua.in.quireg.foursquareapp.di;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 6:53 PM.
 * foursquareapp
 */

@Module
public class RetrofitModule {

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Retrofit.Builder builder) {
        return builder.baseUrl("https://api.foursquare.com/v2/").build();
    }

    @Provides
    @Singleton
    public Retrofit.Builder provideRetrofitBuilder(Converter.Factory converterFactory, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(converterFactory)
                .client(okHttpClient);
    }

    @Provides
    @Singleton
    public Converter.Factory provideConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
//                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
//                .setFieldNamingStrategy(new CustomFieldNamingPolicy())
//                .setPrettyPrinting()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                .serializeNulls()
                .create();
    }

    private static class CustomFieldNamingPolicy implements FieldNamingStrategy {

        @Override
        public String translateName(Field field) {
            String name = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES.translateName(field);
            name = name.substring(2, name.length()).toLowerCase();
            return name;
        }
    }

}
