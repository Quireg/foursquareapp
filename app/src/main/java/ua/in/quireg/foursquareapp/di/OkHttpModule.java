package ua.in.quireg.foursquareapp.di;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.cache.InternalCache;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;
import ua.in.quireg.foursquareapp.BuildConfig;
import ua.in.quireg.foursquareapp.FoursquareApplication;
import ua.in.quireg.foursquareapp.repositories.PersistentStorage;

/**
 * Created by Arcturus Mengsk on 1/18/2018, 3:54 PM.
 * foursquareapp
 */

@Module
public class OkHttpModule {

    private int networkRequestsCount = 0;

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
            Request.Builder requestBuilder = original.newBuilder().url(url);

            Request request = requestBuilder.build();


            return chain.proceed(request);
        });

        builder.addNetworkInterceptor(chain -> {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + 2419200)
                            .build();
                }
        );

        builder.addInterceptor(
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        );

        return builder.build();
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(FoursquareApplication application) {
        int cacheSize = 100 * 1024 * 1024; // 100 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }
}
