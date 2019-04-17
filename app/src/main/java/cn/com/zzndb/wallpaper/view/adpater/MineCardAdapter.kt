package cn.com.zzndb.wallpaper.view.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import cn.com.zzndb.wallpaper.R
import cn.com.zzndb.wallpaper.domain.model.ImageCard
import cn.com.zzndb.wallpaper.view.MineFragment
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find


/**
 * Mine Fragment Card Adapter
 */
class MineCardAdapter(private val mineFragment: MineFragment, private val context: Context, private val imageCards: List<ImageCard>) : RecyclerView.Adapter<MineCardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var cardView: CardView? = null
        var imageView: ImageView? = null
//        var headText: TextView? = null
        var imageText: TextView? = null

        init {
            cardView = view as CardView
            imageView = view.find(R.id.card_image)
//            headText = view.find(R.id.card_head)
            imageText = view.find(R.id.card_sname)
        }
    }

    override fun getItemCount(): Int {
        return imageCards.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.mine_view_card, parent, false)
        val holder = ViewHolder(view)
        holder.imageView!!.setOnClickListener {
            val position = holder.adapterPosition
            val imageCard = mineFragment.getList()[position]
            mineFragment.imagePreView(listOf(imageCard.uri), imageCard.sName, holder.imageView!!, imageCard.date)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageCard = imageCards[position]
        holder.imageText!!.text = imageCard.sName
//        holder.headText!!.text = imageCard.headDate
        Picasso.get().load("file://${imageCard.uri}").into(holder.imageView)
    }
}


