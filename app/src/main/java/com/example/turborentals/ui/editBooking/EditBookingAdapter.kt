package com.example.turborentals.ui.editBooking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turborentals.data.RentalDetails
import com.example.turborentals.databinding.BookingHistoryChildListItemBinding
import com.example.turborentals.databinding.EditBookingListItemBinding

class EditBookingAdapter : ListAdapter<RentalDetails,EditBookingAdapter.EditBookingViewHolder>(EditBookingDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditBookingViewHolder {
        return EditBookingViewHolder.layoutInfo(parent)
    }

    override fun onBindViewHolder(holder: EditBookingViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    class EditBookingViewHolder( val binding : EditBookingListItemBinding ) : RecyclerView.ViewHolder( binding.root ) {

        fun bindData( rentalDetails: RentalDetails ){
            binding.rentalDetails = rentalDetails
            binding.executePendingBindings()
        }

        companion object{
            fun layoutInfo( parent: ViewGroup ) : EditBookingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = EditBookingListItemBinding.inflate(layoutInflater,parent,false)
                return EditBookingViewHolder(binding)
            }
        }

    }

}

class EditBookingDiffUtils : DiffUtil.ItemCallback<RentalDetails>(){
    override fun areItemsTheSame(oldItem: RentalDetails, newItem: RentalDetails): Boolean {
        return  oldItem.RcNumber == newItem.RcNumber
    }

    override fun areContentsTheSame(oldItem: RentalDetails, newItem: RentalDetails): Boolean {
        return  oldItem == newItem
    }

}