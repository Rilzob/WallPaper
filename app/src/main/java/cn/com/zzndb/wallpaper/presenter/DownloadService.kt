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
    lateinit var image: ImageView
    lateinit var fView: ContentFragment
    lateinit var cacheUri: String
    lateinit var sName: String

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
            presenter!!.loadImage(cacheUri, image, fView)
//            stopForeground(true)
//            notificationManager.notify(1, getNotification("Download Success", -1))
            Log.d("test download", "success")
        }

        override fun onFailed() {
            downloadImage = null
            presenter!!.loadImage(cacheUri, image, fView)
//            stopForeground(true)
//            notificationManager.notify(1, getNotification("Download Failed", -1))
            Log.d("test download", "failed")
        }
    }

    private var mBinder = DownloadBinder()

//    private val notificationManager: NotificationManager
//        get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun onBind(intent: Intent) : IBinder {
        return mBinder
    }

    inner class DownloadBinder : Binder() {

        fun startDownload(str: String, presenter: PresenterImpl, image: ImageView, fView: ContentFragment) {
            if (downloadImage == null) {
                this@DownloadService.presenter = presenter
                this@DownloadService.image = image
                this@DownloadService.fView = fView
                this@DownloadService.sName = str
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
//                startForeground(1, getNotification("Downloading...", 0))
//                Toast.makeText(this@DownloadService, "Downloading...", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun getNotification(title: String, progress: Int): Notification { val intent = Intent(this, MainActivity::class.java)
//        val pi = PendingIntent.getActivity(this, 0, intent, 0)
//        val builder = NotificationCompat.Builder(this)
//        builder.setSmallIcon(R.mipmap.ic_launcher)
//        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
//        builder.setContentIntent(pi)
//        builder.setContentTitle(title)
//        if (progress >= 0) {
//            // 当progress大于或等于0时才需显示下载进度
//            builder.setContentText("$progress%")
//            builder.setProgress(100, progress, false)
//        }
//        return builder.build()
//    }
}

