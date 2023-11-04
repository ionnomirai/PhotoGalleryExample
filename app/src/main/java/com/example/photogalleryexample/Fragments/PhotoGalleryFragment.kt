package com.example.photogalleryexample.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photogalleryexample.R
import com.example.photogalleryexample.databinding.FragmentPhotoGalleryBinding
import com.example.photogalleryexample.recyclerView.PhotoListAdapter
import com.example.photogalleryexample.repositories.PhotoRepository
import com.example.photogalleryexample.retrofit.FlickrApi
import com.example.photogalleryexample.viewModels.PhotoGalleryViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.lang.Exception

private const val TAG = "PhotoGalleryFragment_TAG"
class PhotoGalleryFragment : Fragment(){

    private var _binding : FragmentPhotoGalleryBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding because it is null. Is the view visible?"
        }

    private var searchView: SearchView? = null

    private val photoGalleryViewModel: PhotoGalleryViewModel by viewModels()

    private val menuProvider = object : MenuProvider{
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            // creating a menu
            menuInflater.inflate(R.menu.fragment_photo_gallery, menu)

            // This is item in Menu
            val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
            // This is actionView in menuItem
            searchView = searchItem.actionView as? SearchView

            // Setting actionView. How this view will respond to a user
            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                // This callback is executed when the user submits a query
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $query")
                    photoGalleryViewModel.setQuery(query ?: "")
                    return true
                }

                // This callback is executed any time text in the SearchView text box changes.
                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(TAG, "QueryTextChange: $newText")
                    return false
                }
            })
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when(menuItem.itemId){
                R.id.menu_item_clear -> {
                    photoGalleryViewModel.setQuery("")
                    return true
                }
                else -> return false
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)
        binding.photoGrid.layoutManager = GridLayoutManager(context, 3)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*I don't understand, why should we put one viewLifeCycleOwner inside other?
        * Solution: I didn't notice that is the instruction to perform action at the
        *           specific time (STARTED)*/
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                photoGalleryViewModel.uiState.collect{state ->
                    binding.photoGrid.adapter = PhotoListAdapter(state.images)
                    searchView?.setQuery(state.query, false)
                }
            }
        }

        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchView = null
    }
}