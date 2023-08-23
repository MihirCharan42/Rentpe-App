package com.example.rentpe.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rentpe.R
import com.example.rentpe.activities.HomeActivity
import com.example.rentpe.adapters.HouseAdapter
import com.example.rentpe.databinding.FragmentHomeBinding
import com.example.rentpe.databinding.FragmentHouseBinding
import com.example.rentpe.models.house.read.House
import com.example.rentpe.models.house.read.ReadHouseResponse
import com.example.rentpe.utils.NetworkResult
import com.example.rentpe.viewModels.HouseViewModel
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding ?= null
    private val binding get() = _binding!!
    private lateinit var adapter: HouseAdapter
    private val houseViewModel by viewModels<HouseViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        houseViewModel.getHomes()
        adapter = HouseAdapter(::onHouseClicked, activity!!)

        binding.refreshHomes.setOnRefreshListener {
            binding.refreshHomes.isRefreshing = false
            houseViewModel.getHomes()
        }
        houseViewModel.houseReadLiveData.observe(viewLifecycleOwner) {
            binding.progressBarHome.isVisible = false
            binding.errorTv.isVisible = false
            binding.refreshHomes.isVisible = false
            when(it){
                is NetworkResult.Loading -> {
                    binding.progressBarHome.isVisible = true
                    binding.refreshHomes.isVisible = false
                }
                is NetworkResult.Success -> {
                    binding.errorTv.isVisible = false
                    binding.progressBarHome.isVisible = false
                    binding.refreshHomes.isVisible = true
                    adapter.submitList(it.data!!.results)
                }
                is NetworkResult.Error -> {
                    binding.progressBarHome.isVisible = false
                    binding.errorTv.isVisible = true
                    binding.refreshHomes.isVisible = false
                    binding.errorTv.text = it.message
                }
            }
        }
        binding.houseRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.houseRecyclerview.adapter = adapter
    }

    private fun onHouseClicked(house: House){
        val bundle = Bundle()
        var json = JSONObject()
        json.put("id", house.id)
        json.put("created_at", house.created_at)
        json.put("updated_at", house.updated_at)
        json.put("address", house.address)
        json.put("tenant_name", house.tenant_name)
        json.put("tenant_number", house.tenant_phone)
        json.put("landlord_name", house.landlord_name)
        json.put("landlord_number", house.landlord_phone)
        json.put("last_payment", house.last_payment)
        json.put("rent_due", house.rent_due)
        json.put("rent", house.rent)
        json.put("description", house.description)
        Log.e("JSON STR", json.toString())

        bundle.putString("house", json.toString())
        bundle.putStringArrayList("images", house.images)
        val fragment = HouseFragment()
        fragment.arguments = bundle
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.house_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {}
    }
}