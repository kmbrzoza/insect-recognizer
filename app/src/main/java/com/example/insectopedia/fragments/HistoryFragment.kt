package com.example.insectopedia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insectopedia.R
import com.example.insectopedia.viewmodels.HistoryViewModel
import pl.polsl.insectopedia.adapters.InsectsAdapter

class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = HistoryViewModel((requireNotNull(this.activity).application))
        val insectsAdapter = InsectsAdapter(viewModel, this.context)

        viewModel.insects.observe(viewLifecycleOwner) {
            insectsAdapter.notifyDataSetChanged()
            if (insectsAdapter.itemCount >0 ) {
                view.findViewById<TextView>(R.id.empty_history).text = ""
            } else {
                view.findViewById<TextView>(R.id.empty_history).text = "Insect history is empty"
            }
        }


        val layoutManager = LinearLayoutManager(view.context)
        view.findViewById<RecyclerView>(R.id.history_recyclerView).let {
            it.adapter = insectsAdapter
            it.layoutManager = layoutManager
        }
    }
}