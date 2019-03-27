package cn.com.zzndb.wallpaper.domain.model

/**
 * bing json return data
 */
data class bingResult(val images: List<Images>, val tooltips: Tooltips)

data class Images(
    val startdate: String, val fullstartdate: String, val enddate: String,
    val url: String, val urlbase: String, val copyright: String,
    val copyrightlink: String, val title: String, val quiz: String,
    val wp: Boolean, val hsh: String, val drk: Int, val top: Int,
    val bot: Int
)

data class Tooltips(
    val loading: String, val previous: String, val next: String
)
