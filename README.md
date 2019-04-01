# WallPaper

## 初步架构安排：
* model: 放各种json/url解析数据定义
* commands：放各种地址获取

* presenter：连接view和model

* view：响应用户操作，调用presenter方法

## 地址解析相关
| name      | url                                                                      | note                                                                                                         |
| :-------- | :----------------------------------------------------------------------- | :------------------------------------------------------------------------------------------------------------------ |
| bing      | `https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh_CN`<br/>`https://www.bing.com/ImageResolution.aspx?w=360&h=640&mkt=zh-CN` | n:数量，mkt:可省略 [参见](https://stackoverflow.com/questions/10639914/is-there-a-way-to-get-bings-photo-of-the-day)</br>第二个链接貌似是bing提供的其他大小图片w&h参数，返回html里面有链接 |
| 国家地理   | `www.nationalgeographic.com:80/photography/photo-of-the-day/`               | 第三方[api](https://api-cn.berryapi.net/docs.html)画质lj</br>解析html：<meta property="og:image" content=           |
| nasa      | `https://apod.nasa.gov/apod/`</br>`https://apod.nasa.gov/apod/apYYMMDD.html` | 解析html:`<a href=\"(image/.*)\"`<br/>rss提取                                                                   |
| 地球科学   | `https://epod.usra.edu/`                                                  | 解析:`://epod.usra.edu/.a/*-pi`                                                                                |
| noaa      | `http://www.nesdis.noaa.gov/content/imagery-and-data`                       |                                                                                                              |
| wikipedia | `https://en.wikipedia.org/wiki/Wikipedia:Picture_of_the_day`                |                                                                                                              |
| flickr    | `https://www.flickr.com/photos/tags/daily/`                                |                                                                                                              |


