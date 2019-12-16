package co.artechinfo.shekhar.networking

import android.content.Context
import co.artechinfo.shekhar.model.FactsWrapper
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface RestApiService {

    @GET(factsEndPoint)
    fun fetchFactsAsync(): Deferred<FactsWrapper>

    companion object {

        const val baseURL: String = "https://dl.dropboxusercontent.com/"
        const val factsEndPoint: String = "s/2iodh4vg0eortkl/facts.json"

        fun createService(mContext: Context): RestApiService {
            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(NetworkConnectionInterceptor(mContext))
                .build()

            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(RestApiService::class.java)
        }
    }
}