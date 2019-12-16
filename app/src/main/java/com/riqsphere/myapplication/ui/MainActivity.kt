package com.riqsphere.myapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.riqsphere.myapplication.R
import com.riqsphere.myapplication.cache.JikanCacheHandler
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime
import com.riqsphere.myapplication.room.MyaaViewModel
import com.riqsphere.myapplication.tasks.WatchlistAlarm
import com.riqsphere.myapplication.ui.animes.AnimesFragment
import com.riqsphere.myapplication.ui.discover.DiscoverFragment
import com.riqsphere.myapplication.ui.settings.SettingsFragment
import com.riqsphere.myapplication.utils.ImageHandler
import java.io.OutputStream
import java.io.PrintStream
import java.util.*


private const val PREFERENCE_FIRST_RUN = "first_run"


class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        disableSystemOut()
        ImageHandler.init(this)

        val p = getDefaultSharedPreferences(this)
        if (p.getBoolean(PREFERENCE_FIRST_RUN, true)) {
            installTasks()
            p.edit().putBoolean(PREFERENCE_FIRST_RUN, false).apply()
        }

        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            val selectedFragment =
                when (menuItem.itemId) {
                    R.id.nav_animes -> AnimesFragment()
                    R.id.nav_discover -> DiscoverFragment()
                    R.id.nav_settings -> SettingsFragment()
                    else -> null
            }
            selectedFragment?.let { navigateTo(it) }
            true
        }

        navigateToDefaultPage()
    }


    private fun navigateTo(it: Fragment) = supportFragmentManager.beginTransaction().replace(R.id.bottom_navigation_fragment_container, it).commit()

    private fun navigateToDefaultPage() = navigateToAnimes()

    private fun navigateToAnimes() = supportFragmentManager.beginTransaction().replace(R.id.bottom_navigation_fragment_container, AnimesFragment()).commit()

    private fun installTasks() {
        WatchlistAlarm.setAlarm(application)

        val myaaViewModel = MyaaViewModel(application)
        val naruto = WatchlistAnime(
            temporaryFetch(20)
        )
        myaaViewModel.insert(naruto) //naruto
        val hero4 = WatchlistAnime(
            temporaryFetch(38408)
        )
        myaaViewModel.insert(hero4) //boku no hero academia 4
        val nrt = WatchlistAnime(
            temporaryFetch(20)
        )
        myaaViewModel.insert(nrt) //naruto
        val random = WatchlistAnime(
            temporaryFetch(21)
        )
        myaaViewModel.insert(random) //random id
        val rdm = WatchlistAnime(
            temporaryFetch(22)
        )
        myaaViewModel.insert(rdm)
    }

    private fun disableSystemOut() {
        System.setOut(object : PrintStream(object : OutputStream() {
            override fun write(b: Int) {}
        }) {
            override fun flush() {}
            override fun close() {}
            override fun write(b: Int) {}
            override fun write(b: ByteArray) {}
            override fun write(buf: ByteArray, off: Int, len: Int) {}
            override fun print(b: Boolean) {}
            override fun print(c: Char) {}
            override fun print(i: Int) {}
            override fun print(l: Long) {}
            override fun print(f: Float) {}
            override fun print(d: Double) {}
            override fun print(s: CharArray) {}
            override fun print(s: String) {}
            override fun print(obj: Any) {}
            override fun println() {}
            override fun println(x: Boolean) {}
            override fun println(x: Char) {}
            override fun println(x: Int) {}
            override fun println(x: Long) {}
            override fun println(x: Float) {}
            override fun println(x: Double) {}
            override fun println(x: CharArray) {}
            override fun println(x: String) {}
            override fun println(x: Any) {}
            override fun printf(format: String, vararg args: Any): PrintStream {
                return this
            }

            override fun printf(
                l: Locale,
                format: String,
                vararg args: Any
            ): PrintStream {
                return this
            }

            override fun format(format: String, vararg args: Any): PrintStream {
                return this
            }

            override fun format(
                l: Locale,
                format: String,
                vararg args: Any
            ): PrintStream {
                return this
            }

            override fun append(csq: CharSequence?): PrintStream {
                return this
            }

            override fun append(csq: CharSequence?, start: Int, end: Int): PrintStream {
                return this
            }

            override fun append(c: Char): PrintStream {
                return this
            }
        })
    }

    private fun temporaryFetch(id: Int) = JikanCacheHandler.getAnime(id)
}