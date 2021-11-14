package com.example.turborentals.ui.rentalsHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.turborentals.data.RentalDetails
import com.example.turborentals.databinding.RentalImageListItemBinding

class RentalsImageAdapter : ListAdapter<String,RentalsImageAdapter.RentalImageViewHolder>( RentalsImageDiffCallBacks() ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalImageViewHolder {
      return RentalImageViewHolder.layoutInfo(parent)
    }

    override fun onBindViewHolder(holder: RentalImageViewHolder, position: Int) {
         holder.bindData(getItem(position))
    }

    class RentalImageViewHolder( val binding: RentalImageListItemBinding ) : RecyclerView.ViewHolder( binding.root ) {

        fun bindData( url : String  ) {
            binding.urls =url
        }

        companion object{

            fun layoutInfo( parent: ViewGroup ) : RentalImageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RentalImageListItemBinding.inflate(layoutInflater,parent,false)
                return RentalImageViewHolder(binding)
            }

        }

    }

}

class RentalsImageDiffCallBacks : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
       return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String ): Boolean {
       return  oldItem == newItem
    }

}