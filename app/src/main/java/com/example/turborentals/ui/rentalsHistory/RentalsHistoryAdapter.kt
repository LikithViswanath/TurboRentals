package com.example.turborentals.ui.rentalsHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turborentals.data.RentalHistory
import com.example.turborentals.databinding.RentalsHistoryListItemBinding

class RentalsHistoryAdapter : ListAdapter<RentalHistory, RentalsHistoryAdapter.RentalsHistoryViewHolder>( RentalHistoryDiffUtilCallBacks() ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalsHistoryViewHolder {
        return RentalsHistoryViewHolder.layoutInfo(parent)
    }

    override fun onBindViewHolder(holder: RentalsHistoryViewHolder, position: Int) {
          holder.bindData(getItem(position))
    }

    class RentalsHistoryViewHolder( val binding : RentalsHistoryListItemBinding ) : RecyclerView.ViewHolder( binding.root ) {

        fun bindData( rentalHistory: RentalHistory ){
            binding.rentalHistory = rentalHistory
            binding.executePendingBindings()
        }

        companion object{
          fun  layoutInfo( parent: ViewGroup ) : RentalsHistoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RentalsHistoryListItemBinding.inflate(layoutInflater,parent,false)
              return RentalsHistoryViewHolder(binding)
            }
        }

    }

}

class RentalHistoryDiffUtilCallBacks : DiffUtil.ItemCallback<RentalHistory>(){
    override fun areItemsTheSame(oldItem: RentalHistory, newItem: RentalHistory): Boolean {
         return oldItem.RcNumber == newItem.RcNumber
    }

    override fun areContentsTheSame(oldItem: RentalHistory, newItem: RentalHistory): Boolean {
        return  oldItem == newItem
    }

}