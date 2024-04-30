package com.example.baseprojectkotlin_h_c.ui.base.livephoto

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseprojectkotlin_h_c.R
import com.example.baseprojectkotlin_h_c.data.api.response.DataResponse
import com.example.baseprojectkotlin_h_c.data.api.response.LoadingStatus
import com.example.baseprojectkotlin_h_c.databinding.FragmentRecyclerviewBinding
import com.example.baseprojectkotlin_h_c.ui.adapter.PhotoAdapter
import com.example.baseprojectkotlin_h_c.ui.base.BaseWithVMFragment


abstract class BasePhotoFragment<T: BasePhotoViewModel> : BaseWithVMFragment<FragmentRecyclerviewBinding, T>() {
    private val mPhotoAdapter = PhotoAdapter()
    protected var mLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLayoutManager = LinearLayoutManager(requireContext())
    }

    override fun getLayout(): Int {
        return R.layout.fragment_recyclerview
    }

    override fun initView() {
        binding.apply {
            recycleView.layoutManager = mLayoutManager
            recycleView.setHasFixedSize(true)
            recycleView.adapter = mPhotoAdapter
            mySwipeRefreshLayout.setOnRefreshListener {
                refreshData()
            }
        }
        binding.viewModel = mViewModel
    }

    abstract fun refreshData()

    protected fun observeData(){
        mViewModel.photoLisLiveData.observe(this) {
            it?.let {
                when (it.loadingStatus) {
                    LoadingStatus.Success -> {
                        val listPhoto = (it as DataResponse.DataSuccess).body
                        if (listPhoto.photos!=null && listPhoto.photos.isNotEmpty()) {
                            mPhotoAdapter.update(
                                listPhoto.curPage > 1,
                                listPhoto.photos
                            )
                        }
                        binding.mySwipeRefreshLayout.isEnabled = true
                        binding.mySwipeRefreshLayout.isRefreshing = false
                    }
                    LoadingStatus.Refresh -> {
                        binding.mySwipeRefreshLayout.isEnabled = true
                        binding.mySwipeRefreshLayout.isRefreshing = true
                    }
                    else -> {
                        binding.mySwipeRefreshLayout.isEnabled = false
                        binding.mySwipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }
    }

}