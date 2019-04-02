package cn.com.zzndb.wallpaper.domain.commands

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class getNGChinaUrl {
    private val NGChinaUrl = "http://www.ngchina.com.cn/photography/photo_of_the_day/"

    val url: String
        get() {
            val doc_old:Document = Jsoup.connect(NGChinaUrl)
                    .get()
            val element_set = doc_old
                    .select("a[class=imgs]")
                    .attr("href")

            val new_url = "http://www.ngchina.com.cn" + element_set.toString()
            val doc_new: Document = Jsoup
                    .connect(new_url)
                    .get()
            val picture_url = doc_new.select("a[href$=###]")
                    .select("img")
                    .attr("src")
                    .toString()

            println("图片url:")
            println(picture_url)

            return picture_url
        }
}

//fun main(args: Array<String>) {
//    val NGChinaUrl = "http://www.ngchina.com.cn/photography/photo_of_the_day/"
//
//    val doc_old:Document = Jsoup.connect(NGChinaUrl).get()
////    val element_set = doc.select("img[src^=http:]")
////    val element_set1 = doc1.select("img[src~=https?:\\/\\/[a-z.]*\\/(\\d{4}\\/){2}\\d*.jpg]")
//
//    val element_set = doc_old.select("a[class=imgs]").attr("href")
//
//    val new_url = "http://www.ngchina.com.cn" + element_set.toString()
//
//    val doc_new: Document = Jsoup.connect(new_url).get()
//
//    println("图片url:")
//    println(doc_new.select("a[href$=###]").select("img").attr("src").toString())
//
////    println("new_url:")
////    println(new_url)
////    val element_array = element.toArray()
////    println("element_set1:")
////    println(element_set1.first().attr("src").toString())
//
////    println("element_set:")
////    println(element_set.toString())
////    for (element in element_set)
////        println(element.attr("src").toString())
//
////    val NGUrl = "https://www.nationalgeographic.com/photography/photo-of-the-day/"
////    val doc2:Document = Jsoup.connect(NGUrl).get()
////    println("doc2:")
////    println(doc2.toString())
////    val element_set2 = doc2.select("meta[property=og:image]").attr("content")
////    println("element_set2:")
////    println(element_set2.first().toString())
//}