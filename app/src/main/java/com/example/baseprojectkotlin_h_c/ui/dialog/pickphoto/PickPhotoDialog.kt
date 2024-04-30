package com.example.baseprojectkotlin_h_c.ui.dialog.pickphoto

import android.os.Bundle
import android.os.Parcelable
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseprojectkotlin_h_c.App
import com.example.baseprojectkotlin_h_c.R
import com.example.baseprojectkotlin_h_c.data.api.response.DataResponse
import com.example.baseprojectkotlin_h_c.data.api.response.LoadingStatus
import com.example.baseprojectkotlin_h_c.data.model.PhotoModel
import com.example.baseprojectkotlin_h_c.databinding.DialogPickPhotoBinding
import com.example.baseprojectkotlin_h_c.ui.dialog.base.BaseDialog
import com.example.baseprojectkotlin_h_c.ui.dialog.pickphoto.data.PickModelDataType
import com.example.baseprojectkotlin_h_c.ui.dialog.pickphoto.data.adapter.PhotoAdapter


class PickPhotoDialog : BaseDialog<DialogPickPhotoBinding>(),
    PhotoAdapter.ListenerClickItem {
    lateinit var mViewModel: PickPhotoViewModel
    private lateinit var mAdapter: PhotoAdapter
    var onPhotoPickListener: OnPhotoPickListener? = null
    lateinit var mLayoutManager: GridLayoutManager
    var albumState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.Theme_App_Dialog_FullScreen)
        mAdapter = PhotoAdapter(requireActivity(), this)
    }

    override fun getLayout(): Int {
        return R.layout.dialog_pick_photo
    }

    override fun initView() {
        binding.apply {
            recycleView.layoutManager = LinearLayoutManager(requireContext())
            recycleView.setHasFixedSize(true)
            recycleView.adapter = mAdapter
        }
        binding.viewModel = mViewModel
        mViewModel.loadingAll()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setOnKeyListener { dialog, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                mViewModel.onClose()
            }
            true
        }
    }

    override fun initViewModel() {
        val factory = PickPhotoViewModel.Factory(requireActivity().application as App)
        mViewModel = ViewModelProvider(this, factory)[PickPhotoViewModel::class.java]
        mViewModel.mPickPhotoModel.observe(this) {
            it?.let {
                if (it.loadingStatus == LoadingStatus.Success) {
                    val body = (it as DataResponse.DataSuccess).body
                    mLayoutManager = GridLayoutManager(
                        requireActivity(),
                        if (body.pickModelDataType == PickModelDataType.LoadAlbum) {
                            1
                        } else {
                            COLUMNS
                        }
                    )
                    binding.recycleView.layoutManager = mLayoutManager
                    if (body.pickModelDataType == PickModelDataType.LoadAlbum){
                        binding.recycleView.layoutManager!!.onRestoreInstanceState(albumState)
                    }
                    mAdapter.update(body)
                }
            }
        }

        mViewModel.isExitDialog.observe(this) {
            it?.let {
                if (it) {
                    dismiss()
                    onPhotoPickListener?.onPickDismiss()
                }
            }
        }
    }

    override fun onClickFileItem(
        position: Int,
        pickModelDataType: PickModelDataType,
        photoModel: PhotoModel
    ) {
        if (pickModelDataType == PickModelDataType.LoadAlbum) {
            albumState =  binding.recycleView.layoutManager!!.onSaveInstanceState()
            mViewModel.loadAlbumDetail(photoModel.albumId)
        } else {
            dismiss()
            onPhotoPickListener?.onPicked(photoModel)
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    interface OnPhotoPickListener {
        fun onPicked(photoModel: PhotoModel)
        fun onPickDismiss()
    }

    companion object {
        const val COLUMNS = 4
        fun create(): PickPhotoDialog {
            return PickPhotoDialog()
        }
    }
}