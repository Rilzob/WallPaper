package cn.com.zzndb.wallpaper.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.widget.*
import cn.com.zzndb.wallpaper.R
import cn.com.zzndb.wallpaper.presenter.PresenterImpl
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
    private var mineFg: MineFragment? = null

    private var fManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        // init view test
        val mDrawerLayout = find(R.id.drawer_layout) as DrawerLayout
        val navMenu = find(R.id.title_more) as ImageButton
//        val navView = find(R.id.nav_view) as NavigationView
        val titleSearch = find(R.id.title_search) as ImageButton

        // fragment manager
        fManager = supportFragmentManager
        bottomRgGroup = find(R.id.bottom_bar)
        bottomRgGroup!!.setOnCheckedChangeListener(this)

        // choose bing default
        bottombing = find(R.id.bottom_bing)
        bottombing!!.isChecked = true

        navMenu.setOnClickListener {    // control drawer bar
            mDrawerLayout.openDrawer(GravityCompat.START)
        }

        titleSearch.setOnClickListener {
            startActivity(Intent(this, search::class.java))
        }

        getScreenSize()
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
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val fTransaction : FragmentTransaction = fManager!!.beginTransaction()
        hideAllFragment(fTransaction)
        when(checkedId) {
            R.id.bottom_bing -> if (bingFg == null) {
                bingFg = ContentFragment()
                bingFg?.settStr("Bing")
                Log.d("test mes", "new bingFg")
                fTransaction.add(R.id.fg_content, bingFg!!)
            }
            else {
                fTransaction.show(bingFg!!)
            }
            R.id.bottom_ng -> if (ngFg == null) {
                ngFg = ContentFragment()
                ngFg?.settStr("Ng")
                Log.d("test mes", "new ngFG")
                fTransaction.add(R.id.fg_content, ngFg!!)
            } else {
                fTransaction.show(ngFg!!)
            }
            R.id.bottom_nasa -> if (nasaFg == null) {
                nasaFg = ContentFragment()
                nasaFg?.settStr("Nasa")
                Log.d("test mes", "new nasaFg")
                fTransaction.add(R.id.fg_content, nasaFg!!)
            } else {
                fTransaction.show(nasaFg!!)
            }
            R.id.bottom_mine ->  {
                if (mineFg == null) {
                    mineFg = MineFragment()
                    Log.d("test mes", "new mineFg")
                    fTransaction.add(R.id.fg_content, mineFg!!)
                } else {
                    fTransaction.show(mineFg!!)
                }
            }
        }
        fTransaction.commit()
    }

    // hide current fragment before switch
    private fun hideAllFragment(fragmentTransaction: FragmentTransaction) {
        if (bingFg != null) fragmentTransaction.hide(bingFg!!)
        if (ngFg   != null)   fragmentTransaction.hide(ngFg!!)
        if (nasaFg != null) fragmentTransaction.hide(nasaFg!!)
        if (mineFg != null) fragmentTransaction.hide(mineFg!!)
    }
}
