package com.ajayspace.cinema

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //to enable search
        setHasOptionsMenu(true)

        //Data Binding
        fragmentMoviesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)

        gridView = fragmentMoviesBinding.gridView
        pagination()
        attachObservers()
        return fragmentMoviesBinding.root
    }


    private fun pagination() {
        /*
        * Pagination Logic when scrolled all the way down diff will be zero. Update the adapter with new data from api
        */
        fragmentMoviesBinding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (scrollY > scrollX) {
                val rect = Rect()
                //Pagination Logic
                //checking if scrollview reached its last child element
                val diff: Int = fragmentMoviesBinding.scrollView.getChildAt(fragmentMoviesBinding.scrollView.getChildCount() - 1).bottom -
                        (fragmentMoviesBinding.scrollView.getHeight() + fragmentMoviesBinding.scrollView.getScrollY())

                //this means scroll view has reached the last element in scrollview
                if (diff == 0) {
                    Toast.makeText(context, "Loading more Data", Toast.LENGTH_SHORT).show()
                    //update the page number to be queried for new data
                    model.page += 1
                    //this will call the api and updates the live data which triggers the observer
                    model.getMovies()
                }
                //App Bar title will be changed based on the Now Showing text visibility on screen when scrolled
                if (!fragmentMoviesBinding.nowShowingTitle.getGlobalVisibleRect(rect)) {
                    (activity as MainActivity).supportActionBar?.title = "Now Showing"
                }
                if (fragmentMoviesBinding.nowShowingTitle.getGlobalVisibleRect(rect)) {
                    (activity as MainActivity).supportActionBar?.title = "Movies"
                }

            }
        })
    }

    private fun attachObservers() {
        //Live data (movieData) change in value triggers this function
        model.movieData.observe(viewLifecycleOwner, Observer { it ->
            //Updating viewpaget first then grid view
            updateViewPager(it)
            //update the gridview
            updateGridView(it)
        })
    }

    private fun updateGridView(it: List<MovieResult>?) {

        //Custom Grid view has been used to drag other elements in view when gridview is scrolled (using nested scrollview)
        val mainAdapter = GridAdapter(activity, it)
        gridView.adapter = mainAdapter
        gridView.isExpanded = true;
        gridView.isVerticalScrollBarEnabled = false
    }

    private fun updateViewPager(imagesList: List<MovieResult>) {
        imageItemList = ArrayList()
        viewPager = fragmentMoviesBinding.viewPagerImgSlider
        viewPager.visibility = GONE
        viewPager.currentItem = viewPager.currentItem + 1
        sliderAdapter = SliderAdapter(viewPager, imagesList, activity?.baseContext)
        viewPager.visibility = VISIBLE
        viewPager.adapter = sliderAdapter

        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()

        compositePageTransformer.addTransformer(MarginPageTransformer(0))
        compositePageTransformer.addTransformer { page, position ->
            //increase the size of the focused page
            val r: Float = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        viewPager.setPageTransformer(compositePageTransformer)
        sliderRun = Runnable {
            viewPager.currentItem = viewPager.currentItem + 2
        }

        //to enable automatic swiping on viewpager
        val sliderHandler = Handler()
        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRun)
                    sliderHandler.postDelayed(sliderRun, 3000)
                }
            }
        )


        //Search results will update the gridview
        model.searchResultData.observe(viewLifecycleOwner, Observer { it ->
            updateGridView(it)
        })
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = SearchView(
            (activity as MainActivity).supportActionBar!!.themedContext
        )
        // MenuItemCompat.setShowAsAction(item, //MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | //MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        //  MenuItemCompat.setActionView(item, searchView);
        // These lines are deprecated in API 26 using instead
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
        item.actionView = searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                model.getSearchResults(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
}