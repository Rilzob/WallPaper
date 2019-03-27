package cn.com.zzndb.wallpaper.presenter

import android.widget.ImageView
import cn.com.zzndb.wallpaper.domain.commands.getBingUrl
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * the impliment of IPresenter
 * connect model and view
 */
class PresenterImpl() : IPresenter {

    override fun getImageUrl() : String {
        return getBingUrl().url
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
