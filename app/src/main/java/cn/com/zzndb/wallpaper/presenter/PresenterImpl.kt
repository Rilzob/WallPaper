package cn.com.zzndb.wallpaper.presenter

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import cn.com.zzndb.wallpaper.domain.model.ImageCard
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
        val sharePreference = mView.getSharedPreference()
        if (!sharePreference.getBoolean("saveOrigin", false)) {
            return when (str) {
                "Bing" -> getBingUrl(mView).url
                "Ng" -> getNGChinaUrl().url
                "Nasa" -> getNASAUrl().url
                else -> {
                    "https://zzndb.com.cn/3.png"
                }
            }
        }
        else {
            return when (str) {
                "Bing" -> getBingUrl(mView).oriUrl
                // TODO replace the following oriUrl func
                "Ng" -> getNGChinaUrl().url
                "Nasa" -> getNASAUrl().url
                else -> {
                    "https://zzndb.com.cn/3.png"
                }
            }
        }
    }

    // swipe refresh load image from internet
    // download image or load from external storage
    override fun downImage(str: String, image: ImageView, fView: ContentFragment, force: Boolean) {
        if (force) {
            if (checkNetConnection()) {
                mView.getDBinder()!!.startDownload(str, this, image, fView)
                mView.showMes("update done!", 0)
            }
            else {
                fView.hideProcessBar()
                fView.showImageVIew()
                mView.showMes("Please check your net connection!", 1)
            }
        }
        else {
            // get today pic uri if exist
            val uri = getTodayPic(str)
            if (uri != str) {
                loadImage(uri, image, fView)
            } else {
                if (checkNetConnection()) {
                    // wait service binding
                    while (mView.getDBinder() == null);
                    mView.getDBinder()!!.startDownload(str, this, image, fView)
                }
                else {
                    fView.hideProcessBar()
                    fView.showImageVIew()
                    mView.showMes("Please check your net connection!", 1)
                }

            }
        }
    }

    // show image
    override fun loadImage(uri: String, image: ImageView, fView: ContentFragment) {
//        mView.showMes("try loading image locally")
        doAsync {
            uiThread {
                Picasso.get()
                    .load("file://$uri")
                    .into(image, object : Callback {
                        override fun onError(e: Exception?) {
                            fView.hideProcessBar()
                            mView.showMes("Picasso loading image failed!", 1)
                        }

                        override fun onSuccess() {
                            fView.showImageVIew()
                            fView.hideProcessBar()
                        }

                    })
            }
            // check setting whether save image file
            if (mView.getSharedPreference().getBoolean("saveImage", false))
                saveImage(uri)
        }
    }

    // return image name from url
    override fun getIName(url: String): String {
        // parse url to get file name or just Name the file cache
        val regex = "[0-9a-zA-Z_?-]*.(jpg|jpeg|png)".toRegex()
        val result = regex.find(url)?.value
        return if (result != null) {
            result.toString()
        }
        else {
            mView.showMes("parse url get file name error, maybe no pic today!\nloading yesterdays pic", 1)
            ""
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
    override fun cacheTodayPicInfo(sName: String, fName: String, url: String) {
        val date = getDate(0)
        // save once a pic
        if (!picDb.checkExist(fName)) {
            picDb.saveDailyPicInfo(date, sName, fName, url)
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
            val str = dbgettStr(uri)
            val date = dbgetDate(uri)
            // save file like 'date_tstr_name'
            val outFile = File("$imagePath" ,
                "${date}_${str}_${uri.substringAfterLast("/")}")
            if (!outFile.exists()) {
                File(uri.substringBeforeLast("/"), uri.substringAfterLast("/"))
                    .copyTo(outFile, true)
                mView.showMes("Save Image Done!", 0)
            }
        }
        else {
            mView.showMes("Save Failed!\nPermission Denied!", 1)
        }
    }

    override fun checkWFPermission(): Boolean {
        if (!mView.checkWFPermission()) {
            mView.requestWFPermission()
        }
        return mView.checkWFPermission()
    }

    // query database get all image info
    override fun getImageCards(): List<ImageCard> {
        return picDb.getDbImageCards(getDate(0))
    }

    override fun dbgettStr(fName: String): String {
        return picDb.getDbtStr(fName)
    }

    override fun dbgetDate(fName: String): String {
        return picDb.getDbDate(fName)
    }

    override fun changeWallpaper() {
        // if exist set or load from net
        val preference = mView.getSharedPreference()
        // auto set or not
        if (preference.getBoolean("autoChange", false)) {
            var tStr = preference.getString("wallpaperSource", "0")!!
            // decode from setting preference value
            if (tStr == "0") tStr = "Bing"
            if (tStr == "1") tStr = "Ng"
            if (tStr == "2") tStr = "Nasa"
            setWallpaper(BitmapFactory.decodeFile(getCurrentPic(tStr)))
//            val uri = getTodayPic(tStr)
//            if (uri != tStr)
//                setWallpaper(BitmapFactory.decodeFile(uri))
//            else {
//                if (checkNetConnection()) {
//                    // try loading new image
//                    mView.getDBinder()!!.cacheImageOnly(tStr, this)
//                }
//                mView.showMes("Please check your network!")
//            }
        }
    }

    // because of problem, move to MainActivity
    override fun checkNetConnection(): Boolean {
        return mView.checkNetConnection()
    }

    // delete image from database & local cache
    override fun deleteImage(uri: String) {
        mView.showMes("delete done!", 0)
        picDb.deleteImage(uri)
        val file = File(uri)
        if (file.exists())
            file.delete()
    }
}
