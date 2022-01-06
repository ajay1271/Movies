package com.ajayspace.cinema

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajayspace.api.Constants
import com.ajayspace.api.ServiceBuilder
import com.ajayspace.api.TmdbEndpoints
import com.ajayspace.models.MovieDetailModel
import com.ajayspace.models.MovieResult
import com.ajayspace.models.PopularMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesViewModel : ViewModel() {

    var page = 1;
    var movieData = MutableLiveData<List<MovieResult>>()
    var searchResultData = MutableLiveData<List<MovieResult>>()
    var movieDetailsLiveData = MutableLiveData<MovieDetailModel>()
    var list = mutableListOf<MovieResult>()

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch {
            getMoviesData()
        }
    }

    fun getSearchResults(query:String) {
        viewModelScope.launch{
           getResults(query)
        }
    }
    fun getMovieDetails(id: Int)     {
        viewModelScope.launch {
            getMovieDetailsData(id)
        }
    }

    //get movie search results
    private suspend fun getResults(query: String) {
        val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val searchResultCall = request.searchMovies(Constants.API_KEY,query)
        Log.i("Search","${searchResultCall.request().url}")
        searchResultCall.enqueue(object : Callback<PopularMovies>{
            override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {
                if(response.isSuccessful){
                    Log.e("call","${response.body()}")
                    searchResultData.value = response.body()?.results
                }
            }
            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                Log.e("call","$t")
            }
        })
    }

    //get details of a particular movie with movie id
    private suspend fun getMovieDetailsData(movieId: Int) {
        val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val movieDetailsCall = request.getMovieDetails(movieId, Constants.API_KEY)
        movieDetailsCall.enqueue(object : Callback<MovieDetailModel> {
            override fun onResponse(
                call: Call<MovieDetailModel>,
                response: Response<MovieDetailModel>
            ) {
                if (response.isSuccessful) {
                    movieDetailsLiveData.value = response.body()
                }
            }
            override fun onFailure(call: Call<MovieDetailModel>, t: Throwable) {
                Log.e("Error","$t")
            }
        })
    }

    // get now playing movies
    private suspend fun getMoviesData() {
        val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val call = request.getMovies(Constants.API_KEY, page)
        call.enqueue(object : Callback<PopularMovies> {
            override fun onResponse(
                call: Call<PopularMovies>,
                response: Response<PopularMovies>
            ){
                if (response.isSuccessful) {
                    response.body()?.results?.let { list.addAll(it) }
                    movieData.value = list
                }
            }
            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                Log.e("Error","$t")
            }
        })
    }

    fun getRtRdEtc(it: MovieDetailModel): CharSequence? {
        var rating = "Rating :NA"
        if (it.adult == true) {
            rating = "Rating :R"
        }
        return rating + " |  Duration: " + it.runtime + "  min |   " + it.releaseDate
    }

}