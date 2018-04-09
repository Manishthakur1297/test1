package com.techyshed.codez.RecentFragment

import android.app.SearchManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import com.techyshed.codez.Adapter.MyPostAdapter
import com.techyshed.codez.Controller.APIController
import com.techyshed.codez.Controller.ServiceVolley
import com.techyshed.codez.Database.DbManager
import com.techyshed.codez.POJO.Post

import com.techyshed.codez.R
import kotlinx.android.synthetic.main.fragment_download.*
import android.content.DialogInterface
import android.support.v4.widget.SwipeRefreshLayout
import com.techyshed.codez.Adapter.MyPostDownloadAdapter


class DownloadFragment : Fragment() {

    var domain = "thecrazyprogrammer.com"

    val table_Download = "download"
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    var list = ArrayList<Post>()

    var isScrolling = false
    private var recyclerView: RecyclerView? = null
    private var adapter: MyPostDownloadAdapter? = null

    var progressBar: ProgressBar?=null


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        inflater!!.inflate(R.menu.search_menu,menu)

        val sv: SearchView = menu!!.findItem(R.id.search).actionView as SearchView

        val sm= activity.applicationContext.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(activity.componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //Toast.makeText(activity.applicationContext, query, Toast.LENGTH_LONG).show()
                //LoadQuery("%" + query+ "%")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter!!.filter.filter(newText)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater!!.inflate(R.layout.fragment_download, container, false)

        progressBar = view.findViewById(R.id.progressBar) as ProgressBar
        list = ArrayList()
        recyclerView = view.findViewById(R.id.recyclerDownload) as RecyclerView
        var layoutManager = LinearLayoutManager(activity.applicationContext)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)

        var fragmentM = activity.supportFragmentManager

        adapter = MyPostDownloadAdapter(activity.applicationContext, list, fragmentM, domain!!)
        recyclerView!!.adapter = adapter

        try
        {
            LoadQuery("%")
        }catch (ex:Exception)
        {

        }

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        mSwipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            // Refresh items
            page=1
            list.clear()

            LoadQuery("%")

            mSwipeRefreshLayout!!.setRefreshing(false);
        })


        return view

    }

   /* fun BuDelete(view:View)
    {
                var dbManager = DbManager(activity.applicationContext)

                var selectionArgs = arrayOf(list.link.toString())
                dbManager.DeletePost(table_Download,"Url=?",selectionArgs)
                LoadQuery("%")

    }*/

    fun LoadQuery(title:String){
        var dbManager = DbManager(activity.applicationContext)
        var projection = arrayOf("i","Url","Title","Content","Excerpt")
        var selectionArgs = arrayOf(title)
        var cursor = dbManager.Query(table_Download,projection,"Title like ?",selectionArgs,"i"+" ASC")
        list!!.clear()

        if(cursor.moveToFirst())
        {
            do{

                var ID = cursor.getInt(cursor.getColumnIndex("i"))
                var Url = cursor.getString(cursor.getColumnIndex("Url"))
                var Title = cursor.getString(cursor.getColumnIndex("Title"))
                var Content = cursor.getString(cursor.getColumnIndex("Content"))
                var Excerpt = cursor.getString(cursor.getColumnIndex("Excerpt"))

                //(id: Long, link: String, title:String, content:String, excerpt:String)

                list.add(Post(ID.toLong(),Url,Title,Content,Excerpt))
                tvDownload.visibility = View.INVISIBLE

            }while(cursor.moveToNext())
        }

        adapter!!.notifyDataSetChanged()
    }


    override fun onResume() {
        super.onResume()
        //Log.i("Onresume working----","Yes")
        try
        {
            LoadQuery("%")
        }catch (ex:Exception)        {        }
    }


    companion object {

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        var page = 1
        var max = 0
        var length = 0

        fun newInstance(param1: String): DownloadFragment {
            val fragment = DownloadFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }
}
