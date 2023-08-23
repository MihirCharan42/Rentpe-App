package com.example.rentpe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rentpe.databinding.HouseItemBinding
import com.example.rentpe.databinding.TransacationItemBinding
import com.example.rentpe.models.house.read.House
import com.example.rentpe.models.transactions.read.Transaction
import java.text.SimpleDateFormat
import java.util.Calendar


class TransactionAdapter():  ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(ComparatorDiffUtil()) {

    inner class TransactionViewHolder(val binding: TransacationItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(transaction: Transaction){
            binding.transactionId.text = transaction.transaction_id
            binding.status.text = "From ${transaction.tenant_user__name} to ${transaction.landlord_user__name}"
            binding.transactionAmount.text = transaction.amount.toString()
            binding.transactionDate.text = transaction.created_at
        }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.transaction_id == newItem.transaction_id
        }
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = TransacationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }


    override fun onBindViewHolder(holder: TransactionAdapter.TransactionViewHolder, position: Int) {
        val transaction = getItem(position)
        transaction?.let {
            holder.bind(it)
        }
    }


}