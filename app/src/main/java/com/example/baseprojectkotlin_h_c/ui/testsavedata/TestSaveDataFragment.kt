package com.example.baseprojectkotlin_h_c.ui.testsavedata

import androidx.lifecycle.ViewModelProvider
import com.example.baseprojectkotlin_h_c.R
import com.example.baseprojectkotlin_h_c.databinding.FragmentSaveDataBinding
import com.example.baseprojectkotlin_h_c.ui.base.BaseWithVMFragment

class TestSaveDataFragment : BaseWithVMFragment<FragmentSaveDataBinding, TestSaveViewModel>() {
    override fun getLayout(): Int {
        return R.layout.fragment_save_data
    }

    override fun initView() {
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this

    }

    override fun onResume() {
        super.onResume()
        mViewModel.getTimeModel()
    }

    override fun initViewModel() {
        val factory = TestSaveViewModel.Factory(requireActivity().application)
        mViewModel = ViewModelProvider(this,factory)[TestSaveViewModel::class.java]
    }
}