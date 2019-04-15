package cn.com.zzndb.wallpaper.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.com.zzndb.wallpaper.R

/**
 * mine fragment
 */
class MineFragment : Fragment() {
    // TODO create imageAdapter to load image show in the fragment
    // maybe did it after image save

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mine, container, false) as View
        return view
    }

}
