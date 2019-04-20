package cn.com.zzndb.wallpaper.view

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import android.widget.ImageButton
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import cn.com.zzndb.wallpaper.R
import kotlinx.android.synthetic.main.nav_in_titlebar.*
import org.jetbrains.anko.find
import java.util.*

class Setting : AppCompatActivity() {

    private var back: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_in)
        title_text.text = getText(R.string.setting_titlebar_text)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_in_container, SettingFragment())
            .commit()
        back = find(R.id.title_back) as ImageButton
        back?.setOnClickListener {
            this.finish()
        }
    }
}

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_prefer, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        super.onPreferenceTreeClick(preference)
        if (preference?.key == "upTimeSet") {
            TimePickerFragment().show(this.fragmentManager,"timePicker")
            return true
        }
        return false
    }
}

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val upTimeIntent = Intent(activity, WallpaperChange::class.java)
        upTimeIntent.putExtra("hour", hourOfDay)
        upTimeIntent.putExtra("min", minute)
        upTimeIntent.putExtra("status", true)
        activity!!.startService(upTimeIntent)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val min = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, min,
            android.text.format.DateFormat.is24HourFormat(activity))
    }
}
