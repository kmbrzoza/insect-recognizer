package com.example.insectopedia.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.insectopedia.R
import com.example.insectopedia.factories.FindInsectViewModelFactory
import com.example.insectopedia.viewmodels.FindInsectViewModel
import com.google.android.gms.tasks.Task
import com.google.gson.JsonElement
import android.text.style.UnderlineSpan
import android.text.SpannableString

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

        if (viewModel.imageExists()) {
            view.findViewById<ImageView>(R.id.find_insect_image)
                .setImageBitmap(BitmapFactory.decodeFile(viewModel.imagePath))
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
                    view?.findViewById<TextView>(R.id.find_insect_name)?.text =
                        context?.resources?.getString(R.string.unrecognized_insect)
                    viewModel.deleteImage()
                } else {
                    view?.findViewById<TextView>(R.id.find_insect_name)?.text = insect.name

                    val wiki = view?.findViewById<TextView>(R.id.find_insect_wiki)
                    val content = SpannableString(context?.resources?.getString(R.string.wiki))
                    content.setSpan(UnderlineSpan(), 0, content.length, 0)
                    wiki?.text = content

                    wiki?.setOnClickListener {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(insect.wikiURL))
                        context?.startActivity(browserIntent)
                    }
                }
            } else {
                Toast.makeText(activity, "Error sending picture.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
