package com.ajayspace.cinema

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.ajayspace.cinema.databinding.FragmentMoviesBinding
import com.ajayspace.models.MovieResult


class MoviesFragment : Fragment() {

    private lateinit var fragmentMoviesBinding: FragmentMoviesBinding

    private lateinit var imageItemList: ArrayList<SliderItem>
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var sliderRun: Runnable
    private val model: MoviesViewModel by viewModels()
    private lateinit var gridView: ExpandableHeightGridView
    private lateinit var viewPager: ViewPager2


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentMoviesBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_movies, container, false
        )

        gridView = fragmentMoviesBinding.gridView


        attachListener()


        attachObservers()

        return fragmentMoviesBinding.root
    }


    private fun attachListener() {
        fragmentMoviesBinding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > scrollX) {

                val rect = Rect()

                //Pagination Logic
                val diff: Int =
                    fragmentMoviesBinding.scrollView.getChildAt(fragmentMoviesBinding.scrollView.getChildCount() - 1).bottom -
                            (fragmentMoviesBinding.scrollView.getHeight() + fragmentMoviesBinding.scrollView
                                .getScrollY())

              //  Log.i("pagination", "diff-->$diff")

                if (diff == 0) {
                    Log.i("pagination", "UI update called-->")
                    Log.i("pagination", "list size-->${model.list.size}")
                  //  Toast.makeText(context, "Loading more Data", Toast.LENGTH_SHORT).show()
                    //model.page += 1
                    //model.getMovies()
                    Log.i("pagination", "end-->")
                }


                if (!fragmentMoviesBinding.nowShowingTitle.getGlobalVisibleRect(rect)) {
                    (activity as MainActivity).supportActionBar?.title = "Now Showing"
                }
                if (fragmentMoviesBinding.nowShowingTitle.getGlobalVisibleRect(rect)) {
                    (activity as MainActivity).supportActionBar?.title = "Movies"
                }

                Log.i("ViewPager", "viewPager visibility ${viewPager.getGlobalVisibleRect(rect)}")
            } else {
                Toast.makeText(context, "Scrolling Up", Toast.LENGTH_SHORT).show()
                Log.i(
                    "ViewPager",
                    "viewPager visibility ${fragmentMoviesBinding.viewPagerImgSlider.isFocused}"
                )
            }
        })
    }

    private fun attachObservers() {
        model.movieData.observe(viewLifecycleOwner, Observer { it ->
            Log.i("pagination", "Observer triggered-->")
            //test(it)
            var list = ArrayList<String>()

            it.forEach {
                list.add(it.backdrop_path)
            }

            Log.i("Retrofit", "updating UIin fragment triggered()-->")
            Log.i("Retrofit", it.toString())
            updateViewPager(it)
            updateGridView(it)
        }

        )
    }


    private fun updateGridView(it: List<MovieResult>?) {

        val mainAdapter = GridAdapter(activity, it)
        gridView.adapter = mainAdapter
        gridView.isExpanded = true;
        gridView.isVerticalScrollBarEnabled = false

    }

    private fun updateViewPager(imagesList: List<MovieResult>) {
        Log.i("Retrofit", "updateViewPager triggered()-->")
        imageItemList = ArrayList()
        viewPager = fragmentMoviesBinding.viewPagerImgSlider
        viewPager.visibility = GONE
        viewPager.currentItem = viewPager.currentItem + 1
        sliderAdapter = SliderAdapter(viewPager, imagesList, activity?.baseContext)

        sliderRun = Runnable {
            viewPager.currentItem = viewPager.currentItem + 1
        }

        viewPager.visibility = VISIBLE
        viewPager.adapter = sliderAdapter

        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()

        //compositePageTransformer.addTransformer(MarginPageTransformer(0))
        compositePageTransformer.addTransformer { page, position ->

            //
            val r: Float = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.15f

        }
        viewPager.setPageTransformer(compositePageTransformer)


//        viewPager.registerOnPageChangeCallback(
//            object :ViewPager2.OnPageChangeCallback(){
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    sliderHandle.removeCallbacks(sliderRun)
//                    sliderHandle.postDelayed(sliderRun,2000)
//                }
//            }
//        )


    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()

    }


}