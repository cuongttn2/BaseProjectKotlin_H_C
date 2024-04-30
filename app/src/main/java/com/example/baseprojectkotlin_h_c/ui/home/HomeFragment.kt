package com.example.baseprojectkotlin_h_c.ui.home

import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.baseprojectkotlin_h_c.R
import com.example.baseprojectkotlin_h_c.databinding.FragmentHomeBinding
import com.example.baseprojectkotlin_h_c.ui.base.AbsBaseFragment
import com.example.baseprojectkotlin_h_c.ui.dialog.pickphoto.PickPhotoDialog
import com.example.baseprojectkotlin_h_c.ui.dialog.popup.ActionAdapter
import com.example.baseprojectkotlin_h_c.ui.dialog.popup.ActionModel
import com.example.baseprojectkotlin_h_c.ui.dialog.popup.ListActionPopup
import com.example.baseprojectkotlin_h_c.utils.ToastUtils
import com.example.baseprojectkotlin_h_c.utils.Utils

class HomeFragment : AbsBaseFragment<FragmentHomeBinding>() {
    private val listActionPopup by lazy { ListActionPopup(requireActivity()) }
    private val actions by lazy {
        val titles = arrayListOf(
            getString(R.string.action_edit),
            getString(R.string.action_delete),
            getString(R.string.action_edit),
            getString(R.string.action_delete)
        )
        val icons = arrayListOf(
            R.drawable.ic_baseline_edit_24,
            R.drawable.ic_baseline_delete_forever_24,
            R.drawable.ic_baseline_edit_24,
            R.drawable.ic_baseline_delete_forever_24
        )
        val actions = mutableListOf<ActionModel>()
        titles.forEachIndexed { index, title ->
            val actionModel = ActionModel(icons[index], title)
            actions.add(actionModel)
        }

        actions
    }

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        binding.myButtonNextScreen.setOnClickListener {
            val action = HomeFragmentDirections.actionGlobalLocationFragment()
            findNavController().navigate(action)
        }
        binding.myButtonStoragePermission.setOnClickListener {
            if (Utils.storagePermissionGrant(requireContext())) {
                setupWhenStoragePermissionGranted()
            } else {
                requestStoragePermission()
            }
        }
        binding.myButtonApiPaging.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalPagingExampleFragment())
        }

        binding.myButtonShowListPopup.setOnClickListener {
            listActionPopup.showPopup(it, actions, object : ActionAdapter.OnActionClickListener {
                override fun onItemActionClick(position: Int) {
                    when (position) {
                        0 -> {
                            ToastUtils.getInstance(requireContext()).showToast("Click on Edit")
                        }
                        else -> {
                            ToastUtils.getInstance(requireContext()).showToast("Click on Delete")
                        }
                    }
                }

            })
        }
        binding.myButtonShapeableImageView.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGlobalShapeImageFragment())
        }

    }

    private fun requestStoragePermission() {
        resultLauncher.launch(
            Utils.getStoragePermissions()
        )
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (Utils.storagePermissionGrant(requireContext())
            ) {
                setupWhenStoragePermissionGranted()
            } else {
                Utils.showAlertPermissionNotGrant(binding.root, requireActivity())
            }
        }

    private fun setupWhenStoragePermissionGranted() {
        val pickImageDlg = PickPhotoDialog.create()
        pickImageDlg.show(parentFragmentManager, "PickImage")
    }

}