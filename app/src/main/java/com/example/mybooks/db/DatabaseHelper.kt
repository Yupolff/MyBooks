package com.example.mybooks.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mybooks.db.DatabaseContract.BooksColumns.Companion.TABLE_NAME


class DatabaseHelper (context: Context)  : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        private const val DATABASE_NAME = "dbbooksapp"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_BOOKS= "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.BooksColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.BooksColumns.TITLE} TEXT NOT NULL," +
                " ${DatabaseContract.BooksColumns.AUTHOR} TEXT NOT NULL," +
                " ${DatabaseContract.BooksColumns.GENRE} TEXT NOT NULL," +
                " ${DatabaseContract.BooksColumns.QUOTES} TEXT NOT NULL," +
                " ${DatabaseContract.BooksColumns.DATE} TEXT NOT NULL)"
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_BOOKS)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}