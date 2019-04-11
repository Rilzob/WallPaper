package cn.com.zzndb.wallpaper.domain.db

import org.jetbrains.anko.db.*
import java.lang.Exception

class PicDb(private var picDbHelper: PicDbHelper) {
    fun saveDailyPicInfo(date: String, sName: String, fName: String) = picDbHelper.use {
        insert(
            PicTable.NAME,
            PicTable.DATE to date,
            PicTable.SNAME to sName,
            PicTable.FNAME to fName
        )
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
}
