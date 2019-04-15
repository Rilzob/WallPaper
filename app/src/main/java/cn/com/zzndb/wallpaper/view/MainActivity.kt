package cn.com.zzndb.wallpaper.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.com.zzndb.wallpaper.R
import cn.com.zzndb.wallpaper.domain.db.PicDb
import cn.com.zzndb.wallpaper.domain.db.PicDbHelper
import cn.com.zzndb.wallpaper.presenter.DownloadService
import cn.com.zzndb.wallpaper.presenter.PresenterImpl
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import java.lang.Exception
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(), IView, RadioGroup.OnCheckedChangeListener,
    NavigationView.OnNavigationItemSelectedListener{

    // not null delegate, can only use after assign or trow error
    private var sHeight: Int by Delegates.notNull()
    private var sWidth: Int by Delegates.notNull()

    private var presenter: PresenterImpl? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var navView: NavigationView? = null

    private var bottomRgGroup: RadioGroup? = null
    private var bottombing: RadioButton? = null

    private var bingFg: ContentFragment? = null
    private var ngFg: ContentFragment? = null
    private var nasaFg: ContentFragment? = null
    private var mineFg: MineFragment? = null

    private var fManager: FragmentManager? = null

    private var downloadBinder: DownloadService.DownloadBinder? = null

    private var connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            downloadBinder = service as DownloadService.DownloadBinder
        }
        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    private var picDbHelper: PicDbHelper? = null
    private var picDb: PicDb? = null

    private var swipeRefresh: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        picDbHelper = PicDbHelper.getInstance(this)
        picDb = PicDb(picDbHelper!!)
        presenter = PresenterImpl(this, picDb!!)

        // init view test
        mDrawerLayout = find(R.id.drawer_layout) as DrawerLayout
        val navMenu = find(R.id.title_more) as ImageButton
        navView = find(R.id.nav_view) as NavigationView
        val titleSearch = find(R.id.title_search) as ImageButton

        // fragment manager
        fManager = supportFragmentManager
        bottomRgGroup = find(R.id.bottom_bar)
        bottomRgGroup!!.setOnCheckedChangeListener(this)

        // choose bing default
        bottombing = find(R.id.bottom_bing)
        bottombing!!.isChecked = true

        navMenu.setOnClickListener {    // control drawer bar
            mDrawerLayout!!.openDrawer(GravityCompat.START)
        }

        titleSearch.setOnClickListener {
            startActivity(Intent(this, search::class.java))
        }

        navView!!.setNavigationItemSelectedListener(this)
        // this and below the same one use to let the navigation view button restore normal
        navView!!.menu.getItem(0).isChecked = false

        getScreenSize()
        Log.d("test size get:", getHeight().toString() + "x" + getWidth().toString())

        // download service related
        val intent = Intent(this, DownloadService::class.java)
        startService(intent)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

        // swipe refresh related
        swipeRefresh = find(R.id.swipe_refresh) as SwipeRefreshLayout
        swipeRefresh!!.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh!!.setOnRefreshListener { forceLoadImage() }
    }

    override fun showImage(str: String ,view: ImageView, fView: ContentFragment) {
        presenter!!.downImage(str, view, fView)
    }

    override fun showImage(str: String ,view: ImageView, fView: ContentFragment, force: Boolean) {
        presenter!!.downImage(str, view, fView, force)
    }

    override fun getHeight(): Int = sHeight

    override fun getWidth(): Int = sWidth

    private fun getScreenSize() {
        val context = this.applicationContext
        sHeight = context.resources.configuration.screenHeightDp
        sWidth = context.resources.configuration.screenWidthDp
    }

    // show message in ui thread for other invoke
    override fun showMes(str: String) {
        doAsync {
            uiThread {
                Toast.makeText(this@MainActivity, str, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getDBinder(): DownloadService.DownloadBinder? {
        return downloadBinder
    }

    override fun forceLoadImage() {
        val curF = getCurrentFrag()
        if (curF is ContentFragment) {
            doAsync {
                presenter!!.downImage(curF.gettStr(), curF.getImageView(), curF, true)
            }
        }
        else {
            // TODO add for MineFragment
        }
        swipeRefresh!!.isRefreshing = false
    }

    override fun getPresenter(): PresenterImpl {
        return presenter!!
    }

    override fun requestWFPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 1)
        }
    }

    // asth wrong when a fragment loading switch to another still get the loading one
    override fun getCurrentFrag(): Fragment {
        var curF: Fragment? = null
        val currentFL = fManager!!.fragments
        for (fragment in currentFL) {
            if (fragment != null && fragment.userVisibleHint) {
                curF = try {
                    fragment as ContentFragment?
                } catch (e: Exception) {
                    fragment as MineFragment?
                }
            }
        }
        return curF!!
    }

    override fun getContext(): Context {
        return this
    }

    override fun checkWFPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, android.Manifest
            .permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
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

    // navigation item selected action
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_more -> {
                // uncheck the item, open setting activity
                navView!!.menu.getItem(0).isChecked = false
                startActivity(Intent(this, Setting::class.java))
            }
        }
        mDrawerLayout!!.closeDrawers()
        return true
    }

    // running time permission result action
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager
                        .PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "denied permission",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}
