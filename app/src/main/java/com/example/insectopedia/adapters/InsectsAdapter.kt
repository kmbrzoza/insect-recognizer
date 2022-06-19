package pl.polsl.insectopedia.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.insectopedia.R
import com.example.insectopedia.viewmodels.HistoryViewModel
import java.io.File

class InsectsAdapter(private val viewModel: HistoryViewModel,
                     private val context: Context?)
    : RecyclerView.Adapter<InsectsAdapter.InsectsListHolder>() {

    inner class InsectsListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewInsectName: TextView = view.findViewById(R.id.insect_name)
        val textViewWikiLink: TextView = view.findViewById(R.id.insect_wiki)
        val textViewInsectDate: TextView = view.findViewById(R.id.insect_date)
        val imageViewInsect: ImageView = view.findViewById(R.id.insect_photo)
        val buttonViewInsectOptions: TextView = view.findViewById(R.id.insect_options)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsectsListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_history, parent, false)
        return InsectsListHolder(view)
    }

    override fun onBindViewHolder(holder: InsectsListHolder, position: Int) {
        val path = viewModel.insects.value?.get(position)?.photoPath
        if (!checkIfPhotoExists(path!!)) {
            viewModel.delete(viewModel.insects.value?.get(position)!!)
            return
        }
        holder.imageViewInsect.setImageBitmap(BitmapFactory.decodeFile(path))
        holder.textViewInsectName.text = viewModel.insects.value?.get(position)?.name
        holder.textViewInsectDate.text = viewModel.insects.value?.get(position)?.discoveryDate
        holder.textViewWikiLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.insects.value?.get(position)?.wikiURL))
            context?.startActivity(browserIntent)
        }

        holder.buttonViewInsectOptions.setOnClickListener {
            val popup = PopupMenu(context, holder.buttonViewInsectOptions)
            popup.inflate(R.menu.history_options_menu)
            popup.setOnMenuItemClickListener { item: MenuItem? ->
                when (item!!.itemId) {
                    R.id.insect_delete -> {
                        viewModel.delete(viewModel.insects.value?.get(position)!!)
                    }
                }
                true
            }
            popup.show()
        }
    }

    fun checkIfPhotoExists(path: String): Boolean {
        val file = File(path)
        return file.exists()
    }

    override fun getItemCount(): Int {
        return viewModel.insects.value?.size ?: 0
    }
}