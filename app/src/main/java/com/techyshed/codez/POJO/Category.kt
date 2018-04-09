package com.techyshed.codez.POJO

/**
 * Created by Mj 2 on 30-Jan-18.
 */
class Category{

    var id: Long?=null
    var count:Int?=null
    var name:String?=null
    var url:String?=null

    constructor(id: Long, count: Int, name: String, url: String)
    {
        this.id = id
        this.count = count
        this.name = name
        this.url = url
    }
}