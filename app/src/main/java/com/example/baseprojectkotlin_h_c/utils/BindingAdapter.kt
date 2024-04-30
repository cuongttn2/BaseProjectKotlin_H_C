package com.example.baseprojectkotlin_h_c.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.baseprojectkotlin_h_c.R
import com.example.baseprojectkotlin_h_c.data.model.LivePhotoModel
import com.example.baseprojectkotlin_h_c.data.model.PhotoModel
import com.example.baseprojectkotlin_h_c.ui.dialog.popup.ActionModel

@BindingAdapter("android:bindThumbnailFile")
fun ImageView.bindThumbnailFile(photoModel: PhotoModel?) {
    if (photoModel != null) {
        Glide.with(this).load(photoModel.file)
            .placeholder(R.drawable.ic_baseline_image_32)
            .error(R.drawable.ic_baseline_image_32).into(this)
    } else {
        Glide.with(this).load(R.drawable.ic_baseline_image_32)
            .placeholder(R.drawable.ic_baseline_image_32)
            .error(R.drawable.ic_baseline_image_32).into(this)
    }

}

@BindingAdapter("android:livePhoto")
fun ImageView.bindLivePhoto(photoModel: LivePhotoModel?) {
    if (photoModel != null) {
        Glide.with(this).load(photoModel.download_url).fitCenter().centerCrop().override(256, 256)
            .placeholder(R.drawable.ic_baseline_image_32)
            .error(R.drawable.ic_baseline_image_32).into(this)
    } else {
        Glide.with(this).load(R.drawable.ic_baseline_image_32)
            .placeholder(R.drawable.ic_baseline_image_32)
            .error(R.drawable.ic_baseline_image_32).into(this)
    }

}

@BindingAdapter("android:iconForAction")
fun ImageView.iconForAction(actionModel: ActionModel) {
    if (actionModel.icon == -1) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        setImageResource(actionModel.icon)
    }

}

