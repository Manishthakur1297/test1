package com.techyshed.codez

import android.app.SearchManager
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.CoordinatorLayout
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import com.techyshed.codez.CategoryFragment.CrazyFragment
import com.techyshed.codez.Database.DbManager
import com.techyshed.codez.RecentFragment.RecentFragment
import com.techyshed.codez.Handler.BottomNavigationBehavior
import com.techyshed.codez.RecentFragment.DownloadFragment


class MainActivity : AppCompatActivity() {

   var toolbar:ActionBar?=null
    var domain:String?=null
    val table_Recent = "recent"

    override fun onDestroy() {
        super.onDestroy()

        var dbManager = DbManager(applicationContext)
        var del = dbManager.Delete(table_Recent,"1")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var dbManager = DbManager(applicationContext)
        var del = dbManager.Delete(table_Recent,"1")

        toolbar = supportActionBar

        var b = intent.extras
        domain = b.getString("domain")

        var navigation = findViewById(R.id.navigation) as BottomNavigationView

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val layoutParams = navigation.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = BottomNavigationBehavior()

        toolbar!!.setTitle("Recent Articles")
        loadFragment(RecentFragment.newInstance(domain!!))

    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        var fragment:Fragment?=null
        when (item.itemId) {


            R.id.navigation_recent -> {
                //message.setText(R.string.title_home)
                toolbar!!.setTitle("Recent Articles")
                //supportActionBar!!.setTitle("CrazyProgrammer")
                        //fragment = CrazyFragment(domain)
                loadFragment(RecentFragment.newInstance(domain!!))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_category -> {
                toolbar!!.setTitle("Category")
                //supportActionBar!!.setTitle("Programmer")
                //fragment = CrazyFragment(domain)
                loadFragment(CrazyFragment.newInstance(domain!!))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_download -> {
                toolbar!!.setTitle("Downloaded")
                //supportActionBar!!.setTitle("CyProgrammer")
                fragment = DownloadFragment()
                loadFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment : Fragment)
    {

        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container,fragment)
        //transaction.addToBackStack("f")
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        val sv: SearchView = menu!!.findItem(R.id.search).actionView as SearchView

        val sm= applicationContext.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                //LoadQuery("%" + query+ "%")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }*/


}

