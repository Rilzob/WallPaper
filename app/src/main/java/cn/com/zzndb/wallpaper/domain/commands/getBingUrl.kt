package cn.com.zzndb.wallpaper.domain.commands

import android.util.Log
import cn.com.zzndb.wallpaper.domain.model.bingResult
import cn.com.zzndb.wallpaper.view.IView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL


/**
 * get bing url, use Gson analyze json return
 */
class getBingUrl(sWidth: Int, sHeight: Int, val mView: IView) {
    private val BingURL = "https://cn.bing.com/HPImageArchive.aspx?" +
            "format=js&idx=0&n=1&mkt=zh_CN"
    private val ImageSizeUrl = "https://cn.bing.com/ImageResolution.aspx?w=$sWidth" +
            "&h=$sHeight&mkt=zh-CN"

    val url: String
        get() {
            // get json list
            val jsonStr = URL(BingURL).readText()
            // use gson parse json
            val fJsonStr = Gson().fromJson(jsonStr, bingResult::class.java)
            // use okhttp send get request to get fit size image width&height
            val client = OkHttpClient().newBuilder()
                .followRedirects(false) // just get new image size, not follow
                .followSslRedirects(false)
                .build()
            val request = Request.Builder()
                .url(ImageSizeUrl)
                .get()
                .removeHeader("Referer")
                .header("Referer", "https://www.bing.com/?mkt=zh-CN")
                .build()
            val regex = "\\d{2,4}x\\d{2,4}".toRegex()
            val reSize = regex.find(client.newCall(request).execute()
                    .header("location").toString())?.value
            if (reSize != null) {
                Log.d(
                    "test okhttp request", "https://www.bing.com${regex
                        .replace(fJsonStr.images[0].url, reSize)}"
                )
                // change size argument to fit screen
                return "https://www.bing.com${regex.replace(
                    fJsonStr.images[0].url, reSize
                )
                }"
            }
            else {
                mView.showMes("Error to get fit image size!!")
                return "https://www.bing.com${fJsonStr.images[0].url}"
            }
        }
}
