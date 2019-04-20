package cn.com.zzndb.wallpaper.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import cn.com.zzndb.wallpaper.R
import cn.com.zzndb.wallpaper.presenter.PresenterImpl
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find




/**
 * Image content fragment
 */
class ContentFragment : Fragment(), IContentFragmentView {

    private lateinit var image: ImageView
    private lateinit var mView: IView
    private var tStr: String = ""

    private var processBar: ProgressBar? = null
    private var imageView: ImageView? = null

    private var presenter: PresenterImpl? = null

//    private var height: Int = 0
//    private var width: Int = 0

    fun settStr(str: String) {
        tStr = str
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view, container, false) as View
        // get fragment h&w from its layout
//        val myLayout = view.findViewById(R.id.fragment_layout) as RelativeLayout
//        myLayout.viewTreeObserver.addOnGlobalLayoutListener{
//            if (height == 0 || width == 0) {
//                height = myLayout.height
//                width = myLayout.width
//            }
//        }
        image = view.find(R.id.imageshow)
        mView = activity as IView
        processBar = view.find(R.id.imageview_process) as ProgressBar
        imageView = view.find(R.id.imageshow) as ImageView
        imageView!!.setOnClickListener { imagePreView() }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // load image
        val fView = this
        if (tStr !=  "") doAsync {
            mView.showImage(tStr, image, fView, false)
        }
//        mView.showMes("$tStr image loading", 0)
    }

    override fun hideProcessBar() {
        processBar!!.visibility = View.GONE
    }

    override fun showProcessBar() {
        processBar!!.visibility = View.VISIBLE
    }

    override fun hideImageView() {
        imageView!!.visibility = View.GONE
    }

    override fun showImageVIew() {
        imageView!!.visibility = View.VISIBLE
    }

//    // tmp get imageView size from full size processBar :)
//    override fun height(): Int {
//        Log.d("test fragmentSize", "$height x $width")
//        return this.height
//    }
//
//    override fun width(): Int = this.width

    // check current fragment
    override fun gettStr(): String {
        return tStr
    }

    override fun getImageView(): ImageView {
        return imageView!!
    }

    override fun imagePreView() {
        // frg may wrong ... not loaded image touch, then choose other fragment
//        val frg = mView.getCurrentFrag() as ContentFragment
        // TODO load image if exist not just yesterday
        presenter = mView.getPresenter()
        val overlayView = OverlayView(context!!, "Today's ${this.gettStr()} Image")
        val uris = listOf<String>(
            presenter!!.getCurrentPic(this.gettStr())
        )
        overlayView.update(presenter!!, uris[0])
        StfalconImageViewer.Builder<String>(context, uris) { imageView, uri ->
            Picasso.get().load("file://$uri").into(imageView)
        }.withTransitionFrom(imageView).withOverlayView(overlayView).show()

    }
}
