package com.example.rentpe.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.rentpe.R
import com.example.rentpe.activities.house.HouseDetails
import com.example.rentpe.activities.transactions.PaymentActivity
import com.example.rentpe.databinding.FragmentHouseBinding
import com.example.rentpe.models.house.HouseRequest
import com.example.rentpe.models.house.UpdateHouseRequest
import com.example.rentpe.models.house.read.House
import com.example.rentpe.utils.NetworkResult
import com.example.rentpe.viewModels.HouseViewModel
import com.google.gson.JsonObject
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class HouseFragment : Fragment() {

    private var _binding: FragmentHouseBinding ?= null
    private val binding get() = _binding!!
    private val houseViewModel by viewModels<HouseViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHouseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val str: String? = arguments?.getString("house")
        val images: ArrayList<String>? = arguments?.getStringArrayList("images")
        val json = JSONObject(str)
        json.let {
            binding.goToImagesFragmentBtn.setOnClickListener {view ->

                val fragment = ImagesFragment.newInstance(images!!)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.house_fragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            binding.addressTextfield.setText(it.getString("address"))
            binding.rentTextfield.setText(it.getInt("rent").toString())
            binding.descriptionTextfield.setText(it.getString("description"))

            binding.landlordNameTextfield.setText(it.getString("landlord_name"))
            binding.landlordNumberTextfield.setText(it.getString("landlord_number"))

            binding.tenantNameTextfield.setText(it.getString("tenant_name"))
            binding.tenantNumberTextfield.setText(it.getString("tenant_number"))

            binding.payRentBtn.isEnabled = it.getInt("rent").toString() == SimpleDateFormat("yyyy-mm-dd").format(Calendar.getInstance().time)

            houseViewModel.houseStatusLiveData.observe(this@HouseFragment){obj ->
                binding.loader.isVisible = false
                binding.controlButtons.isVisible = true
                when(obj) {
                    is NetworkResult.Loading -> {
                        binding.loader.isVisible = true
                        binding.controlButtons.isVisible = false
                    }
                    is NetworkResult.Success -> {
                        binding.loader.isVisible = false
                        Toast.makeText(context, obj.data!!.message, Toast.LENGTH_SHORT).show()
                        binding.controlButtons.isVisible = true
                    }
                    is NetworkResult.Error -> {
                        binding.loader.isVisible = false
                        Toast.makeText(context, obj.message, Toast.LENGTH_SHORT).show()
                        binding.controlButtons.isVisible = true
                    }
                }
            }

            binding.updateBtn.setOnClickListener {view ->
                val new_house = initiateHouse(it, images!!)
                if(checkAnyChange(new_house, it)){
                    houseViewModel.putHomes(json.getInt("id"), UpdateHouseRequest(
                        new_house.address,
                        new_house.description,
                        new_house.landlord_name,
                        new_house.landlord_phone,
                        new_house.rent,
                        new_house.tenant_name,
                        new_house.tenant_phone
                    ))
                } else {
                    Toast.makeText(context, "No changes detected!", Toast.LENGTH_SHORT).show()
                }
            }

            binding.payRentBtn.setOnClickListener {
                val i = Intent(context, PaymentActivity::class.java)
                startActivity(i)
            }
        }
    }

    private fun checkAnyChange(house1: House, house2: JSONObject): Boolean {
        return house1.address != house2.getString("address") ||
                house1.created_at != house2.getString("created_at") ||
                house1.description != house2.getString("description") ||
                house1.landlord_name != house2.getString("landlord_name") ||
                house1.landlord_phone != house2.getString("landlord_number") ||
                house1.last_payment != house2.getString("last_payment") ||
                house1.rent != house2.getInt("rent") ||
                house1.rent_due != house2.getString("rent_due") ||
                house1.tenant_name != house2.getString("tenant_name") ||
                house1.tenant_phone != house2.getString("tenant_number") ||
                house1.updated_at != house2.getString("updated_at")
    }

    private fun initiateHouse(jsonObject: JSONObject, images: ArrayList<String>): House {
        val address = binding.addressTextfield.text.toString()
        val description = binding.descriptionTextfield.text.toString()
        val rent = binding.rentTextfield.text.toString()

        val tenantName = binding.tenantNameTextfield.text.toString()
        val tenantNumber = binding.tenantNumberTextfield.text.toString()

        val landlordName = binding.landlordNameTextfield.text.toString()
        val landlordNumber = binding.landlordNumberTextfield.text.toString()

        val house = House(
            address,
            jsonObject.getString("created_at"),
            description,
            jsonObject.getInt("id"),
            images,
            landlordName,
            landlordNumber,
            jsonObject.getString("last_payment"),
            Integer.parseInt(rent),
            jsonObject.getString("rent_due"),
            tenantName,
            tenantNumber,
            jsonObject.getString("updated_at"),
            )
        return house
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    companion object {
//        private const val ARG_HOUSE = "arg_house"
//        private const val ARG_IMAGES = "arg_images"
//
//        fun newInstance(house: String): HouseFragment {
//            val fragment = HouseFragment()
//            val args = Bundle().apply {
//                putString(ARG_HOUSE, house)
//            }
//            fragment.arguments = args
//            return fragment
//        }
//    }
}