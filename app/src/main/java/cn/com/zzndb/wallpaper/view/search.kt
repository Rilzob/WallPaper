package cn.com.zzndb.wallpaper.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import cn.com.zzndb.wallpaper.R
import org.jetbrains.anko.find

class search : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_layout)
        initView()
    }

    fun initView() {
        // focus on search view
        val searchView = find(R.id.search_view) as SearchView
        searchView.onActionViewExpanded()
    }
}

