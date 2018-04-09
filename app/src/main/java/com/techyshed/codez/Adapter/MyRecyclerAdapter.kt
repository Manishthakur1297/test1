package com.techyshed.codez.Adapter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.techyshed.codez.CategoryFragment.CrazyPost
import com.techyshed.codez.POJO.Category
import com.techyshed.codez.R


/**
 * Created by Mj 2 on 28-Jan-18.
 */

class MyRecyclerAdapter(private val context:Context, private val list:ArrayList<Category>,
                        private val fragmentManager: FragmentManager, private val domain:String)
    : RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.crazy_items, null, false);
        view.setLayoutParams(RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {

        var item = list[position]
        holder!!.title!!.text = list[position].name
        holder!!.posts!!.text = "(" + list[position].count.toString()+")"

        holder.title!!.setOnClickListener(View.OnClickListener {

            //Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show()
            //var intent = Intent(context, BlankFragment::class.java)

            var fm: FragmentManager = fragmentManager
            val transaction = CrazyPost()

            val intent = Bundle()
            intent.putString("domain", domain)
            intent.putString("id",item.id.toString())
            intent.putString("count",item.count.toString())
            intent.putString("url",item.url)
            transaction.arguments = intent


            fm.beginTransaction().replace(R.id.frame_container,transaction).addToBackStack("f").commit()

        })
    }


   /* private fun loadFragment(fragment : CategoryFragment)
    {
        var transaction = fragmentManager
        transaction.replace(R.id.frame_container,fragment)
        transaction.addToBackStack("")
        transaction.commit()
    }*/

    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        //internal var id : TextView?=null
        //internal var date : TextView?=null
        internal var title : TextView?=null
        internal var posts : TextView?=null

        init {

            title = itemView.findViewById(R.id.crazyCategory) as TextView
            posts = itemView.findViewById(R.id.tvPosts) as TextView

            /*id = itemView.findViewById(R.id.tvID) as TextView
            date = itemView.findViewById(R.id.tvDate) as TextView
            image = itemView.findViewById(R.id.iv) as ImageView*/
        }

    }

}

