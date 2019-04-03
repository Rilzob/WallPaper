package cn.com.zzndb.wallpaper.presenter

import android.widget.ImageView
import cn.com.zzndb.wallpaper.domain.commands.getBingUrl
import cn.com.zzndb.wallpaper.domain.commands.getNASAUrl
import cn.com.zzndb.wallpaper.domain.commands.getNGChinaUrl
import cn.com.zzndb.wallpaper.view.IView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * the impliment of IPresenter
 * connect model and view
 */
class PresenterImpl(val mView: IView) : IPresenter {

    override fun getImageUrl(str: String) : String {
        return when(str) {
            "Bing" -> getBingUrl(mView.getWidth(), mView.getHeight(), mView).url
            "Ng"   -> getNGChinaUrl().url
            "Nasa" -> getNASAUrl().url
            else -> {
                "https://zzndb.com.cn/3.png"
            }
        }
    }

    override fun loadImage(url: String, image: ImageView) {
        doAsync {
            uiThread {
                Picasso.get()
                    .load(url)
                    .resize(image.width, image.height)
                    .centerCrop()
                    .into(image)
            }
        }
    }

}
