package cn.com.zzndb.wallpaper.domain.commands

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class getNASAUrl {
    private val NASAUrl = "https://apod.nasa.gov/apod/"

    val url: String
        get() {
            val doc_old: Document = Jsoup
                    .connect(NASAUrl)
                    .get()

            val element = doc_old
                    .select("IMG")
                    .attr("SRC")

            val picture_url = "https://apod.nasa.gov/apod/" + element.toString()

            println("picture_url:")
            println(picture_url)

            return picture_url
        }
}

//fun main(args: Array<String>) {
//    val NASAUrl = "https://apod.nasa.gov/apod/"
//
//    val doc_old: Document = Jsoup
//            .connect(NASAUrl)
//            .get()
//
//    val element = doc_old
//            .select("IMG")
//            .attr("SRC")
//
//    val picture_url = "https://apod.nasa.gov/apod/" + element.toString()
//
//    println("picture_url:")
//    println(picture_url)
//}