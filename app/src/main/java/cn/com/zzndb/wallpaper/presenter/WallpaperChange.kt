package cn.com.zzndb.wallpaper.presenter

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import org.jetbrains.anko.doAsync
import java.util.*

class WallpaperChange: Service() {

    var presenter: PresenterImpl? = null
    var hour: Int = 6
    var min: Int = 0
    lateinit var calender: Calendar

    var count = 0

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // check whether set time
        if ((intent!!.getIntExtra("hour", 25)) == 25) {
            // do not change initial from MainActivity
            if (!intent.getBooleanExtra("fromMain", false)) {
                doAsync {
                    presenter?.changeWallpaper()
                    Log.d("test service", "exec $count")
                    count++
                    if (presenter != null)
                        Log.d("test service", "presenter not null")
                }
            }
        } else {
            hour = intent.getIntExtra("hour", 25)
            min = intent.getIntExtra("min", 60)
            Log.d("test service", "time change to $hour : $min")
        }

        // skip first main activity set alarm
        if (!intent.getBooleanExtra("fromMain", false)) {
            // alarm test
            calender = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.MINUTE, min)
                set(Calendar.HOUR_OF_DAY, hour)
            }
            if (intent.getBooleanExtra("status", false)) {
                calender.add(Calendar.DATE, 1)
            }
            val wcintent = Intent(this, WallpaperChange::class.java)
            wcintent.putExtra("status", true)
            val pendingIntent = PendingIntent.getService(this, 0, wcintent, 0)
            val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmMgr.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calender.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private var mBinder = WallBinder()

    inner class WallBinder : Binder() {
        fun argSet(presenter: PresenterImpl) {
            this@WallpaperChange.presenter = presenter
        }
    }
}

