package cn.com.zzndb.wallpaper.domain.db

import org.jetbrains.anko.db.*
import java.lang.Exception

class PicDb(private var picDbHelper: PicDbHelper) {

    fun saveDailyPicInfo(date: String, sName: String, fName: String) {
        val test = picDbHelper.use {
            try {
                // check old uri
                select(PicTable.NAME, PicTable.FNAME)
                    .whereArgs("${PicTable.DATE} like '$date' and " +
                            "${PicTable.SNAME} like '$sName'").parseOpt(StringParser)!!
            } catch (e: Exception) {
                ""
            }
        }
        if (test == "")
            // insert uri
            picDbHelper.use {
                insert(
                    PicTable.NAME,
                    PicTable.DATE to date,
                    PicTable.SNAME to sName,
                    PicTable.FNAME to fName
                )
            }
        else {
            // update uri
            picDbHelper.use {
                update(PicTable.NAME, PicTable.FNAME to fName)
                    .whereArgs("${PicTable.DATE} like '$date' and " +
                            "${PicTable.SNAME} like '$sName'").exec()
            }
        }
    }

    fun checkExist(fName: String) : Boolean {
        var existfName = ""
        picDbHelper.use {
            existfName = try {
                select(PicTable.NAME, PicTable.FNAME)
                    .whereArgs("${PicTable.FNAME} like '$fName'").parseOpt(StringParser)!!
            } catch (e: Exception) {
                ""
            }

        }
        return existfName != ""
    }

    fun checkTodayPicInfo(date: String, str: String) : String {
        var existfName = ""
        picDbHelper.use {
            existfName = try {
                select(PicTable.NAME, PicTable.FNAME)
                    .whereArgs("${PicTable.DATE} like '$date' and ${PicTable.SNAME} like '$str'")
                    .parseOpt(StringParser)!!
            } catch (e: Exception) {
                str
            }
        }
        return existfName
    }

    // today's or yesterday's (for not up in UTC+8 timezone source
    fun getCurrentPicUri(date: String, str: String, bdate: String) : String {
        var existfName = checkTodayPicInfo(date, str)
        if (existfName == str) {
            // try get yesterday's image uri
            picDbHelper.use {
                existfName = try {
                    select(PicTable.NAME, PicTable.FNAME)
                        .whereArgs("${PicTable.DATE} like '$bdate' and ${PicTable.SNAME} like '$str'")
                        .parseOpt(StringParser)!!
                } catch (e: Exception) {
                    str
                }
            }
        }
        return existfName
    }
}
