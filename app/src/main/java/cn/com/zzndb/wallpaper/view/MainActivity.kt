package cn.com.zzndb.wallpaper.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.widget.*
import cn.com.zzndb.wallpaper.domain.commands.ImageSaveUtils
import cn.com.zzndb.wallpaper.domain.commands.test
import cn.com.zzndb.wallpaper.domain.commands.test.REQUEST_EXTERNAL_STORAGE
import cn.com.zzndb.wallpaper.presenter.PresenterImpl
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(), IView, RadioGroup.OnCheckedChangeListener{

    // not null delegate, can only use after assign or trow error
    private var sHeight: Int by Delegates.notNull()
    private var sWidth: Int by Delegates.notNull()

    private var presenter = PresenterImpl(this)
    private val mDrawerLayout: DrawerLayout? = null

    private var bottomRgGroup: RadioGroup? = null
    private var bottombing: RadioButton? = null

    private var bingFg: ContentFragment? = null
    private var ngFg: ContentFragment? = null
    private var nasaFg: ContentFragment? = null
    private var mineFg: ContentFragment? = null

    private var fManager: FragmentManager? = null
    var context:Context=this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(cn.com.zzndb.wallpaper.R.layout.activity_main)
        initView()
    }

    private fun initView() {
        // init view test
        val mDrawerLayout = find(cn.com.zzndb.wallpaper.R.id.drawer_layout) as DrawerLayout
        val navMenu = find(cn.com.zzndb.wallpaper.R.id.title_more) as ImageButton
//        val navView = find(R.id.nav_view) as NavigationView
        val titleSearch = find(cn.com.zzndb.wallpaper.R.id.title_search) as ImageButton

        // fragment manager
        fManager = supportFragmentManager
        bottomRgGroup = find(cn.com.zzndb.wallpaper.R.id.bottom_bar)
        bottomRgGroup!!.setOnCheckedChangeListener(this)

        // choose ~~bing~~ nasa default (to tmp escape the buggy switch. (( nasa first also buggy after processBar added, so change back
        bottombing = find(cn.com.zzndb.wallpaper.R.id.bottom_bing)
        bottombing!!.isChecked = true

        navMenu.setOnClickListener {    // control drawer bar
            mDrawerLayout.openDrawer(GravityCompat.START)
        }

        titleSearch.setOnClickListener {
            startActivity(Intent(this, search::class.java))
        }

        getScreenSize()
        val permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, test.PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE)
        }
        doAsync {
            //DonwloadSaveImg.donwloadImg(presenter.getImageUrl("Bing"))
            //val bitmap = d(presenter.getImageUrl("Bing"))

            var bitmap =ImageSaveUtils.returnBitMap(presenter.getImageUrl("Bing"))
            val isSaveSuccess = ImageSaveUtils.saveImageToGallery(bitmap,context)
            print(isSaveSuccess)
        }

        Log.d("test size get:", getHeight().toString() + "x" + getWidth().toString())
    }

    override fun showImage(str: String ,view: ImageView, fView: ContentFragment) {
        presenter.loadImage(presenter.getImageUrl(str), view, fView)
    }

    override fun getHeight(): Int = sHeight

    override fun getWidth(): Int = sWidth

    private fun getScreenSize() {
        val context = this.applicationContext
        sHeight = context.resources.configuration.screenHeightDp
        sWidth = context.resources.configuration.screenWidthDp
    }

    override fun showMes(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    // like bottom clickListener
    // TODO: learn how to fix the buggy switch, something wrong after nasa image load; add ProcessBar
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val fTransaction : FragmentTransaction = fManager!!.beginTransaction()
        hideAllFragment(fTransaction)
        when(checkedId) {
            cn.com.zzndb.wallpaper.R.id.bottom_bing -> if (bingFg == null) {
                bingFg = ContentFragment()
                bingFg?.settStr("Bing")
                Log.d("test mes", "new bingFg")
                fTransaction.add(cn.com.zzndb.wallpaper.R.id.fg_content, bingFg!!)
            }
            else {
                fTransaction.show(bingFg!!)
            }
            cn.com.zzndb.wallpaper.R.id.bottom_ng -> if (ngFg == null) {
                ngFg = ContentFragment()
                ngFg?.settStr("Ng")
                Log.d("test mes", "new ngFG")
                fTransaction.add(cn.com.zzndb.wallpaper.R.id.fg_content, ngFg!!)
            } else {
                fTransaction.show(ngFg!!)
            }
            cn.com.zzndb.wallpaper.R.id.bottom_nasa -> if (nasaFg == null) {
                nasaFg = ContentFragment()
                nasaFg?.settStr("Nasa")
                Log.d("test mes", "new nasaFg")
                fTransaction.add(cn.com.zzndb.wallpaper.R.id.fg_content, nasaFg!!)
            } else {
                fTransaction.show(nasaFg!!)
            }
            cn.com.zzndb.wallpaper.R.id.bottom_mine ->  {
                if (mineFg == null) {
                    mineFg = ContentFragment()
                    Log.d("test mes", "new mineFg")
                    fTransaction.add(cn.com.zzndb.wallpaper.R.id.fg_content, mineFg!!)
                } else {
                    fTransaction.show(mineFg!!)
                }
            }
        }
        fTransaction.commit()
    }

    private fun hideAllFragment(fragmentTransaction: FragmentTransaction) {
        if (bingFg != null) fragmentTransaction.hide(bingFg!!)
        if (ngFg   != null)   fragmentTransaction.hide(ngFg!!)
        if (mineFg != null) fragmentTransaction.hide(mineFg!!)
        if (mineFg != null) fragmentTransaction.hide(mineFg!!)
    }
}
