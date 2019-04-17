package cn.com.zzndb.wallpaper.view

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import cn.com.zzndb.wallpaper.R
import kotlinx.android.synthetic.main.nav_in_titlebar.*
import org.jetbrains.anko.find

class About : AppCompatActivity() {

    private var back: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_in)
        title_text.text = getString(R.string.about_titlebar_text)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_in_container, AboutFragment())
            .commit()
        back = find(R.id.title_back) as ImageButton
        back?.setOnClickListener {
            // may need save profile here
            this.finish()
        }
    }
}

class AboutFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.about_prefer, rootKey)
    }
}
