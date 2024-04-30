package com.example.baseprojectkotlin_h_c.ui.dialog.pickphoto.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.baseprojectkotlin_h_c.data.model.PhotoModel
import com.example.baseprojectkotlin_h_c.databinding.ItemAlbumBinding
import com.example.baseprojectkotlin_h_c.databinding.ItemPhotoBinding
import com.example.baseprojectkotlin_h_c.ui.dialog.pickphoto.data.PickModelDataType
import com.example.baseprojectkotlin_h_c.ui.dialog.pickphoto.data.PickPhotoModel


class PhotoAdapter(
    private val context: Context,
    private val listener: ListenerClickItem
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var pickPhotoModel: PickPhotoModel? = null
    fun update(pickPhotoModel: PickPhotoModel) {
        this.pickPhotoModel = pickPhotoModel
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == PickModelDataType.LoadAlbum.ordinal) {
            val itemBinding = ItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            AlbumViewHolder(itemBinding)
        } else {
            val itemBinding = ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            PhotoViewHolder(itemBinding)
        }

    }


    override fun getItemCount(): Int = if (pickPhotoModel != null) {
        pickPhotoModel!!.photoList.size
    } else {
        0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlbumViewHolder -> {
                holder.bind(position)
            }
            is PhotoViewHolder -> {
                holder.bind(position)
            }
        }

    }


    inner class AlbumViewHolder(itemView: ItemAlbumBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        internal val binding = itemView
        fun bind(position: Int) {
            binding.photoModel = pickPhotoModel!!.photoList[position]
            binding.myLayoutRoot.setOnClickListener {
                if (position < itemCount) {
                    listener.onClickFileItem(
                        position,
                        pickPhotoModel!!.pickModelDataType,
                        pickPhotoModel!!.photoList[position]
                    )
                }
            }
        }
    }


    inner class PhotoViewHolder(itemView: ItemPhotoBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        internal val binding = itemView
        fun bind(position: Int) {
            binding.photoModel = pickPhotoModel!!.photoList[position]
            binding.myLayoutRoot.setOnClickListener {
                if (position < itemCount) {
                    listener.onClickFileItem(
                        position,
                        pickPhotoModel!!.pickModelDataType,
                        pickPhotoModel!!.photoList[position]
                    )
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (pickPhotoModel != null) {
            pickPhotoModel!!.pickModelDataType.ordinal
        } else {
            PickModelDataType.LoadAlbum.ordinal
        }
    }

    interface ListenerClickItem {
        fun onClickFileItem(
            position: Int,
            pickModelDataType: PickModelDataType,
            photoModel: PhotoModel
        )
    }


}
