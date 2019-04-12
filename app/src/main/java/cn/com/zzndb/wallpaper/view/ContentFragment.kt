package cn.com.zzndb.wallpaper.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import cn.com.zzndb.wallpaper.R
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find


/**
 * Image content fragment
 */
class ContentFragment : Fragment(), IFragmentView {

    private lateinit var image: ImageView
    private lateinit var mView: IView
    private var tStr: String = ""

    private var processBar: ProgressBar? = null
    private var imageView: ImageView? = null

    fun settStr(str: String) {
        tStr = str
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frgment_view, container, false) as View
        image = view.find(R.id.imageshow)
        mView = activity as IView
        processBar = view.find(R.id.imageview_process) as ProgressBar
        imageView = view.find(R.id.imageshow) as ImageView
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.d("test mes", "image init" + processBar?.height + "," + processBar?.width)

        // load image
        val fView = this
        if (tStr !=  "") doAsync {
            mView.showImage(tStr, image, fView)
        }
        mView.showMes("$tStr image loading")
        Log.d("test mes", tStr)
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

    // tmp get imageView size from full size processBar :)
    override fun height(): Int = processBar!!.height

    override fun width(): Int = processBar!!.width

    // check current fragment
    override fun gettStr(): String {
        return tStr
    }

    override fun getImageView(): ImageView {
        return imageView!!
    }
}
