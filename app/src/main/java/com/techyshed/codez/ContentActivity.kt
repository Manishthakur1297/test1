package com.techyshed.codez

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.Toast
import com.techyshed.codez.Database.DbManager
import com.techyshed.codez.Handler.MyWebViewClient

class ContentActivity : AppCompatActivity() {

    var domain: String? = null
    var webView: WebView?=null
    var title:String?=null
    var url:String?=null
    var content:String?=null
    var excerpt:String?=null

    val table_Download = "download"

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId)
        {
            R.id.download->
            {
                var dbManager = DbManager(applicationContext)
                var values = ContentValues()

                values.put("Url", url)
                values.put("Title", title)
                values.put("Content", content)
                values.put("Excerpt", excerpt)

                var ID = dbManager.Insert(table_Download, values)

                Toast.makeText(applicationContext,"Post Downloaded", Toast.LENGTH_LONG).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        webView = findViewById(R.id.webView) as WebView

        var b:Bundle = intent.extras
        url = b.getString("url")
        title = b.getString("title")
        content = b.getString("content")
        excerpt = b.getString("excerpt")

        Log.i("content------",content)
        webView!!.settings.javaScriptEnabled = true
        webView!!.settings.setSupportZoom(true)
        webView!!.settings.builtInZoomControls = true
        webView!!.settings.displayZoomControls = false
        var webViewClient = MyWebViewClient()
        webView!!.setWebViewClient(webViewClient)

        webView!!.loadData("<html>" +
                "<body> <h1 style=\"color:red;text-align:center;\">"
                + title+"</h1><p>" + content + "</p></body></html>","text/html",null)

        //progressBar!!.setVisibility(View.GONE);

        webView!!.setOnKeyListener( View.OnKeyListener { view, i, keyEvent ->

            //public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (i == KeyEvent.KEYCODE_BACK
                    && keyEvent.getAction() == MotionEvent.ACTION_UP
                    && webView!!.canGoBack()) {
                handler.sendEmptyMessage(1);
                true
            }

            false
        })
    }

    private val handler = object : Handler() {
        override fun handleMessage(message: Message) {
            when (message.what) {
                1 -> {
                    webViewGoBack()
                }
            }
        }
    }


    private fun webViewGoBack(){
        webView!!.goBack();
    }
}
