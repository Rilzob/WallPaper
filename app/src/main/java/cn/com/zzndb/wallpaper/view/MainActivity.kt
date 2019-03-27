package cn.com.zzndb.wallpaper.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import cn.com.zzndb.wallpaper.R
import cn.com.zzndb.wallpaper.presenter.PresenterImpl
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find

class MainActivity : AppCompatActivity(), IView{

    private val presenter = PresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val imageview = find(R.id.imageshow) as ImageView
        doAsync {
            showImage(imageview)
        }
    }

    override fun showImage(view: ImageView) {
        presenter.loadImage(presenter.getImageUrl(), view)
    }
}
