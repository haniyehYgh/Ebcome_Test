package com.example.ebcometest.core.base

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ebcometest.R
import com.example.ebcometest.utils.GlobalConstants
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

abstract class BaseFragment<E : ViewDataBinding> : Fragment() {

    lateinit var binding: E

    abstract fun getResourceLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            getResourceLayoutId(),
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    open fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .show()
    }

    open fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG)
            .show()
    }

    open fun onShowError(msg: String) {}

    open fun showLoading() {
        Timber.tag(GlobalConstants.TAG).d("Current Fragment Ui State :=> Loading")
    }

    open fun hideLoading() {
        Timber.tag(GlobalConstants.TAG).d("Current Fragment Ui State :=> Loaded")
    }

    protected fun hideKeyboard(view: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // START ==> This Section is for handling gallery image picking
    private val _galleryResultLivedata = MutableLiveData<Uri?>()

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            _galleryResultLivedata.value = it
        }

    fun openGallery(): LiveData<Uri?> {
        activityResultLauncher.launch("image/*")
        return _galleryResultLivedata
    }
    // <== End


    // START ==> This Section is for handling Permission requests
    private val _permissionResultLivedata = MutableLiveData<Boolean>()

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            _permissionResultLivedata.value = permissions.entries.all { it.value }
        }

    fun requestPermissions(permissions: Array<String>): LiveData<Boolean> {
        requestMultiplePermissions.launch(permissions)
        return _permissionResultLivedata
    }

    // <== End


}

