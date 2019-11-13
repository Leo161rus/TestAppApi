package com.example.testappapi.presentor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.testappapi.NewsAppApi
import com.example.testappapi.R
import com.example.testappapi.di.component.AppComponent
import com.example.testappapi.presentor.news.NewsFragment
import com.example.testappapi.presentor.sources.SourcesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_container.*
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {


    private var title: String = SourcesFragment.TITLE
    private lateinit var fragment: Fragment
    private lateinit var newsAppApi: NewsAppApi

    companion object {
        const val FRAGMENT_KEY: String = "fragment"
        private const val TITLE_KEY: String = "title"
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsAppApi.appComponent.inject(this)


        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener { onBackPressed() }
        navigation.setOnNavigationItemSelectedListener(this)

        title = savedInstanceState?.getString(TITLE_KEY) ?: title

        if (supportFragmentManager.findFragmentByTag(FRAGMENT_KEY) == null) {
            changeFragment(SourcesFragment())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigationSources -> {
                title = SourcesFragment.TITLE
                fragment = SourcesFragment()
            }
            R.id.navigationNews -> {
                title = NewsFragment.TITLE
                fragment = NewsFragment()
            }
        }

        toolbar.title = title
        changeFragment(fragment)

        return true
    }

    override fun onResume() {
        super.onResume()
        toolbar.title = title
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(
                R.id.main_container, fragment,
                FRAGMENT_KEY
            )
            .commit()
    }

    fun showBackButton(show: Boolean) = with(toolbar) {
        when (show) {
            true -> navigationIcon =
                resources.getDrawable(R.drawable.ic_apps_white_24dp)
            else -> navigationIcon = null
        }
    }
}
