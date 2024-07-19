package ru.evgenyfedotov.weather

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// private const val TOKEN = "439d4b804bc8187953eb36d2a8c26a02"
private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val OKHTTP_CONNECT_TIMEOUT = 10L
private const val OKHTTP_WRITE_TIMEOUT = 30L
private const val OKHTTP_READ_TIMEOUT = 30L

class App: Application() {

    lateinit var retrofit: Retrofit

    override fun onCreate() {
        super.onCreate()

        retrofit = createRetrofit()
    }

    private fun createRetrofit(): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createHttpClient())
            .build()
    }

    private fun createHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain
                    .request().run {
                        val url = url
                            .newBuilder()
                            .addQueryParameter("appid", TOKEN)
                            .build()

                        newBuilder()
                            .url(url)
                            .build()
                    }
                )
            }

            .connectTimeout(OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(OKHTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                        addInterceptor(this)
                    }
                }
            }
            .build()
    }
}