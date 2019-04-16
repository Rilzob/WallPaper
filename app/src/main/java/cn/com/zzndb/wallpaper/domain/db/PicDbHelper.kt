package cn.com.zzndb.wallpaper.domain.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Pic database helper
 */
class PicDbHelper private constructor(ctx: Context): ManagedSQLiteOpenHelper(
    ctx, PicDbHelper.DB_NAME, null, PicDbHelper.DB_VERSION) {

    companion object {
        const val DB_NAME = "pic.db"
        const val DB_VERSION = 2
        private val instance: PicDbHelper? = null

        fun getInstance(ctx: Context) = instance ?: PicDbHelper(ctx)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(PicTable.NAME, true,
            PicTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            PicTable.DATE to TEXT,
            PicTable.SNAME to TEXT,
            PicTable.FNAME to TEXT,
            PicTable.URL to TEXT)
    }

    // just simple cache data
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(PicTable.NAME, true)
        onCreate(db)
    }

    val Context.database: PicDbHelper
        get() = PicDbHelper.getInstance(this)

}
