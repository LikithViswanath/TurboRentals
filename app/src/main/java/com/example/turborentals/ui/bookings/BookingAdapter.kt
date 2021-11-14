package com.example.turborentals.ui.bookings

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turborentals.data.RentalDetails
import com.example.turborentals.databinding.BookingListItemBinding

class BookingAdapter : ListAdapter<RentalDetails, BookingAdapter.BookingViewHolder>(BookingDiffUtils()) {

    var onSingleClickListener : OnSingleClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        return BookingViewHolder.layoutInfo(parent)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bindData(getItem(position),onSingleClickListener!!)
    }

    class BookingViewHolder( val binding: BookingListItemBinding ) : RecyclerView.ViewHolder( binding.root ) {

        var isSelected = false

        fun bindData( rentalDetails: RentalDetails ,onSingleClickListener: OnSingleClickListener){
          binding.rentalDetails = rentalDetails
            binding.bookingCardView.setOnClickListener {
                isSelected = !isSelected
                if (isSelected) {
                    it.setBackgroundColor(Color.CYAN)
                }else{
                    it.setBackgroundColor(Color.WHITE)
                }
                onSingleClickListener.onClick(rentalDetails)
            }
            binding.executePendingBindings()
        }

        companion object{
            fun layoutInfo( parent: ViewGroup ) : BookingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BookingListItemBinding.inflate(layoutInflater,parent,false)
                return BookingViewHolder(binding)
            }
        }

    }

}

class OnSingleClickListener( val listener : ( rentalDetails : RentalDetails )->Unit ) {
    fun onClick( rentalDetails: RentalDetails) = listener(rentalDetails)
}

class BookingDiffUtils : DiffUtil.ItemCallback<RentalDetails>(){

    override fun areItemsTheSame(oldItem: RentalDetails, newItem: RentalDetails): Boolean {
        return oldItem.RcNumber == newItem.RcNumber
    }

    override fun areContentsTheSame(oldItem: RentalDetails, newItem: RentalDetails): Boolean {
        return oldItem == newItem
    }

}