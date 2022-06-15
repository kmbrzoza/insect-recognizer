package com.example.insectopedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.insectopedia.R
import com.example.insectopedia.factories.FindInsectViewModelFactory
import com.example.insectopedia.viewmodels.FindInsectViewModel
import com.google.android.gms.tasks.Task
import com.google.gson.JsonElement

class FindInsectFragment : Fragment() {
    private lateinit var viewModel: FindInsectViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val imagePath = arguments?.getString("imagePath")

        if (imagePath == null) {
            Toast.makeText(activity, "Image path is missing!", Toast.LENGTH_LONG).show()
            backToCameraFragment()
            return null
        }

        val factory =
            FindInsectViewModelFactory((requireNotNull(this.activity).application), imagePath)
        viewModel = ViewModelProvider(this, factory).get(FindInsectViewModel::class.java)

        return inflater.inflate(R.layout.fragment_find_insect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonBack = view.findViewById<Button>(R.id.find_insect_back).apply {
            setOnClickListener {
                backToCameraFragment()
            }
        }

        val response = viewModel.processImage()

        if (response == null) {
            Toast.makeText(activity, "Image does not exist!", Toast.LENGTH_LONG).show()
            backToCameraFragment()
        } else {
            buttonBack.isClickable = false
            processResponse(response)
            buttonBack.isClickable = true
        }
    }

    private fun backToCameraFragment() {
        view?.findNavController()?.navigate(R.id.action_findInsectFragment_to_cameraFragment)
    }

    private fun processResponse(response: Task<JsonElement>) {
        response.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val insect = viewModel.processResult(task.result!!)

                if (insect == null) {
                    // TODO Show info about unrecognized insect

                    viewModel.deleteImage()
                } else {
                    // TODO Show info about recognized insect

                }
            } else {
                Toast.makeText(activity, "Error sending picture.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
