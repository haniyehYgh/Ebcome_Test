package com.example.ebcometest.core.parent

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.example.ebcometest.core.base.BaseActivity
import com.example.ebcometest.core.base.BaseAndroidViewModel
import com.example.ebcometest.core.model.UiState


abstract class ParentActivity<T : BaseAndroidViewModel,E:ViewDataBinding> : BaseActivity<E>() {

     lateinit var viewModel: T
     abstract fun getViewModelClass(): Class<T>

     override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)

          viewModel = ViewModelProvider(this)[getViewModelClass()]

          viewModel.getNetworkErrors().observe(this){ status ->
               onShowError(status)
          }

          viewModel.getUiState().observe(this){state ->
               state?.let {
                    when(it){
                         UiState.LOADING -> onShowLoading()
                         UiState.LOADED -> onHideLoading()
                    }
               }
          }
     }
}