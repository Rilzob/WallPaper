package cn.com.zzndb.wallpaper.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Environment.*
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.FileProvider
import cn.com.zzndb.wallpaper.R
import cn.com.zzndb.wallpaper.presenter.PresenterImpl
import kotlinx.android.synthetic.main.imageview_overlay.view.*
import java.io.File

/**
 * preview overlay
 */
@SuppressLint("ViewConstructor")
class OverlayView constructor(
    context: Context,
    description: String
) : RelativeLayout(context) {
    init {
        View.inflate(context, R.layout.imageview_overlay, this)
        setBackgroundColor(Color.TRANSPARENT)
        Text.text = description
    }

    fun update(presenter: PresenterImpl, uri: String) {
        Text.setOnClickListener {
            Log.d("test Ov", "overlay clicked")
        }

        setWallpaper.setOnClickListener {
            val image = BitmapFactory.decodeFile(uri)
            presenter.setWallpaper(image)
            presenter.mView.showMes("wallpaper set done!")
        }

        shareImage.setOnClickListener {
            val fUri = FileProvider.getUriForFile(
                context,
                "cn.com.zzndb.wallpaper.provider",
                File(uri)
            )
            presenter.sendShareIntent(fUri)
        }

        saveImage.setOnClickListener {
//            Log.d("test dir", context.filesDir.toString())
//            Log.d("test dir", getExternalStorageDirectory().toString())
//            Log.d("test dir", getExternalStoragePublicDirectory(DIRECTORY_PICTURES).toString())
            presenter.saveImage(uri)

        }
    }
}