package com.techyshed.codez.POJO


/**
 * Created by Mj 2 on 27-Jan-18.
 */

class Post {

    var id: Long?=null

   // var date: String?=null

   // var slug: String?=null

    var link: String?=null

    var title:String?=null

    var content: String?=null

    var excerpt: String?=null

    constructor(id: Long, link: String, title:String, content:String, excerpt:String)
    {
        this.id = id
       // this.date = date
        this.link = link
       // this.slug = slug
        this.title = title
        this.content = content
        this.excerpt = excerpt
    }

}


/*class Title(
        val rendered:String?=null
)

class Content(
        val rendered: String?=null
)

class Excerpt(
        val rendered: String?=null
)

class Result (
        val items: List<Post>?=null

)*/
