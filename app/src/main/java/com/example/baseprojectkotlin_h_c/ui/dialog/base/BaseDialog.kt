package com.example.baseprojectkotlin_h_c.ui.dialog.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseDialog<V : ViewDataBinding>: DialogFragment() {
    protected lateinit var binding: V
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        binding.lifecycleOwner = this
        initView()
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
    abstract fun getLayout(): Int
    abstract fun initView()
    abstract fun initViewModel()
}
