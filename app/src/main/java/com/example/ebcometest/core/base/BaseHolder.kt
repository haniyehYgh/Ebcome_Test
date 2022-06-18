package com.example.ebcometest.core.base

import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView



abstract class BaseHolder<T>(binding : ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    var inflater: LayoutInflater = LayoutInflater.from(binding.root.context)

    abstract fun bind(value : T, position : Int)


}