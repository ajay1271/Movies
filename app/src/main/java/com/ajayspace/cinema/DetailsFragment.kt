package com.ajayspace.cinema

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ajayspace.cinema.databinding.FragmentDetailsBinding
import com.ajayspace.models.MovieDetailModel
import com.bumptech.glide.Glide


import com.facebook.shimmer.ShimmerFrameLayout





class DetailsFragment : Fragment() {

    private val model: MoviesViewModel by viewModels()

    private lateinit var fragmentDetailsBinding: FragmentDetailsBinding

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    private var baseBackdrop: String = "https://image.tmdb.org/t/p/original/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        fragmentDetailsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_details, container, false
        )

        fragmentDetailsBinding.waiting.visibility = VISIBLE
        fragmentDetailsBinding.detailsData.visibility = GONE

        attachObservers()



        model.getMovieDetails(DetailsFragmentArgs.fromBundle(requireArguments()).movieId)

        return fragmentDetailsBinding.root
    }

    private fun attachObservers() {
        Log.i("detailsFragment","attachObservers-->")
        model.movieDetailsLiveData.observe(viewLifecycleOwner, Observer { it ->

            //var len1:  Int = str ?.length ?: -1

            var  pop = it.voteAverage ?:5.0
            var  rating = pop.times(5.0)
            var total = rating.div(10.0)

           if(it!=null){

               fragmentDetailsBinding.categories.text = it.genres[0].name.toString()

               fragmentDetailsBinding.detailsData.visibility = VISIBLE
               fragmentDetailsBinding.waiting.visibility = GONE
               Log.i("detailsFragment","--->")
               fragmentDetailsBinding.movieDescription.text = it.overview
               fragmentDetailsBinding.movieTitle.text = it.title.toString()
               fragmentDetailsBinding.movieRating.rating = total.toFloat()
               fragmentDetailsBinding.ratingsText.text = total.toString()
               var img = fragmentDetailsBinding.poster
               activity?.let { it1 -> Glide.with(it1).load(baseBackdrop+it.posterPath).into(img) }

               fragmentDetailsBinding.movieDetailsRtRdRatingEtc.text = getRtRdEtc(it)
           };

            else{
               fragmentDetailsBinding.waiting.text = getString(R.string.no_response)
           }

        }

        )
    }

    private fun getRtRdEtc(it: MovieDetailModel): CharSequence? {

        var rating = "Rating :NA"

        if(it.adult==true){
            rating = "Rating :R"
        }

        return rating+" |  Duration: "+it.runtime +"  min |   "+ it.releaseDate

    }


}