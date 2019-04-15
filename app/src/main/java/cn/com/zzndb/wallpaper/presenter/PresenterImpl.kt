package cn.com.zzndb.wallpaper.presenter

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment.*
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import cn.com.zzndb.wallpaper.R
import cn.com.zzndb.wallpaper.domain.commands.getBingUrl
import cn.com.zzndb.wallpaper.domain.commands.getNASAUrl
import cn.com.zzndb.wallpaper.domain.commands.getNGChinaUrl
import cn.com.zzndb.wallpaper.domain.db.PicDb
import cn.com.zzndb.wallpaper.view.ContentFragment
import cn.com.zzndb.wallpaper.view.IView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * the implement of IPresenter
 * connect model and view
 */
class PresenterImpl(val mView: IView, val picDb: PicDb) : IPresenter {

    // return image url
    override fun getImageUrl(str: String) : String {
        return when(str) {
            "Bing" -> getBingUrl(mView.getWidth() + 400, mView.getHeight() + 600, mView).url
            "Ng"   -> getNGChinaUrl().url
            "Nasa" -> getNASAUrl().url
            else -> {
                "https://zzndb.com.cn/3.png"
            }
        }
    }

    // download image or load from external storage
    override fun downImage(str: String, image: ImageView, fView: ContentFragment) {
        // get today pic uri if exist
        val uri = getTodayPic(str)
        if (uri != str) {
            loadImage(uri, image, fView)
        }
        else {
            // wait service binding
            while (mView.getDBinder() == null);
            mView.getDBinder()!!.startDownload(str, this, image, fView)
        }
    }

    // swipe refresh load image from internet
    override fun downImage(str: String, image: ImageView, fView: ContentFragment, force: Boolean) {
        mView.getDBinder()!!.startDownload(str, this, image, fView)
        mView.showMes("update done!")
    }

    // show image
    override fun loadImage(uri: String, image: ImageView, fView: ContentFragment) {
        mView.showMes("try loading image locally")
        doAsync {
            uiThread {
                Picasso.get()
                    .load("file://$uri")
                    // there may cause random bug: java.lang.IllegalArgumentException: At least one dimension has to be positive number.
                    .resize(fView.width(), fView.height())
                    .centerCrop()
                    .into(image, object : Callback {
                        override fun onError(e: Exception?) {
                            fView.hideProcessBar()
                            mView.showMes("Picasso loading image failed!")
                        }

                        override fun onSuccess() {
                            fView.showImageVIew()
                            fView.hideProcessBar()
                        }

                    })
            }
        }
    }

    // return image name from url
    override fun getIName(url: String): String {
        // parse url to get file name or just Name the file cache
        val regex = "[0-9a-zA-Z_?-]*.(jpg|jpeg|png)".toRegex()
        val result = regex.find(url)?.value
        if (result != null) {
            return result.toString()
        }
        else {
            mView.showMes("parse url get file name error, maybe no pic today!\nloading yesterdays pic")
            return ""
        }
    }

    // get date - amount
    @SuppressLint("SimpleDateFormat")
    private fun getDate(amount: Int) : String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -amount)
        return SimpleDateFormat("yyyy-MM-dd").format(cal.time)
    }

    // save cache info only once a day a image
    override fun cacheTodayPicInfo(sName: String, fName: String) {
        val date = getDate(0)
        // save once a pic
        if (!picDb.checkExist(fName)) {
            picDb.saveDailyPicInfo(date, sName, fName)
        }
    }

    // return str or uri
    @SuppressLint("SimpleDateFormat")
    override fun getTodayPic(str: String) : String {
        val date = getDate(0)
        return picDb.checkTodayPicInfo(date, str)
    }

    // get current showing pic uri
    override fun getCurrentPic(str: String): String {
        val date = getDate(0)
        val bdate = getDate(1)
        return picDb.getCurrentPicUri(date, str, bdate)
    }

    override fun sendShareIntent(uri: Uri) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
        }
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(mView.getContext(),
            Intent.createChooser(sendIntent, mView.getContext().resources.
                getText(R.string.send_to)), Bundle()
        )
    }

    override fun setWallpaper(image: Bitmap) {
        val mwallpapermanager = WallpaperManager.getInstance(mView.getContext())
        mwallpapermanager.setBitmap(image)
    }

    override fun saveImage(uri: String) {
        if (checkWFPermission()) {
            val imagePath = File(
                getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
                , mView.getContext().resources.getString(R.string.app_name)
            )
//            val fileName = File("$imagePath/${uri.substringAfterLast("/")}")
//            if (!fileName.parentFile.exists()) {
//                fileName.parentFile.mkdir()
//            }
            File(uri.substringBeforeLast("/"), uri.substringAfterLast("/"))
                .copyTo(File(imagePath, uri.substringAfterLast("/")), true)
            mView.showMes("Save Image Done!")
        }
        else {
            mView.showMes("Save Failed!\nPermission Denied!")
        }
    }

    override fun checkWFPermission(): Boolean {
        if (!mView.checkWFPermission()) {
            mView.requestWFPermission()
        }
        return mView.checkWFPermission()
    }

}
