package com.example.woowagithubrepositoryapp.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.woowagithubrepositoryapp.R

@BindingAdapter("iconImage")
fun AppCompatImageView.setIconImage(resource : String){
    when(resource){
        "" -> {
            Glide.with(this.context)
                .load(R.drawable.ic_user)
                .into(this)
        }
        else -> {
            Glide.with(this.context)
                .load(resource)
                .placeholder(R.drawable.ic_user)
                .transform(CircleCrop())
                .error(R.drawable.ic_user)
                .into(this)
        }
    }
}

@BindingAdapter("updatedText")
fun AppCompatTextView.setUpdatedText(resource: String){
    text = TimeUtil.getTimeData(resource)
}