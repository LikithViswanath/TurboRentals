package com.example.turborentals.ui.allRentals

import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turborentals.data.RentalDetails
import com.example.turborentals.databinding.RentalListItemBinding
import com.example.turborentals.generated.callback.OnClickListener

class AllRentalsAdapter : ListAdapter<RentalDetails, AllRentalsAdapter.AllRentalsViewHolder>( AllRentalsDiffCallBacks() ) {

    var setHistoryListener : SetHistoryListener ? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllRentalsViewHolder {
        return AllRentalsViewHolder.layoutInfo(parent)
    }

    override fun onBindViewHolder(holder: AllRentalsViewHolder, position: Int) {
        holder.bindData(getItem(position) , setHistoryListener!! )
    }

    class AllRentalsViewHolder( private val binding : RentalListItemBinding ) : RecyclerView.ViewHolder( binding.root ) {

        fun bindData( rentalDetails: RentalDetails , setHistoryListener: SetHistoryListener ){
            binding.rentalDetails = rentalDetails
            binding.setOnHistoryListener = setHistoryListener
            binding.executePendingBindings()
        }

        companion object{
            fun layoutInfo(parent: ViewGroup) : AllRentalsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding  =  RentalListItemBinding.inflate(layoutInflater,parent,false)
                return AllRentalsViewHolder(binding)
            }
        }
    }

}

class SetHistoryListener( val listener : ( rentalDetails : RentalDetails ) -> Unit ){
    fun onClick( rentalDetails : RentalDetails ) = listener( rentalDetails )
}

class AllRentalsDiffCallBacks : DiffUtil.ItemCallback<RentalDetails>(){

    override fun areItemsTheSame(oldItem: RentalDetails, newItem: RentalDetails): Boolean {
        return oldItem.RcNumber == newItem.RcNumber
    }

    override fun areContentsTheSame(oldItem: RentalDetails, newItem: RentalDetails): Boolean {
        return oldItem == newItem
    }

}