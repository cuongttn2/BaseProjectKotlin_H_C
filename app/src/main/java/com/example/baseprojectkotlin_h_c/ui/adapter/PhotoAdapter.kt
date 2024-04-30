package com.example.baseprojectkotlin_h_c.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.baseprojectkotlin_h_c.data.model.LivePhotoModel
import com.example.baseprojectkotlin_h_c.databinding.ItemLivePhotoBinding

class PhotoAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val data = mutableListOf<LivePhotoModel>()
    fun update(isAddMore: Boolean, newData: List<LivePhotoModel>?) {
        if (isAddMore) {
            val oldSize = data.size
            if (newData != null && newData.isNotEmpty()) {
                data.addAll(newData)
                notifyItemInserted(oldSize)
            }
        } else {
            data.clear()
            if (newData != null && newData.isNotEmpty()) {
                data.addAll(newData)
            }
            notifyDataSetChanged()
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ItemLivePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LocationViewHolder) {
            holder.bind(position)
        }
    }

    inner class LocationViewHolder(private val binding: ItemLivePhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.livePhoto = data[position]
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}