package com.techyshed.codez.Adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.techyshed.codez.CategoryFragment.CrazyContent
import com.techyshed.codez.ContentActivity
import com.techyshed.codez.Database.DbManager
import com.techyshed.codez.POJO.Post
import com.techyshed.codez.R
import com.techyshed.codez.RecentFragment.DownloadFragment
import java.lang.Character.toLowerCase

class MyPostDownloadAdapter(private val context: Context, private var list:ArrayList<Post>,
                            private val fragmentManager: FragmentManager, private val domain:String)
    : RecyclerView.Adapter<MyPostDownloadAdapter.MyViewHolder>(),Filterable{

    var mFilteredList = list
    val listOriginal = list

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                val charString = charSequence.toString()

                if (charString.isEmpty()) {

                    mFilteredList = listOriginal
                } else {

                    val filteredList = ArrayList<Post>()

                    for (post in listOriginal) {

                        if (post.title!!.toLowerCase().contains(charString)){

                            filteredList.add(post)
                        }
                    }

                    mFilteredList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = mFilteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mFilteredList = filterResults.values as ArrayList<Post>
                list = mFilteredList
               notifyDataSetChanged()
            }
        }
    }


    val table_Download = "download"

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.crazy_post_download_items, null, false);
        view.setLayoutParams(RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return MyViewHolder(view)
    }


        override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {

        var item = list[position]
        holder!!.title!!.text = list[position].title
        //holder!!.budelete!!.setText("Del")
        holder!!.excerpt!!.text = list[position].excerpt

        holder!!.budelete!!.setOnClickListener({

            var dbManager = DbManager(context)
            var selectionArgs = arrayOf(item.link.toString())
            dbManager.DeletePost(table_Download,"Url=?",selectionArgs)
            list.remove(item)
            notifyDataSetChanged()
        })

        holder.title!!.setOnClickListener(View.OnClickListener {

            var fm: FragmentManager = fragmentManager
            val transaction = CrazyContent.newInstance(domain)

            //val intent = Bundle()
            /*intent.putString("domain", domain)
            intent.putString("url",item.link)
            intent.putString("title",item.title)
            intent.putString("content",item.content)
            intent.putString("excerpt",item.excerpt)
            transaction.arguments = intent*/
            val intent = Intent(context,ContentActivity::class.java)
            intent.putExtra("domain", domain)
            intent.putExtra("url",item.link)
            intent.putExtra("title",item.title)
            intent.putExtra("content",item.content)
            intent.putExtra("excerpt",item.excerpt)
            context.startActivity(intent)
            //fm.beginTransaction().replace(R.id.frame_container,transaction).addToBackStack("f").commit()

        })


    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        internal var title : TextView?=null
        internal var excerpt : TextView?=null
        internal var budelete : Button?=null

        init {

            title = itemView.findViewById(R.id.crazyTitle) as TextView
            excerpt = itemView.findViewById(R.id.crazyExcerpt) as TextView
            budelete = itemView.findViewById(R.id.buDelete) as Button


        }

    }


}
