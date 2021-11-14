package com.example.turborentals.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turborentals.data.Rental
import com.example.turborentals.databinding.ProfileListItemBinding

class ProfileAdapter: ListAdapter<Rental,ProfileAdapter.ProfileViewHolder>( ProfileDiffCallBacks() ) {

    var addButtonListener: AddButtonListener? = null
    var bookingsButtonListener: BookingsButtonListener? = null
    var bookButtonListener: BookButtonListener? = null
    var collectButtonListener: CollectButtonListener? = null

    var totalLayoutListener : TotalLayoutListener?=null
    var onBookedListener : OnBookedListener? = null
    var onRentListener : OnRentListener? = null
    var onAvailableListener : OnAvailableListener? = null
    var todayListener : TodayListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder.layoutInfo(parent)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bindData(getItem(position),
            addButtonListener!!,
            bookingsButtonListener!!,
            bookButtonListener!!,
            collectButtonListener!!,

            totalLayoutListener!!,
            onBookedListener!!,
            onRentListener!!,
            onAvailableListener!!,
            todayListener!!
        )
    }

    class ProfileViewHolder( private val binding : ProfileListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bindData(
            _data : Rental,
            addButtonListener: AddButtonListener,
            bookingsButtonListener: BookingsButtonListener,
            bookButtonListener: BookButtonListener,
            collectButtonListener: CollectButtonListener,

            totalLayoutListener: TotalLayoutListener,
            onBookedListener : OnBookedListener,
            onRentListener : OnRentListener,
            onAvailableListener : OnAvailableListener,
            todayListener: TodayListener
        ) {
            binding.rental = _data
            binding.executePendingBindings()
            binding.addButtonListener = addButtonListener
            binding.bookingButtonListener = bookingsButtonListener
            binding.collectButtonListener = collectButtonListener
            binding.bookButtonListener = bookButtonListener
            binding.totalLayoutListener = totalLayoutListener
            binding.onAvailableListener = onAvailableListener
            binding.onBookedListener = onBookedListener
            binding.onRentListener = onRentListener
            binding.todayListener = todayListener

            binding.executePendingBindings()

        }

        companion object {
            fun layoutInfo( parent: ViewGroup): ProfileViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProfileListItemBinding.inflate(layoutInflater,parent,false)
                return ProfileViewHolder( binding)
            }
        }

    }

    class AddButtonListener( val listener: ( rental : Rental )-> Unit ){
        fun onClick( rental: Rental ) = listener( rental )
    }

    class BookingsButtonListener( val listener: ( rental : Rental ) -> Unit ){
        fun onClick( rental: Rental )  = listener( rental )
    }

    class BookButtonListener( val listener: ( rental : Rental ) -> Unit ){
        fun onCLick( rental: Rental )  = listener( rental )
    }

    class CollectButtonListener( val listener: ( rental : Rental ) -> Unit ){
        fun onClick( rental: Rental ) = listener( rental )
    }

    /*---------------------------------------------------------------------------*/

    class TotalLayoutListener( val listener: (rental: Rental) -> Unit ){
        fun onClick( rental: Rental ) = listener( rental )
    }

    class OnRentListener( val listener: (rental: Rental) -> Unit ){
        fun onClick( rental: Rental ) = listener(rental)
    }

    class OnBookedListener( val listener: (rental: Rental) -> Unit ){
        fun onClick( rental: Rental ) = listener(rental)
    }

    class OnAvailableListener( val listener: (rental: Rental) -> Unit ){
        fun onClick( rental: Rental ) = listener(rental)
    }

    class TodayListener( val listener: (rental: Rental) -> Unit ){
        fun onClick( rental: Rental ) = listener(rental)
    }

}

class ProfileDiffCallBacks : DiffUtil.ItemCallback<Rental>() {
    override fun areItemsTheSame(oldItem: Rental, newItem: Rental): Boolean {
        return oldItem.rentalID == newItem.rentalID
    }

    override fun areContentsTheSame(oldItem: Rental, newItem: Rental): Boolean {
        return oldItem == newItem
    }
}

