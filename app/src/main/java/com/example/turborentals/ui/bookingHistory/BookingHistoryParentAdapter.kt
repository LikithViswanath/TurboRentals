package com.example.turborentals.ui.bookingHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turborentals.data.BookingDetails
import com.example.turborentals.databinding.BookingHistoryChildBinding

class BookingHistoryParentAdapter : ListAdapter<BookingDetails,BookingHistoryParentAdapter.BookingHistoryParentViewHolder>(BookingHistoryParentDiffUtils()) {

    var bookingDetailsClickListener : BookingDetailsClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BookingHistoryParentViewHolder {
      return BookingHistoryParentViewHolder.layoutInfo(parent)
    }

    override fun onBindViewHolder(holder: BookingHistoryParentViewHolder, position: Int) {
        holder.bindData(getItem(position),bookingDetailsClickListener!!)
    }

    class BookingHistoryParentViewHolder( val binding : BookingHistoryChildBinding) : RecyclerView.ViewHolder( binding.root ) {

        private val viewPool = RecyclerView.RecycledViewPool()

        fun bindData( bookingDetails: BookingDetails,bookingDetailsClickListener: BookingDetailsClickListener ){
            binding.bookingDetails = bookingDetails
            binding.bookingDetailsClickListener = bookingDetailsClickListener
            binding.bookingHistoryChildRecycler.setRecycledViewPool(viewPool)
            binding.executePendingBindings()
        }

        companion object{
            fun layoutInfo( parent: ViewGroup ) : BookingHistoryParentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BookingHistoryChildBinding.inflate(layoutInflater,parent,false)
                return BookingHistoryParentViewHolder(binding)
            }
        }

    }

}

class BookingDetailsClickListener( val listener : (bookingDetails: BookingDetails)->Unit ){
    fun onClick( bookingDetails: BookingDetails ) = listener(bookingDetails)
}

class BookingHistoryParentDiffUtils : DiffUtil.ItemCallback<BookingDetails>(){
    override fun areItemsTheSame(oldItem: BookingDetails, newItem: BookingDetails): Boolean {
        return  oldItem.phoneNumber == newItem.phoneNumber
    }

    override fun areContentsTheSame(oldItem: BookingDetails, newItem: BookingDetails): Boolean {
        return  oldItem == newItem
    }

}