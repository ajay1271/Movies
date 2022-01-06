package com.ajayspace.cinema

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajayspace.api.ServiceBuilder
import com.ajayspace.api.TmdbEndpoints
import com.ajayspace.models.MovieDetailModel
import com.ajayspace.models.MovieResult
import com.ajayspace.models.PopularMovies
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesViewModel : ViewModel() {

    private var baseBackdropBase: String = "https://image.tmdb.org/t/p/original/"
    private var posterBase: String = "https://image.tmdb.org/t/p/w342/"

    var page = 1;
    var list = mutableListOf<MovieResult>()
    var movieData = MutableLiveData<List<MovieResult>>()
    var updatedMovieData = MutableLiveData<List<MovieResult>>()
    var movieDetailsLiveData = MutableLiveData<MovieDetailModel>()

    var movieDetails: MovieDetailModel? = null

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch {
            getMoviesData()
        }
    }

    fun getMovieDetails(id: Int) {
        Log.i("detailsFragment", "getMovieDetails-->")
        viewModelScope.launch {
            getMovieDetailsData(id)
        }
    }


    private suspend fun getMovieDetailsData(movieId: Int) {

        Log.i("detailsFragment", "getMovieDetailsData-->")

        var movieDetailModel: MovieDetailModel? = null
        val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)


        val movieDetailsCall = request.getMovieDetails(movieId, "2bfc2845a8e711f212828a7f8d23d3a7")

        var url = movieDetailsCall.request().url.toString()

        Log.i("detailsFragment", "getMovieDetailsData-->${movieDetailsCall.request().url}")

        movieDetailsCall.enqueue(object : Callback<MovieDetailModel> {
            override fun onResponse(
                call: Call<MovieDetailModel>,
                response: Response<MovieDetailModel>
            ) {
                if (response.isSuccessful) {
                    Log.i("detailsFragment", "getMovieDetailsData.isSuccessful-->")
                    // Log.i("Retrofit", response.body().toString())
                    movieDetailModel = response.body()
                    updateMovieDetailsUI(movieDetailModel)
                }
            }

            override fun onFailure(call: Call<MovieDetailModel>, t: Throwable) {
                Log.i("detailsFragment", "getMovieDetailsData.failed--> $t")
                // Log.i("Retrofit", t.toString())
            }

        }


        )


    }

    private suspend fun getMoviesData() {
        Log.i("Retrofit", "getMoviesData-->")

        val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val call = request.getMovies("2bfc2845a8e711f212828a7f8d23d3a7", page)

        Log.i("Retrofit", "Retrofit url-->${call.request().url.toString()}")

        call.enqueue(object : Callback<PopularMovies> {
            override fun onResponse(
                call: Call<PopularMovies>,
                response: Response<PopularMovies>
            ) {
                if (response.isSuccessful) {
                    Log.i("Retrofit", "success")
                    response.body()?.results?.forEach {
                        it.backdrop_path = baseBackdropBase + it.backdrop_path
                        it.poster_path = posterBase + it.poster_path
                        list.add(it)
                    }
                    updateDashboardUI(list)
                }
            }

            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                Log.i("Retrofit", "Failed $t")
            }

        })
    }

    fun updateMovieDetailsUI(movieDetails: MovieDetailModel?) {
        Log.i("Retrofit", "updateMovieDetailsUI -->")
        movieDetailsLiveData.value = movieDetails

    }

    fun updateDashboardUI(list: List<MovieResult>) {
        Log.i("pagination", "This is after network call observer will be triggered now-->")
        Log.i("pagination", "list.size--> ${list.size}")
        movieData.value = list

    }

}