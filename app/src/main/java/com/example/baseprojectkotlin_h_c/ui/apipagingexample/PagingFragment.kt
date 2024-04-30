package com.example.baseprojectkotlin_h_c.ui.apipagingexample
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.baseprojectkotlin_h_c.ui.base.livephoto.BasePhotoFragment

class PagingFragment : BasePhotoFragment<PagingExampleViewModel>() {
    var firstVisibleItem = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    var visibleThreshold = 1

    override fun initView() {
        super.initView()
        binding.apply {
            recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    visibleItemCount = recyclerView.childCount
                    totalItemCount = mLayoutManager!!.itemCount
                    firstVisibleItem = mLayoutManager!!.findFirstVisibleItemPosition()
                    if (dy > 0 && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                        mViewModel.fetchData(true)
                    }
                }
            })
        }

    }

    override fun initViewModel() {
        val factory = PagingExampleViewModel.Factory(requireActivity().application)
        mViewModel =
            ViewModelProvider(this, factory)[PagingExampleViewModel::class.java]
        observeData()
        mViewModel.fetchData(false)

    }

    override fun refreshData() {
        mViewModel.fetchData(false)
    }

}