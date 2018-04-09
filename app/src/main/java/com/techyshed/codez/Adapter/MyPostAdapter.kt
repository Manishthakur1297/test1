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
import android.widget.TextView
import com.techyshed.codez.CategoryFragment.CrazyContent
import com.techyshed.codez.ContentActivity
import com.techyshed.codez.Database.DbManager
import com.techyshed.codez.POJO.Post
import com.techyshed.codez.R


class MyPostAdapter(private val context: Context, private val list:ArrayList<Post> ,
                    private val fragmentManager: FragmentManager, private val domain:String)
    : RecyclerView.Adapter<MyPostAdapter.MyViewHolder>(){

    val table_Download = "download"

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.crazy_post_items, null, false);
        view.setLayoutParams(RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {

        var item = list[position]
        holder!!.title!!.text = list[position].title
        holder!!.excerpt!!.text = list[position].excerpt
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

        /*holder.title!!.setOnLongClickListener(View.OnLongClickListener {

            var alertDialog = AlertDialog.Builder(context).create()
            alertDialog.setCancelable(true)
            alertDialog.setTitle("Remove Post")
            alertDialog.setMessage("Do you want to Remove Article ?")
            alertDialog.setButton(android.R.string.yes,null, DialogInterface.OnClickListener {
                dialogInterface, i ->

                    var dbManager = DbManager(context)
                    var selectionArgs = arrayOf(item.link.toString())
                    dbManager.DeletePost(table_Download,"Url=?",selectionArgs)

            } )
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
                    alertDialog.show()
            true
        })*/
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        internal var title : TextView?=null
        internal var excerpt : TextView?=null

        init {


            title = itemView.findViewById(R.id.crazyTitle) as TextView
            excerpt = itemView.findViewById(R.id.crazyExcerpt) as TextView


        }

    }


}
