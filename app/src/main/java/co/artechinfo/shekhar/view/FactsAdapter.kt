package co.artechinfo.shekhar.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.artechinfo.shekhar.R
import co.artechinfo.shekhar.model.Fact
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FactsAdapter(private val factList: List<Fact>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: Context? = null

    var requestOptions = RequestOptions()
        .placeholder(R.mipmap.ic_launcher)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.mContext = parent.context
        return FactViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_fact, parent, false)
        )
    }

    override fun getItemCount() = factList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mFact = factList[position]
        val factViewHolder = holder as FactViewHolder
        factViewHolder.tvTitle.text = when (mFact.title.isNullOrBlank()) {
            true -> factViewHolder.tvTitle.context.resources.getString(R.string.na)
            else -> mFact.title
        }
        factViewHolder.tvDescription.text = when (mFact.description.isNullOrBlank()) {
            true -> factViewHolder.tvDescription.context.resources.getString(R.string.na)
            else -> mFact.description
        }

        Glide.with(factViewHolder.itemView.context)
            .setDefaultRequestOptions(requestOptions)
            .load(mFact.imageHref)
            .into(factViewHolder.ivImageHref)
    }

    class FactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val ivImageHref: ImageView = itemView.findViewById(R.id.ivImageHref)
    }

}