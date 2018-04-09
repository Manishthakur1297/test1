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
import com.techyshed.codez.Adapter.MyRecyclerAdapter
import com.techyshed.codez.Controller.APIController
import com.techyshed.codez.Controller.ServiceVolley
import com.techyshed.codez.POJO.Category
import com.techyshed.codez.Database.DbManager
import com.techyshed.codez.R


class CrazyFragment() : Fragment() {

    var domain: String? = null
    //var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    //private var mParam2: String? = null

    val table_Category = "category"

    var list = ArrayList<Category>()
    var isScrolling = false
    private var recyclerView: RecyclerView? = null
    private var adapter: MyRecyclerAdapter? = null

    var progressBar: ProgressBar? = null

    //val path = "https://www.thecrazyprogrammer.com/wp-json/wp/v2/categories?per_page=90&page="
    //val link = "https://www.thecrazyprogrammer.com/wp-json/wp/v2/posts?categories="

    val path = "wp-json/wp/v2/categories?per_page=90&page="
    val link = "wp-json/wp/v2/posts?categories="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            domain = arguments.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_crazy, container, false)

        Log.i("Deleted again----","Mastoiii")
        Log.i("Onresume working----","No")
        page = 1
        Log.i("Page----", page.toString())
        progressBar = view.findViewById(R.id.progressBar) as ProgressBar
        recyclerView = view.findViewById(R.id.recyclerCrazy) as RecyclerView
        var layoutManager = LinearLayoutManager(activity.applicationContext)
        recyclerView!!.isNestedScrollingEnabled = false
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)

        var fragmentM = activity.supportFragmentManager

        adapter = MyRecyclerAdapter(activity.applicationContext, list, fragmentM , domain!!)
        recyclerView!!.adapter = adapter

        //LoadQuery("%")

        //fragmentM = activity.supportFragmentManager
        //myPostAdapter = MyPostAdapter(activity.applicationContext,list!!,fragmentM!!)
        //recyclerView!!.adapter = myPostAdapter

        fetchData(domain + path + page)

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
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

                if (isScrolling && (first + visible == total)) {
                    isScrolling = false


                    if (page <= max && length > 0) {
                        Toast.makeText(activity.applicationContext, "Fetching More Posts.....", Toast.LENGTH_SHORT).show()

                        fetchData(domain + path + page)
                    } else {
                        Toast.makeText(activity.applicationContext, "No More Posts", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        })

        return view
    }


    override fun onDestroy() {
        super.onDestroy()

        var dbManager = DbManager(activity.applicationContext)
        var del = dbManager.Delete(table_Category,"1")
        Log.i("Page Before----", page.toString())
        page = 1
        Log.i("Page After----", page.toString())
    }

    fun LoadQuery(title:String){
        var dbManager = DbManager(activity.applicationContext)
        var projection = arrayOf("i","ID","Title","Count","Url")
        var selectionArgs = arrayOf(title)
        var cursor = dbManager.Query(table_Category,projection,"Title like ?",selectionArgs,"i"+" ASC")
        list.clear()

        if(cursor.moveToFirst())
        {
            do{

                var ID = cursor.getInt(cursor.getColumnIndex("ID"))
                var Title = cursor.getString(cursor.getColumnIndex("Title"))
                var Count = cursor.getInt(cursor.getColumnIndex("Count"))
                var Url = cursor.getString(cursor.getColumnIndex("Url"))

                list.add(Category(ID.toLong(),Count,Title,Url))

            }while(cursor.moveToNext())
        }

        adapter!!.notifyDataSetChanged()
    }


    override fun onResume() {
        super.onResume()
       // Log.i("Onresume working----","Yes")
       LoadQuery("%")
       // Log.i("Really Onresume how----","Yes")
    }

    fun fetchData(path: String) {

        progressBar!!.setVisibility(View.VISIBLE);

        var dbManager = DbManager(activity.applicationContext)
        var values = ContentValues()

        Log.i("contentvalues----","Yes")
        val service = ServiceVolley()

        Log.i("service----","Yes")
        val apiController = APIController(service)

        Log.i("Apicontroller----","Yes")
        apiController.getArray(path)
        { response ->

            Log.i("response----","Yes")
            // Parse the result
            try {
                //Log.i("Response----", "/post request OK! Response9999: $response")
                Log.i("PageNo.----", page.toString())
                var len = response!!.length()
                //Log.i("Length---------", len.toString())

                var del = dbManager.Delete(table_Category,"1")

                for (i in 0..len - 1) {
                    var jsonObject = response!!.getJSONObject(i)
                    var id = jsonObject.getLong("id")
                    var count = jsonObject.getInt("count")
                    var name = jsonObject.getString("name")
                    var url = link + id

                    Log.i("___URL_______", url)


                    values.put("ID", id.toInt())
                    values.put("Title", name)
                    values.put("Count", count)
                    values.put("Url", url)

                    if (count == 0) {

                    } else {
                        var ID = dbManager.Insert(table_Category, values)
                        list!!.add(Category(id, count, name, url))
                    }
                }
                length = len
                page++
                //adapter!!.notifyDataSetChanged()
                LoadQuery("%")
                progressBar!!.setVisibility(View.GONE);

            }catch (ex:Exception){Log.i("Exceptiiiiion---",ex.toString())
                progressBar!!.setVisibility(View.GONE);}

        }

    }


    companion object {

        private val ARG_PARAM1 = "param1"
        //private val ARG_PARAM2 = "param2"

        var page = 1
        val max = 5
        var length = 0

        fun newInstance(param1: String): CrazyFragment {
            val fragment = CrazyFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }
}
