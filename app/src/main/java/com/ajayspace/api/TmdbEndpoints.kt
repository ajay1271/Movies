package com.ajayspace.api



import com.ajayspace.models.MovieDetailModel
import com.ajayspace.models.PopularMovies
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbEndpoints {

    @GET("/3/movie/now_playing")
    fun getMovies(@Query("api_key") key: String,@Query("page") page:Int=1): Call<PopularMovies>

    @GET("/3/movie/{movie_id}")
    fun getMovieDetails( @Path("movie_id") id: Int, @Query("api_key") key: String): Call<MovieDetailModel>



}

object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}