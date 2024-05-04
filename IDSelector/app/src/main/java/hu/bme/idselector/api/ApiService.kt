package hu.bme.idselector.api

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.gson.GsonBuilder
import hu.bme.idselector.data.LoginData
import hu.bme.idselector.data.NationalId
import hu.bme.idselector.data.Profile
import hu.bme.idselector.data.UserRegistration
import hu.bme.idselector.data.OtherId
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Retrofit interface for the API calls
 */
interface ApiService {

    @GET("/image/detection")
    fun detectNationalIdText(
        @Query("front") front: String?,
        @Query("back") back: String?
    ): Call<NationalId?>?

    @Multipart
    @POST("/image/corners")
    fun detectCorners(
        @Part image: MultipartBody.Part
    ): Call<List<List<Float>>>?

    @Multipart
    @POST("/image/crop")
    fun cropPicture(
        @Part image: MultipartBody.Part,
        @Part corners: MultipartBody.Part
    ): Call<String>?

    @GET("/image/clear")
    fun clearFolder(): Call<Any>?

    @POST("/auth/login")
    @Headers("No-Authentication: true")
    fun loginUser(@Body loginData: LoginData): Call<String?>

    @POST("/auth/register")
    @Headers("No-Authentication: true")
    fun registerUser(@Body registrationData: UserRegistration): Call<ResponseBody?>

    @GET("/auth/token")
    fun testToken(): Call<ResponseBody?>

    @GET("/user/profiles/list")
    fun getProfiles(): Call<List<Profile>?>?

    @GET("/user/profiles/{id}/national/list")
    fun getNationalIds(@Path("id") profileId: Int): Call<List<NationalId>?>

    @POST("/user/profiles/{id}/national/new")
    fun createNewNationalId(
        @Path("id") profileId: Int,
        @Body nationalId: NationalId
    ): Call<ResponseBody>?

    @POST("/user/profiles/{id}/other/new")
    fun createNewOtherId(
        @Path("id") profileId: Int,
        @Body otherId: OtherId
    ): Call<ResponseBody>?

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


        fun getNationalIdFront(userId: Int, nationalId: Int) =
            getNationalIdImage(userId, nationalId, "front")

        fun getNationalIdBack(userId: Int, nationalId: Int) =
            getNationalIdImage(userId, nationalId, "back")

        private fun getNationalIdImage(
            userId: Int,
            nationalId: Int,
            part: String
        ): GlideUrl? = getGlideURL("/user/profiles/$userId/national/$nationalId/$part")

        fun getImageUrl(imageId: String): GlideUrl? {
            return getGlideURL("image/download/$imageId")
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