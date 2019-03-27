package cn.com.zzndb.wallpaper.domain.commands

import cn.com.zzndb.wallpaper.domain.model.bingResult
import com.google.gson.Gson
import java.net.URL

/**
 * get bing url, use Gson analyze json return
 */
class getBingUrl {
    private val BingURL = "https://www.bing.com/HPImageArchive.aspx?" +
            "format=js&idx=0&n=1&mkt=zh_CN"

    val url: String
        get() {
            val jsonStr = URL(BingURL).readText()
            val fJsonStr = Gson().fromJson(jsonStr, bingResult::class.java)
            return "https://www.bing.com${fJsonStr.images[0].url}"
        }
}