package com.techyshed.codez.CategoryFragment

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.Toast
import com.techyshed.codez.Database.DbManager
import com.techyshed.codez.Handler.MyWebViewClient
import com.techyshed.codez.R
import kotlinx.android.synthetic.main.activity_main.*


class CrazyContent : Fragment() {

    //var progressBar: ProgressBar?=null
    //internal var toolbar: Toolbar?=null

    var domain: String? = null
    var webView: WebView?=null
    var title:String?=null
    var url:String?=null
    var content:String?=null
    var excerpt:String?=null

    val table_Download = "download"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            domain = arguments.getString(ARG_PARAM1)
            //mParam2 = arguments.getString(ARG_PARAM2)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId)
        {
            R.id.download->
            {
                var dbManager = DbManager(activity.applicationContext)
                var values = ContentValues()

                values.put("Url", url)
                values.put("Title", title)
                values.put("Content", content)
                values.put("Excerpt", excerpt)

                var ID = dbManager.Insert(table_Download, values)

                Toast.makeText(activity.applicationContext,"Post Downloaded",Toast.LENGTH_LONG).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater!!.inflate(R.layout.fragment_crazy_content, container, false)
        //var toolbar = view.findViewById(R.id.toolbar) as Toolbar
        //var actvity = activity as AppCompatActivity
        //actvity.setSupportActionBar(toolbar)
        //actvity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //progressBar = view.findViewById(R.id.progressBar) as ProgressBar
        webView = view.findViewById(R.id.webView) as WebView

        var b:Bundle = arguments
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
        return view
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

    companion object {

        private val ARG_PARAM1 = "param1"

        fun newInstance(param1: String): CrazyContent {
            val fragment = CrazyContent()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }
}
