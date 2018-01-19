package ua.in.quireg.foursquareapp.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import ua.in.quireg.foursquareapp.BuildConfig;
import ua.in.quireg.foursquareapp.FoursquareApplication;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 3:54 PM.
 * foursquareapp
 */

@Module
public class OkHttpModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(true);
        builder.cache(cache);
        builder.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("client_id", BuildConfig.CLIENT_ID)
                    .addQueryParameter("client_secret", BuildConfig.CLIENT_SECRET)
                    .addQueryParameter("v", "20170801")
                    .build();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);

        return builder.build();
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(FoursquareApplication application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }
}
