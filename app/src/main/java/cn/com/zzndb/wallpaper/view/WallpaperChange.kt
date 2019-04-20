package cn.com.zzndb.wallpaper.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import cn.com.zzndb.wallpaper.presenter.PresenterImpl
import org.jetbrains.anko.doAsync
import java.util.*

class WallpaperChange: Service() {

    var presenter: PresenterImpl? = null
    var hour: Int = 6
    var min: Int = 0
    private var alarmMgr: AlarmManager? = null
    private lateinit var pendingIntent: PendingIntent

    var count = 0

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // check whether from this
//        if (intent!!.getBooleanExtra("status", false)) {
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
            val calender: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, min)
            }
            val wcintent = Intent(this, WallpaperChange::class.java)
//            wcintent.putExtra("status", true)
            pendingIntent = PendingIntent.getService(this, 1, wcintent, PendingIntent.FLAG_CANCEL_CURRENT)
            alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmMgr?.setInexactRepeating(
                AlarmManager.RTC,
                calender.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
//        }
//        else Log.d("test service", "not status true")
        return super.onStartCommand(intent, flags, startId)
//        alarm = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val triggerTime = SystemClock.elapsedRealtime() + 5000
//        val i = Intent(this, WallpaperChange::class.java)
//        val pendingIntent = PendingIntent.getService(this, 1, i, 0)
//        alarm!!.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
    }

    private var mBinder = WallBinder()

    inner class WallBinder : Binder() {
        fun argSet(presenter: PresenterImpl) {
            this@WallpaperChange.presenter = presenter
        }
    }
}

