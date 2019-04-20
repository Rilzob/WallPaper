package cn.com.zzndb.wallpaper.view


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.com.zzndb.wallpaper.R
import cn.com.zzndb.wallpaper.domain.model.ImageCard
import cn.com.zzndb.wallpaper.presenter.PresenterImpl
import cn.com.zzndb.wallpaper.view.adpater.MineCardAdapter
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import org.jetbrains.anko.find

/**
 * mine fragment
 * show daily cached images
 */
class MineFragment : Fragment(), IMineFragmentView {

    private lateinit var mView: IView

    private var presenter: PresenterImpl? = null

    private var adapter: MineCardAdapter? = null

    private var imageList: List<ImageCard>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mine, container, false) as View

        mView = activity as IView
        presenter = mView.getPresenter()

        val recyclerView = view.find(R.id.mineRecyclerView) as RecyclerView
        val layoutManager = GridLayoutManager(view.context, 3)
        recyclerView.layoutManager = layoutManager
        getImageList()

        adapter = MineCardAdapter(this, view.context, imageList!!)
        recyclerView.adapter = adapter

        return view
    }

    // get cached image list from database
    private fun getImageList() {
        imageList = presenter!!.getImageCards()
    }

    // mine fragment imagePreView
    override fun imagePreView(uris: List<String>, tStr: String, image: ImageView, date: String) {
        // TODO add continue list preview for mineFragment
        val overlayView = OverlayView(context!!, "$date's $tStr Image")
        overlayView.update(presenter!!, uris[0])
        StfalconImageViewer.Builder<String>(context, uris) { imageView, uri ->
            Picasso.get().load("file://$uri").into(imageView)
        }.withTransitionFrom(image).withOverlayView(overlayView).show()

    }

    // for adapter get current image card
    override fun getList() : List<ImageCard> {
        return imageList!!
    }

    // refresh mine fragment & check item add
    override fun updateMineFragment() {
        val oSize = imageList!!.size
        getImageList()
        val nSize = imageList!!.size
        if (nSize > oSize) {
            adapter!!.updateItem(imageList!!)
            adapter!!.notifyItemInserted(nSize - 1)
            adapter!!.notifyItemRangeChanged(nSize - 1, imageList!!.size)
        }
        adapter!!.notifyDataSetChanged()
        // TODO reDownload the image if the image not in app file dir
    }

    // delete item
    override fun deleteImage(uri: String, postion: Int) {
        val builder = AlertDialog.Builder(mView.getContext())
        builder.setMessage(R.string.minefrg_dialog_message)
            .setTitle(R.string.minefrg_dialog_title)
        builder.apply {
            setPositiveButton(R.string.minefrg_dialog_ok
            ) { _, _ ->
                mView.getPresenter().deleteImage(uri)
                getImageList()
                adapter!!.updateItem(imageList!!)
                adapter!!.notifyItemRemoved(postion)
                adapter!!.notifyItemRangeChanged(postion, imageList!!.size)
            }
            setNegativeButton(R.string.minefrg_dialog_cancel, null)
        }
        val dialog = builder.create()
        dialog.show()
    }
}
