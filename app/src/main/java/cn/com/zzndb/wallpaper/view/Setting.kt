package cn.com.zzndb.wallpaper.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import android.widget.ImageButton
import cn.com.zzndb.wallpaper.R
import org.jetbrains.anko.find

class Setting : AppCompatActivity() {

    private var back: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.setting_container, SettingFragment())
            .commit()
        back = find(R.id.setting_title_back) as ImageButton
        back?.setOnClickListener {
            // may need save profile here
            this.finish()
        }
    }
}

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_prefer, rootKey)
    }
}
