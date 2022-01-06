package com.ajayspace.cinema

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.ajayspace.models.MovieResult
import com.bumptech.glide.Glide

class GridAdapter(var activity: FragmentActivity?, var result: List<MovieResult>?) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var img: ImageView
    override fun getCount(): Int {
        return result!!.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        var convertView = convertView

        if (convertView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            convertView = LayoutInflater.from(activity).inflate(R.layout.img_item, parent, false);
        }

        img = convertView!!.findViewById(R.id.image_slider)
//        img.layoutParams.height = 600;
//        img.layoutParams.width = 350;
//        img.layoutParams.height = 600;

        Log.i("fx","img--->${result?.get(position)?.poster_path}")


        activity?.let { Glide.with(it).load(result?.get(position)?.poster_path).into(img) };

        img.setOnClickListener {
            var movieId = 0;


            if (result?.get(position)?.id != null) {
                movieId = result?.get(position)?.id!!
            }
            Toast.makeText(activity, "${result?.get(position)?.id}", Toast.LENGTH_LONG).show()
            it.findNavController()
                .navigate(MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment(movieId))
        }

        return convertView


    }

}
