package com.example.ebcometest.core.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import com.example.ebcometest.R
import com.example.ebcometest.utils.GlobalConstants
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

typealias PermissionGranted = (Array<String>) -> Unit
typealias PermissionDenied = (Array<String>) -> Unit


abstract class BaseActivity<E : ViewDataBinding> : AppCompatActivity() {

    private val permissionRequest: Int = 12000

    private var granted: PermissionGranted? = null

    var denied: PermissionDenied? = null

    lateinit var binding: E

    abstract fun getResourceLayoutId(): Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, getResourceLayoutId())
        binding.lifecycleOwner = this
        modifyWindowsDecor()
    }


    protected open fun modifyWindowsDecor(){
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setStatusBarColorResource(android.R.color.transparent)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = view.layoutParams as ViewGroup.MarginLayoutParams
            params.apply {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }

            // Return CONSUMED if you don't want want the window insets to keep being
            // passed down to descendant views.
            WindowInsetsCompat.CONSUMED
        }
    }

    protected fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    open fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
    }

    open fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG)
            .show()
    }

    open fun onShowError(msg:String){

    }
    open fun onShowLoading() {
        Timber.tag(GlobalConstants.TAG).d("Current Activity Ui State :=> Loading")
    }

    open fun onHideLoading() {
        Timber.tag(GlobalConstants.TAG).d("Current Activity Ui State :=> Loaded")
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequest) {
            if (permissions.size == grantResults.size) {
                if (granted != null)
                    granted!!(arrayOf(*permissions))
            } else {
                denied!!(arrayOf(*permissions))
            }
        }
    }

    open fun checkPermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            )
                return false
        }
        return true
    }

    open fun permissionsRequest(
        permissions: Array<String>,
        granted: PermissionGranted? = null,
        denied: PermissionDenied? = null
    ) {
        var hasPermission = true
        val deniedPermissions = mutableListOf<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                hasPermission = false
                deniedPermissions.add(permission)
            }
        }
        if (hasPermission) {
            granted?.let { it(permissions) }
        } else {
            denied?.let { it(deniedPermissions.toTypedArray()) }
            this.granted = granted
            this.denied = denied
            ActivityCompat.requestPermissions(this, permissions, permissionRequest)
        }
    }

    open fun setStatusBarColorResource(@ColorRes color: Int) {
        setStatusBarColor(ContextCompat.getColor(this, color))
    }

    private fun setStatusBarColor(color: Int) {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }

}