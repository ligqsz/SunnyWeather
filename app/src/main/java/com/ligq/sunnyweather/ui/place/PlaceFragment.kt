package com.ligq.sunnyweather.ui.place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ligq.sunnyweather.MainActivity
import com.ligq.sunnyweather.R
import com.ligq.sunnyweather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : Fragment() {
    val viewModel: PlaceViewModel by viewModels()
    private lateinit var adapter: PlaceAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is MainActivity && viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }
        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerview.adapter = adapter
        searchPlaceEdit.addTextChangedListener { text ->
            val content = text.toString()
            Log.d("placeFragment", "content:$content")
            if (content.isNotEmpty()) {
                Log.d("placeFragment", "content not empty")
                recyclerview.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.searchPlaces(content)
            } else {
                Log.d("placeFragment", "content empty")
                recyclerview.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        //result?????????Repository#searchPlaces???emit(result)???????????????
        viewModel.placeLiveData.observe(viewLifecycleOwner, { result ->
            val places = result.getOrNull()
            Log.d("placeFragment", "places:$places")
            if (places != null) {
                recyclerview.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                Log.d("placeFragment", "adapter count:${viewModel.placeList.size}")
                adapter.notifyDataSetChanged()
            } else {
                recyclerview.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                Toast.makeText(activity, "????????????????????????", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

}