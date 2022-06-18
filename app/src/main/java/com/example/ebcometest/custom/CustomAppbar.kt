package com.example.ebcometest.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.ebcometest.R
import com.example.ebcometest.databinding.CustomAppbarBinding


class CustomAppbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private lateinit var binding: CustomAppbarBinding

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CustomAppbar, 0, 0)
            .apply {
                try {
                    initialize(context)
                    setTitleText(getString(R.styleable.CustomAppbar_customPageTitle) ?: "")
                    setStartIcon(
                        getDrawable(R.styleable.CustomAppbar_customStartIcon)
                    )
                    setEndIcon(
                        getDrawable(R.styleable.CustomAppbar_customEndIcon)
                    )

                } finally {
                    recycle()
                }
            }

    }

    private fun initialize(context: Context?) {

        val inflater = LayoutInflater.from(context)

        binding = DataBindingUtil
            .inflate(inflater, R.layout.custom_appbar, this, true)

    }

    fun setonEndIconClickListener(listener: () -> Unit) {
        binding.endIcon.setOnClickListener {
            listener.invoke()
        }
    }

    fun setonStartIconClickListener(listener: () -> Unit) {
        binding.startIcon.setOnClickListener {
            listener.invoke()
        }
    }

    fun setTitleText(text: String?) {
        binding.textView.text = text
    }

    fun setStartIcon(drawable: Drawable?) {
        drawable?.let {
            binding.startIcon.setImageDrawable(drawable)
        }

    }

    fun setEndIcon(drawable: Drawable?) {
        drawable.let {
            binding.endIcon.setImageDrawable(drawable)
        }

    }

}