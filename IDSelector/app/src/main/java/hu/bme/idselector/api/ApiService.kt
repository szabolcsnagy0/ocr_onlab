package hu.android.qtyadoki.api

import android.media.Image
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.gson.GsonBuilder
import hu.bme.idselector.api.ServiceInterceptor
import hu.bme.idselector.api.TokenManager
import hu.bme.idselector.data.LoginData
import hu.bme.idselector.data.Person
import hu.bme.idselector.data.UserRegistration
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Retrofit interface for the API calls
 */
interface ApiService {

    @GET("/upload/text")
    fun detectText(
        @Query("front") front: String?,
        @Query("back") back: String?
    ): Call<Person?>

    @Multipart
    @POST("/upload/corners")
    fun detectCorners(
        @Part image: MultipartBody.Part
    ): Call<List<List<Float>>>?

    @Multipart
    @POST("/upload/crop")
    fun cropPicture(
        @Part image: MultipartBody.Part,
        @Part corners: MultipartBody.Part
    ): Call<String>?

    @POST("/auth/login")
    @Headers("No-Authentication: true")
    fun loginUser(@Body loginData: LoginData): Call<String?>

    @POST("/auth/register")
    @Headers("No-Authentication: true")
    fun registerUser(@Body registrationData: UserRegistration): Call<ResponseBody?>

    @GET("/auth/token")
    fun testToken(): Call<ResponseBody?>

    companion object {
        var api: ApiService? = null
        var tokenManager: TokenManager? = null
        private const val BASE_URL = "http://192.168.0.25:80/"

        /**
         * Get the singleton instance of the ApiService
         */
        fun getInstance(): ApiService {
            if (api == null) {
                val client = OkHttpClient.Builder()
                    .callTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .addInterceptor(ServiceInterceptor(tokenManager))
                    .build()
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                api = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build().create(ApiService::class.java)
            }
            return api!!
        }

        fun getImageUrl(imageId: String): GlideUrl? {
            return getGlideURL("download/$imageId")
        }

        /**
         * Get the GlideUrl for the given endpoint
         */
        private fun getGlideURL(endPoint: String): GlideUrl? {
            if (tokenManager == null) return null
            val token = tokenManager!!.fetchAuthToken() ?: return null
            val url = "$BASE_URL$endPoint"

            return GlideUrl(
                url,
                LazyHeaders.Builder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            )
        }
    }
}