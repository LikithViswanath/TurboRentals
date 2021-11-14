package com.example.turborentals.ui.bookingDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turborentals.data.BookingDetails
import com.example.turborentals.data.RentalDetails
import com.example.turborentals.databinding.BookingDetailsListItemBinding

class BookingDetailsAdapter : ListAdapter<RentalDetails,BookingDetailsAdapter.BookingDetailsViewHolder>( BookingDetailsDiffUtils() ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingDetailsViewHolder {
        return BookingDetailsViewHolder.layoutInfo(parent)
    }

    override fun onBindViewHolder(holder: BookingDetailsViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    class BookingDetailsViewHolder( val binding : BookingDetailsListItemBinding ) : RecyclerView.ViewHolder( binding.root ) {

        fun bindData( rentalDetails: RentalDetails ){
             binding.rentalDetails = rentalDetails
            binding.executePendingBindings()
        }

        companion object{
            fun layoutInfo( parent: ViewGroup ): BookingDetailsViewHolder {
                 val layoutInflater = LayoutInflater.from(parent.context)
                 val binding = BookingDetailsListItemBinding.inflate(layoutInflater,parent,false)
                return BookingDetailsViewHolder(binding)
            }
        }

    }

}

class BookingDetailsDiffUtils : DiffUtil.ItemCallback<RentalDetails>(){
    override fun areItemsTheSame(oldItem: RentalDetails, newItem: RentalDetails): Boolean {
       return oldItem.RcNumber == newItem.RcNumber
    }

    override fun areContentsTheSame(oldItem: RentalDetails, newItem: RentalDetails): Boolean {
        return oldItem == newItem
    }

}