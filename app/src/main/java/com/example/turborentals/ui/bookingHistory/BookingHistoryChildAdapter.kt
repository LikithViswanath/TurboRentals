package com.example.turborentals.ui.bookingHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turborentals.data.RentalDetails
import com.example.turborentals.databinding.BookingHistoryChildListItemBinding

class BookingHistoryChildAdapter : ListAdapter<RentalDetails,BookingHistoryChildAdapter.BookingHistoryChildViewHolder>(BookingHistoryChildDiffUtils()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BookingHistoryChildViewHolder {
       return BookingHistoryChildViewHolder.layoutInfo(parent)
    }

    override fun onBindViewHolder(holder: BookingHistoryChildViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    class BookingHistoryChildViewHolder( val binding : BookingHistoryChildListItemBinding ) : RecyclerView.ViewHolder(binding.root){

        fun bindData( rentalDetails: RentalDetails ){
            binding.rentalDetails = rentalDetails
        }

        companion object{
            fun layoutInfo( parent: ViewGroup ) : BookingHistoryChildViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BookingHistoryChildListItemBinding.inflate(layoutInflater,parent,false)
                return BookingHistoryChildViewHolder(binding)
            }
        }
    }

}

class BookingHistoryChildDiffUtils : DiffUtil.ItemCallback<RentalDetails>(){
    override fun areItemsTheSame(oldItem: RentalDetails, newItem: RentalDetails): Boolean {
       return oldItem.RcNumber == newItem.RcNumber
    }

    override fun areContentsTheSame(oldItem: RentalDetails, newItem: RentalDetails): Boolean {
        return  oldItem == newItem
    }

}