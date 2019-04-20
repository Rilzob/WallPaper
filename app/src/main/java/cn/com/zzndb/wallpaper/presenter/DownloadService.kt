package cn.com.zzndb.wallpaper.presenter

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Environment
import android.os.IBinder
import android.util.Log
import android.widget.ImageView
import cn.com.zzndb.wallpaper.domain.DownloadImage
import cn.com.zzndb.wallpaper.view.ContentFragment

class DownloadService : Service() {
    private var presenter: PresenterImpl? = null
    var image: ImageView? = null
    var fView: ContentFragment? = null
    lateinit var cacheUri: String
    lateinit var sName: String
    var justCache = false

    private var downloadImage: DownloadImage? = null
    private var downloadUrl: String? = null
    private var listener = object : DownloadListener {

        override fun onProgress(progress: Int) {
//            notificationManager.notify(1, getNotification("Downloading...", progress))
//            Log.d("test download", "Downloading....")
        }

        override fun onSuccess() {
            downloadImage = null
            presenter!!.cacheTodayPicInfo(sName, cacheUri, downloadUrl!!)
            if (!justCache)
                presenter!!.loadImage(cacheUri, image!!, fView!!)
            else {
                justCache = false
                if (presenter!!.picDb.checkExist(cacheUri)) {
                    // update image success then change
                    presenter!!.changeWallpaper()
                }
            }
            Log.d("test download", "success")
        }

        override fun onFailed() {
            downloadImage = null
            if (!justCache)
                presenter!!.loadImage(cacheUri, image!!, fView!!)
            else {
                justCache = false
            }
            Log.d("test download", "failed")
        }
    }

    private var mBinder = DownloadBinder()


    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    inner class DownloadBinder : Binder() {

        fun startDownload(str: String, presenter: PresenterImpl, image: ImageView, fView: ContentFragment) {
            if (downloadImage == null) {
                this@DownloadService.presenter = presenter
                this@DownloadService.image = image
                this@DownloadService.fView = fView
                this@DownloadService.sName = str
                download(str, presenter)
            }
        }

        // what a shit code.. :(
        fun cacheImageOnly(str: String, presenter: PresenterImpl) {
            if (downloadImage == null) {
                this@DownloadService.presenter = presenter
                this@DownloadService.sName = str
                this@DownloadService.justCache = true
                download(str, presenter)
            }
        }

        // real download
        private fun download(str: String, presenter: PresenterImpl) {
            downloadUrl = presenter.getImageUrl(str)
            val fName = presenter.getIName(downloadUrl!!)
            // try to load Nasa yesterdays image if current web not a image file,
            // this maybe cause more issue, but who care :(
            cacheUri = if (fName != "")
                "${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/$fName"
            else
                presenter.getCurrentPic(str) // load from database
            downloadImage = DownloadImage(listener, cacheUri)
            downloadImage!!.execute(downloadUrl)
        }
    }
}

