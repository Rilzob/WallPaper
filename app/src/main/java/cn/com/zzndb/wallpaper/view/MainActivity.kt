package cn.com.zzndb.wallpaper.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.widget.ImageButton
import android.widget.ImageView
import cn.com.zzndb.wallpaper.R
import cn.com.zzndb.wallpaper.presenter.PresenterImpl
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find

class MainActivity : AppCompatActivity(), IView{

    private val presenter = PresenterImpl()
    private val mDrawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val mDrawerLayout = find(R.id.drawer_layout) as DrawerLayout
        val imageButtonMore = find(R.id.title_more) as ImageButton
//        val navView = find(R.id.nav_view) as NavigationView
        val imageButtonSearch = find(R.id.title_search) as ImageButton

        imageButtonMore.setOnClickListener {    // control drawer bar
            mDrawerLayout.openDrawer(GravityCompat.START)
        }

        imageButtonSearch.setOnClickListener {
            startActivity(Intent(this, search::class.java))
        }

        val imageview = find(R.id.imageshow) as ImageView
        doAsync {
            showImage(imageview)
        }
    }

    override fun showImage(view: ImageView) {
        presenter.loadImage(presenter.getImageUrl(), view)
    }
}
