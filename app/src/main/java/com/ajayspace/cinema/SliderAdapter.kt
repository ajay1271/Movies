package com.ajayspace.cinema

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ajayspace.models.MovieResult
import com.bumptech.glide.Glide

class SliderAdapter(val viewPager2: ViewPager2, private val images: List<MovieResult>, private var context:Context?) :RecyclerView.Adapter<SliderAdapter.SliderViewHolder>(){


    //
    class SliderViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView){
        var img = itemView.findViewById<ImageView>(R.id.image_slider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.img_pager, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        var img  = holder.img

      //  img.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

       // Log.i("Retrofit",images[position])
        context?.let { Glide.with(it).load(images[position].backdrop_path).into(img) };

        img.setOnClickListener {
            var movieId = 0;

            Log.i("detailsFragment", "itwm with id-->${images[position].id} Clicked")

            movieId = images[position].id

          //  Toast.makeText(context, "${result?.get(position)?.id}", Toast.LENGTH_LONG).show()
            it.findNavController()
                .navigate(MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment(movieId))
        }

    }



    override fun getItemCount(): Int {
      return images.size
    }

}