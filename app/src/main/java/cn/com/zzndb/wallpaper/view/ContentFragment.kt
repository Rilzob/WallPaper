package cn.com.zzndb.wallpaper.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import cn.com.zzndb.wallpaper.R
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find


class ContentFragment : Fragment()  {

    // TODO deal mine fragment
    private lateinit var image: ImageView
    private lateinit var mActivity: MainActivity
    private var tStr: String = ""

    fun settStr(str: String) {
        tStr = str
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frgment_view, container, false) as View
        image = view.find(R.id.imageshow)
        mActivity = activity as MainActivity
        Log.d("test mes", "image init")
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (tStr !=  "") doAsync {
            mActivity.showImage(tStr, image)
        }
        Toast.makeText(mActivity, tStr + " image loaded", Toast.LENGTH_SHORT).show()
        Log.d("test mes", tStr)
    }
}
