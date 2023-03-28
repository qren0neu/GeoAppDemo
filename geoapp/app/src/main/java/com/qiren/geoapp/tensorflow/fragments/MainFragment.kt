package com.qiren.geoapp.tensorflow.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.qiren.geoapp.R
import com.qiren.geoapp.tensorflow.activities.LoginActivity
import com.qiren.geoapp.databinding.MainFragLayoutBinding
import com.qiren.geoapp.tensorflow.services.UserService


class MainFragment : Fragment() {
    private var mainFragBinding: MainFragLayoutBinding? = null
    private val CODE_PICK_RESULT = 123;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainFragBinding = MainFragLayoutBinding.inflate(inflater, container, false)

        return mainFragBinding?.root
    }

    override fun onResume() {
        super.onResume()
        if (UserService.checkLogin()) {
            val user = UserService.getUser()
            mainFragBinding!!.tvWelcome.visibility = View.VISIBLE
            mainFragBinding!!.tvWelcome.text = "Welcome! \n" + user?.email
            mainFragBinding!!.btnLogout.visibility = View.VISIBLE
        } else {
            mainFragBinding!!.tvWelcome.visibility = View.GONE
            mainFragBinding!!.tvWelcome.text = ""
            mainFragBinding!!.btnLogout.visibility = View.GONE
        }
        mainFragBinding?.btnPermission?.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(MainFragmentDirections.actionMainToPermission())
        }
        mainFragBinding?.btnCamera?.setOnClickListener {
            if (!UserService.checkLogin()) {
                val intent = Intent()
                intent.setClass(requireContext(), LoginActivity::class.java)
                requireActivity().startActivity(intent)
                return@setOnClickListener
            }
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                    .navigate(MainFragmentDirections.actionMainToCamera())
        }
        mainFragBinding?.btnGallery?.setOnClickListener {
            if (!UserService.checkLogin()) {
                val intent = Intent()
                intent.setClass(requireContext(), LoginActivity::class.java)
                requireActivity().startActivity(intent)
                return@setOnClickListener
            }
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            requireActivity().startActivityForResult(intent, CODE_PICK_RESULT)
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), CODE_PICK_RESULT)
        }
        mainFragBinding?.btnLogout?.setOnClickListener {
            UserService.logout()
            onResume()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_PICK_RESULT) {
            // picked up
            val imageUri = data?.data
//            val action = MainFragmentDirections.actionMainToImage()
            val bundle = Bundle()
            bundle.putString("imageUri", imageUri.toString())
            findNavController().navigate(R.id.action_main_to_image, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainFragBinding = null
    }
}