package com.techyshed.codez.CategoryFragment

import android.content.ContentValues
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import com.techyshed.codez.Adapter.MyPostAdapter
import com.techyshed.codez.Controller.APIController
import com.techyshed.codez.Controller.ServiceVolley
import com.techyshed.codez.Database.DbManager
import com.techyshed.codez.POJO.Post
import com.techyshed.codez.R


class CrazyPost : Fragment() {

    var domain: String? = null

    //private var mParam1: String? = null
    //private var mParam2: String? = null

    val table_Posts = "posts"
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    var list = ArrayList<Post>()
    //var list:ArrayList<Post>?=null

    var isScrolling = false
    private var recyclerView: RecyclerView? = null
    private var adapter: MyPostAdapter? = null

    var progressBar: ProgressBar?=null

    //var fragmentM:FragmentManager?=null

    val path = "wp-json/wp/v2/posts?categories="
    val link = "&page="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (arguments != null) {
            domain = arguments.getString(ARG_PARAM1)
            //mParam2 = arguments.getString(ARG_PARAM2)
        }*/


    }

    override fun onDestroy() {
        super.onDestroy()

        var dbManager = DbManager(activity.applicationContext)
        var del = dbManager.Delete(table_Posts,"1")
        Log.i("Page Before----", page.toString())
        page = 1
        Log.i("Page After----", page.toString())
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater!!.inflate(R.layout.fragment_crazy_post, container, false)

        Log.i("Deleted again----","-------------")
        Log.i("Page----", page.toString())
        val b:Bundle = arguments
        domain = b.getString("domain")
        val id = b.getString("id").toLong()
        val count = b.getString("count").toInt()
        max = count/10 + 1
        val url = b.getString("url")

        //Log.i("===URL==--",url)
        //Log.i("path+id+link+page----",path+id.toString()+link+page.toString())

        progressBar = view.findViewById(R.id.progressBar) as ProgressBar
        list = ArrayList()
        recyclerView = view.findViewById(R.id.recycler) as RecyclerView
        var layoutManager = LinearLayoutManager(activity.applicationContext)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)

        var fragmentM = activity.supportFragmentManager

        adapter = MyPostAdapter(activity.applicationContext, list, fragmentM, domain!!)
        recyclerView!!.adapter = adapter

        LoadQuery("%")

        //fragmentM = activity.supportFragmentManager
        //myPostAdapter = MyPostAdapter(activity.applicationContext,list!!,fragmentM!!)
        //recyclerView!!.adapter = myPostAdapter

        fetchData(domain + path+id+link+ page)

        //var thread = Thread.sleep(5000)
        //thread.run { LoadQuery("%")  }


        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true
                }
                // val totalItemCount = recyclerView!!.layoutManager.itemCount
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //var layout = LinearLayoutManager(this@MainActivity)
                val visible = layoutManager.childCount

                val total = layoutManager.itemCount

                val first = layoutManager.findFirstVisibleItemPosition()

                if(isScrolling && (first + visible == total ))
                {
                    isScrolling = false


                    if(page <= max && length >0 )
                    {
                        Toast.makeText(activity.applicationContext,"Fetching More Posts.....",Toast.LENGTH_SHORT).show()

                        fetchData(domain + path+id+link+ page)
                    }
                    else
                    {
                        Toast.makeText(activity.applicationContext,"No More Posts",Toast.LENGTH_SHORT).show()
                    }

                }

            }
        })

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        mSwipeRefreshLayout!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            // Refresh items
            page=1
            list.clear()
            var dbManager = DbManager(activity.applicationContext)
            var del = dbManager.Delete(table_Posts,"1")

            fetchData(domain + path+id+link+ page)

            mSwipeRefreshLayout!!.setRefreshing(false);
        })

        return view

    }

    fun LoadQuery(title:String){
        var dbManager = DbManager(activity.applicationContext)
        var projection = arrayOf("i","ID","Url","Title","Content","Excerpt")
        var selectionArgs = arrayOf(title)
        var cursor = dbManager.Query(table_Posts,projection,"Title like ?",selectionArgs,"i"+" ASC")
        list!!.clear()

        if(cursor.moveToFirst())
        {
            do{

                var ID = cursor.getInt(cursor.getColumnIndex("ID"))
                var Url = cursor.getString(cursor.getColumnIndex("Url"))
                var Title = cursor.getString(cursor.getColumnIndex("Title"))
                var Content = cursor.getString(cursor.getColumnIndex("Content"))
                var Excerpt = cursor.getString(cursor.getColumnIndex("Excerpt"))

                //(id: Long, link: String, title:String, content:String, excerpt:String)

                list.add(Post(ID.toLong(),Url,Title,Content,Excerpt))

            }while(cursor.moveToNext())
        }

        adapter!!.notifyDataSetChanged()

        /*var fragmentM = activity.supportFragmentManager

        adapter = MyPostAdapter(activity.applicationContext, list, fragmentM)
        recyclerView!!.adapter = adapter*/
    }


       override fun onResume() {
           super.onResume()
           Log.i("Onresume working----","Yes")
           LoadQuery("%")
           Log.i("Really Onresume how----","Yes")
       }



    fun fetchData(path : String){

        progressBar!!.setVisibility(View.VISIBLE);
        var dbManager = DbManager(activity.applicationContext)
        var values = ContentValues()

        val service = ServiceVolley()

        val apiController = APIController(service)

        apiController.getArray(path)
        { response ->

            try {

                Log.i("PageNo.----", page.toString())
                var len = response!!.length()
                Log.i("Length---------", len.toString() + " max = " + max.toString())
                for (i in 0..len-1) {
                    var jsonObject = response!!.getJSONObject(i)
                    var id = jsonObject.getLong("id")
                    //var date = jsonObject.getString("date_gmt")
                    //var slug = jsonObject.getString("slug")
                    var url = jsonObject.getString("link")

                    //Log.i("___URL_______", url)

                    var tit = jsonObject.getJSONObject("title")
                    var title = tit.getString("rendered")

                    //var media = jsonObject.getString("source_url")

                    var con = jsonObject.getJSONObject("content")
                    var content = con.getString("rendered")

                    //Log.i("content11111---", content)

                    var exc = jsonObject.getJSONObject("excerpt")

                    var str = exc.getString("rendered")

                    var l = str.length

                    var excerpt:String?=null
                    //Log.i("str.get(0)",str[0].toString())
                    if(l!=0)
                    {
                        excerpt = SplitString(str)
                    }
                    else
                    {
                        excerpt = str
                    }

                    Log.i("legth",l.toString())
                    Log.i("excerpt",excerpt)

                    //("i","ID","Url","Title","Content","Excerpt")

                    values.put("ID", id.toInt())
                    values.put("Url", url)
                    values.put("Title", title)
                    values.put("Content", content)
                    values.put("Excerpt", excerpt)

                    var ID = dbManager.Insert(table_Posts, values)

                    list!!.add(Post(id, url, title, content, excerpt!!))

                }
                length = len
                page++
                adapter!!.notifyDataSetChanged()
                progressBar!!.setVisibility(View.GONE);

            }catch (ex:Exception){Log.i("Exceptiiiiion---",ex.toString())
                progressBar!!.setVisibility(View.GONE);}

        }

    }


    fun SplitString(str:String):String
    {
        var string:StringBuilder?=null
        var c = str[1]
        Log.i("c---",c.toString())
        if(c=='u')
        {
            var array = str.split("<ul><li>")
            string = StringBuilder(array[1])
        }
        else if(c=='p')
        {
            var array = str.split("<p>")
            string = StringBuilder(array[1])
        }


        return string.toString()
    }
    companion object {

        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        var page = 1
        var max = 0
        var length = 0

        fun newInstance(param1: String): CrazyPost {
            val fragment = CrazyPost()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }
}
