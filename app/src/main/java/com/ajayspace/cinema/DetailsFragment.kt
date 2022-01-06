package com.ajayspace.cinema


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ajayspace.api.Constants
import com.ajayspace.cinema.databinding.FragmentDetailsBinding
import com.ajayspace.models.MovieDetailModel
import com.bumptech.glide.Glide


class DetailsFragment : Fragment() {

    private val model: MoviesViewModel by viewModels()
    private lateinit var fragmentDetailsBinding: FragmentDetailsBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //data binding is used
        fragmentDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

        //Initial page until callback from viewmodel
        fragmentDetailsBinding.waiting.visibility = VISIBLE
        fragmentDetailsBinding.detailsData.visibility = GONE

        //sending movie id to getMovieDetails method in viewmodel
        // which makes a async call to API and updates the live data which triggered the observer here
        model.getMovieDetails(DetailsFragmentArgs.fromBundle(requireArguments()).movieId)
        attachObservers()
        return fragmentDetailsBinding.root
    }

    private fun attachObservers() {

        //Observe change in movieDetailLiveData to trigger and assign values

        model.movieDetailsLiveData.observe(viewLifecycleOwner, Observer { it ->
            val total = (it.voteAverage ?: 5.0)*5.0
            if (it != null) {
                fragmentDetailsBinding.categories.text = it.genres[0].name.toString()
                fragmentDetailsBinding.detailsData.visibility = VISIBLE
                fragmentDetailsBinding.waiting.visibility = GONE
                fragmentDetailsBinding.movieDescription.text = it.overview
                fragmentDetailsBinding.movieTitle.text = it.title.toString()
                fragmentDetailsBinding.movieRating.rating = total.div(10.0).toFloat()
                fragmentDetailsBinding.ratingsText.text = total.div(10.0).toString()
                val img = fragmentDetailsBinding.poster
                activity?.let { it1 ->
                    Glide.with(it1).load(Constants.POSTER_BASE_URL + it.posterPath).into(img)
                }
                fragmentDetailsBinding.movieDetailsRtRdRatingEtc.text = model.getRtRdEtc(it)
            };
            else {
                fragmentDetailsBinding.waiting.text = getString(R.string.no_response)
            }
        })
    }
}