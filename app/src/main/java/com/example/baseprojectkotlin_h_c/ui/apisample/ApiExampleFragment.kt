package com.example.baseprojectkotlin_h_c.ui.apisample

import androidx.lifecycle.ViewModelProvider
import com.example.baseprojectkotlin_h_c.ui.base.livephoto.BasePhotoFragment

class ApiExampleFragment : BasePhotoFragment<ApiExampleViewModel>() {
    override fun initViewModel() {
        val factory = ApiExampleViewModel.Factory(requireActivity().application)
        mViewModel = ViewModelProvider(this, factory)[ApiExampleViewModel::class.java]
        observeData()
        mViewModel.fetchPhotoList(false)

    }

    override fun refreshData() {
        mViewModel.fetchPhotoList(true)
    }
}