package cn.com.zzndb.wallpaper.presenter

import android.widget.ImageView
import cn.com.zzndb.wallpaper.domain.commands.getBingUrl
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

    override fun getImageUrl() : String {
//        return getBingUrl(mView.getWidth(), mView.getHeight()).url
        return getNGChinaUrl().url
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
