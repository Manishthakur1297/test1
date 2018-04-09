package com.techyshed.codez.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager{

    var sqlDB:SQLiteDatabase?=null
    //var dbTable:String?=null

    constructor(context: Context)
    {
        var db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
        //this.dbTable = dbTable
    }

    val dbName ="Programming"
    val table_Category = "category"
    val table_Posts = "posts"
    val table_Recent = "recent"
    val table_Download = "download"
    val i = "i"
    val colID = "ID"
    val colTitle = "Title"
    val colCount = "Count"
    val colContent = "Content"
    val colUrl = "Url"
    val colExcerpt = "Excerpt"
    val dbVersion = 1

    val sqlCreateTableCategory = "CREATE TABLE IF NOT EXISTS "+ table_Category + " (" + i + " INTEGER PRIMARY KEY, "+ colID +
            " INTEGER, "+ colTitle + " TEXT, "+ colCount +" INTEGER , "+ colUrl + " TEXT);"

    //id,url,title,content,excerpt
    val sqlCreateTablePosts = "CREATE TABLE IF NOT EXISTS "+ table_Posts + " (" + i + " INTEGER PRIMARY KEY, " + colID +
            " INTEGER, " + colUrl + " TEXT, " + colTitle + " TEXT, "+ colContent +" TEXT, "+ colExcerpt +" TEXT  );"

    val sqlCreateTableRecent = "CREATE TABLE IF NOT EXISTS "+ table_Recent + " (" + i + " INTEGER PRIMARY KEY, " + colID +
            " INTEGER, " + colUrl + " TEXT, " + colTitle + " TEXT, "+ colContent +" TEXT, "+ colExcerpt +" TEXT  );"

    val sqlCreateTableDownload = "CREATE TABLE IF NOT EXISTS "+ table_Download + " (" + i + " INTEGER PRIMARY KEY, " +
            colUrl + " TEXT, "+ colTitle + " TEXT, "+ colContent +" TEXT, "+ colExcerpt +" TEXT  );"

    inner class DatabaseHelperNotes:SQLiteOpenHelper{

        var context:Context?=null
        constructor(context:Context):super(context,dbName,null,dbVersion){
            this.context = context
        }

        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(sqlCreateTableCategory)
            p0!!.execSQL(sqlCreateTablePosts)
            p0!!.execSQL(sqlCreateTableRecent)
            p0!!.execSQL(sqlCreateTableDownload)
            //Toast.makeText(context,"Database and Table Created",Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("DROP TABLE IF EXISTS "+ table_Category)
            p0!!.execSQL("DROP TABLE IF EXISTS "+ table_Posts)
            p0!!.execSQL("DROP TABLE IF EXISTS "+ table_Recent)
            //p0!!.execSQL("DROP TABLE IF EXISTS "+ table_Download)
        }



    }

    fun Insert(dbTable :String, values:ContentValues):Long{
        val ID = sqlDB!!.insert(dbTable,"",values)
        return ID
    }



    fun Query(dbTable: String, projection: Array<String>,selection:String,selectionArgs: Array<String>,SorOrder:String):Cursor{
        //projection --- column select , cursor ---- like a table , selection ---- rows to select , Sort data or not ---- SorOrder

        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(sqlDB,projection,selection,selectionArgs,null,null,SorOrder)
        return cursor
    }

    fun Delete(dbTable: String, selection:String):Int{
        val count = sqlDB!!.delete(dbTable,selection,null)
        return count
    }

    fun DeletePost(dbTable: String,selection:String,selectionArgs:Array<String>):Int{
        val count = sqlDB!!.delete(dbTable,selection,selectionArgs)
        return count
    }

    fun Update(dbTable: String, values:ContentValues,selection:String,selectionArgs: Array<String>):Int{
        var count = sqlDB!!.update(dbTable,values,selection,selectionArgs)
        return count
    }

}