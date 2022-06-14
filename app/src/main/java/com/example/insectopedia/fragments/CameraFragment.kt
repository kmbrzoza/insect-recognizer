package com.example.insectopedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.insectopedia.R
import com.example.insectopedia.factories.CameraViewModelFactory
import com.example.insectopedia.viewmodels.CameraViewModel

class CameraFragment : Fragment() {
    private lateinit var viewModel: CameraViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = CameraViewModelFactory(requireNotNull(this.activity).application)
        viewModel = ViewModelProvider(this, factory).get(CameraViewModel::class.java)

        startCamera()

        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            backToMainFragment()
        }

        view.findViewById<Button>(R.id.camera_button_back).apply {
            setOnClickListener {
                backToMainFragment()
            }
        }

        view.findViewById<Button>(R.id.camera_button_take_picture).apply {
            setOnClickListener {
                this.isClickable = false
                viewModel.savePicture()
            }
        }

        viewModel.newImagePath.observe(viewLifecycleOwner) {
            if (it != null) {
                val bundle = bundleOf(Pair("imagePath", it))
                viewModel.newImagePath.value = null

                // TODO GO TO NEW VIEW
            }
        }
    }

    private fun backToMainFragment() {
        view?.findNavController()?.navigate(R.id.action_cameraFragment_to_mainFragment)
    }

    private fun startCamera() {
        val futureCameraProvider = ProcessCameraProvider.getInstance(requireContext())

        futureCameraProvider.addListener({
            val cameraProvider: ProcessCameraProvider = futureCameraProvider.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(
                    view?.findViewById<PreviewView>(R.id.view_finder)?.surfaceProvider
                )
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, CameraSelector.DEFAULT_BACK_CAMERA, preview, viewModel.imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Camera Provider binding failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }
}