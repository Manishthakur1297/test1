package com.techyshed.codez

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.techyshed.codez.Database.DbManager

class SiteActivity : AppCompatActivity() {

    val table_Category = "category"
    val table_Posts = "posts"
    val table_Recent = "recent"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site)
        var db = DbManager(applicationContext)
        db.Delete(table_Category,"1")
        db.Delete(table_Posts,"1")
        db.Delete(table_Recent,"1")
    }

    override fun onResume() {
        super.onResume()
        var db = DbManager(applicationContext)
        db.Delete(table_Category,"1")
        db.Delete(table_Posts,"1")
        db.Delete(table_Recent,"1")
    }

    fun BuCrazy(view: View)
    {
        var intent  = Intent(applicationContext,MainActivity::class.java)
        intent.putExtra("domain","https://www.thecrazyprogrammer.com/")
        startActivity(intent)
    }

    fun BuCoding(view: View)
    {
        var intent  = Intent(applicationContext,MainActivity::class.java)
        intent.putExtra("domain","http://www.codingalpha.com/")
        startActivity(intent)
    }


    fun BuJava(view: View)
    {
        var intent  = Intent(applicationContext,MainActivity::class.java)
        intent.putExtra("domain","https://www.thejavaprogrammer.com/")
        startActivity(intent)
    }

    fun BuCodeWin(view: View)
    {
        var intent  = Intent(applicationContext,MainActivity::class.java)
        intent.putExtra("domain","https://www.codeforwin.org/")
        startActivity(intent)
    }


    fun Buw3c(view: View)
    {
        var intent  = Intent(applicationContext,MainActivity::class.java)
        intent.putExtra("domain","https://www.w3schools.in/")
        startActivity(intent)
    }

}
