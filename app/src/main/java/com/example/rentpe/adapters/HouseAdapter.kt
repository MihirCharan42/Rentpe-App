package com.example.rentpe.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rentpe.R
import com.example.rentpe.databinding.HouseItemBinding
import com.example.rentpe.fragments.HouseFragment
import com.example.rentpe.models.house.read.House
import com.example.rentpe.models.house.read.ReadHouseResponse
import com.example.rentpe.utils.NetworkResult
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.Executors
import javax.microedition.khronos.opengles.GL

class HouseAdapter(private val onHouseClicked: (House) -> Unit, context: Context): ListAdapter<House, HouseAdapter.HouseViewHolder>(ComparatorDiffUtil()) {

    val mContext = context

    class ComparatorDiffUtil : DiffUtil.ItemCallback<House>() {
        override fun areItemsTheSame(oldItem: House, newItem: House): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: House, newItem: House): Boolean {
            return oldItem == newItem
        }
    }

    inner class HouseViewHolder(val binding: HouseItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(house: House){
            binding.houseAddress.text = house.address
            binding.houseRent.text = "$ " + house.rent.toString()
            if(house.rent_due == SimpleDateFormat("yyyy-mm-dd").format(Calendar.getInstance().time)){
                binding.houseRentStatusDue.isVisible = true
                binding.houseRentStatusDue.text = "Rent is due on ${house.rent_due}"
                binding.houseRentStatusPaid.isVisible = false
            } else if(house.last_payment!= "null") {
                binding.houseRentStatusDue.isVisible = false
                binding.houseRentStatusPaid.isVisible = true
                binding.houseRentStatusPaid.text = "Rent was paid on ${house.last_payment}"
            } else {
                binding.houseRentStatusDue.isVisible = false
                binding.houseRentStatusPaid.isVisible = false
            }


            Glide.with(mContext).load("https" + house.images[0].substring(4)).into(binding.houseImage);

            binding.houseItem.setOnClickListener{
                onHouseClicked(house)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val binding = HouseItemBinding.inflate(inflater, parent, false)
        return HouseViewHolder(binding)
    }


    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        val house = getItem(position)
        house?.let {
            holder.bind(it)
        }
    }
}