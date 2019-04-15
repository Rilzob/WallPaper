package cn.com.zzndb.wallpaper.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import cn.com.zzndb.wallpaper.R
import org.jetbrains.anko.find

class search : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_layout)
        initView()
    }

    private fun initView() {
        // focus on search view
        val searchView = find(R.id.search_view) as SearchView
        searchView.onActionViewExpanded()
    }
}

