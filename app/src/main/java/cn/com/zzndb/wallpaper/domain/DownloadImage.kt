package cn.com.zzndb.wallpaper.domain

import android.os.AsyncTask
import android.util.Log
import cn.com.zzndb.wallpaper.presenter.DownloadListener
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.lang.Exception

class DownloadImage(private val listener: DownloadListener,
                    private val cachePath: String) : AsyncTask<String, Int, Int>() {
    val TYPE_SUCCESS: Int = 0
    val TYPE_FAILED: Int = 1

    private var lastProgress = 0

    override fun doInBackground(vararg params: String?): Int {
        lateinit var ins: InputStream
        lateinit var savedFile: RandomAccessFile
        lateinit var file: File
        try {
            var downloadLength: Long = 0
            val downloadUrl = params[0]
            Log.d("test cachePath", cachePath)
            // save file to app data cache dir
            file = File(cachePath)
            if (file.exists()) {
                downloadLength = file.length()
            }
            val contentLength = getContentLength(downloadUrl!!)
            if (contentLength == 0L) {
                return TYPE_FAILED
            }
            else if (contentLength == downloadLength) {
                return TYPE_SUCCESS
            }

            val client = OkHttpClient()
            val request = Request.Builder()
                .addHeader("RANGE", "bytes=$downloadLength-")
                .url(downloadUrl)
                .build()
            val response = client.newCall(request).execute()
            if (response != null) {
                ins = response.body()!!.byteStream()
                savedFile = RandomAccessFile(file, "rw")
                savedFile.seek(downloadLength) // skip downloaded byte
                val b = ByteArray(1024)
                var total = 0
                var len = 0
                while (({len = ins.read(b); len}()) != -1) {
                    total += len
                    savedFile.write(b, 0, len)
                    // caculate the process of downloading
                    val progress: Int = ((total + downloadLength) * 100 /
                            contentLength).toInt()
                    publishProgress(progress)
                }
                response.body()?.close()
                return TYPE_SUCCESS
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                ins.close()
                savedFile.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return TYPE_FAILED
    }

    override fun onProgressUpdate(vararg values: Int?) {
        val progress: Int = values[0]!!
        if (progress > lastProgress) {
            listener.onProgress(progress)
            lastProgress = progress
        }
    }

    override fun onPostExecute(result: Int?) {
        when (result) {
            TYPE_SUCCESS -> {
                listener.onSuccess()
            }
            TYPE_FAILED -> {
                listener.onFailed()
            }
        }
    }

    private fun getContentLength(downloadUrl: String) : Long {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(downloadUrl)
            .build()
        val response = client.newCall(request).execute()
        if (response != null && response.isSuccessful)  {
            val contentLength = response.body()!!.contentLength()
            response.body()?.close()
            return contentLength
        }
        return 0
    }

}