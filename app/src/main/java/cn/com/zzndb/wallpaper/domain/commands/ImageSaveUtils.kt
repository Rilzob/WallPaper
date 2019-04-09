package cn.com.zzndb.wallpaper.domain.commands

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import cn.com.zzndb.wallpaper.view.MainActivity
import cn.com.zzndb.wallpaper.view.search
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ImageSaveUtils {

    companion object {   //var bitmap: Bitmap?=null
  fun returnBitMap(url: String): Bitmap {

  //      Thread(Runnable {
            var imageurl: URL = URL(url)

            try {
                imageurl = URL(url)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

 //           try {
                val conn = imageurl.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.connect()
                val `is` = conn.inputStream
            val bitmap: Bitmap = BitmapFactory.decodeStream(`is`)
                `is`.close()
 //           } catch (e: IOException) {
 //               e.printStackTrace()
 //           }
  //      }).start()

        return bitmap
    }


        fun saveImageToGallery(bmp: Bitmap,context: Context): Boolean {
            // 保存图片目录 （根目录下XXX）
            val appDir = File(Environment.getExternalStorageDirectory().absolutePath)
           if (!appDir.exists()) {
                /*mkdirs();//多级目录*/
                val isSuccess = appDir.mkdirs()
                // DebugLog.i("创建"+isSuccess.toString());
                //  }else{
                //    DebugLog.i("已有文件夹");
            }
            // 文件名
            val fileName = "XXexport" + System.currentTimeMillis() + ".jpg"
            val file = File(appDir, fileName)
                //try {
                //   DebugLog.i("开始保存");
            //test.verifyStoragePermissions()
            val fos:FileOutputStream= FileOutputStream(file)
           // val x = FileOutputStream(file)
                //通过io流的方式来压缩保存图片
                val isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()

                //把文件插入到系统图库
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

                //保存图片后发送广播通知更新数据库
                val uri = Uri.fromFile(file)
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

                if (isSuccess) {
                    return true
                } else {
                    return false
                }
            //} catch (e: IOException) {
              //  e.printStackTrace()
            //}

            //return false
        }

    }
}
