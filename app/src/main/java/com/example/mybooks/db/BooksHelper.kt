package com.example.mybooks.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.mybooks.db.DatabaseContract.BooksColumns.Companion.TABLE_NAME
import com.example.mybooks.db.DatabaseContract.BooksColumns.Companion._ID
import java.sql.SQLException

class BooksHelper  (context: Context){
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: BooksHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): BooksHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BooksHelper(context)
            }
    }
    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC")
    }
    init {
        dataBaseHelper = DatabaseHelper(context)
    }
    fun queryById(id: String): Cursor {
        return database.query(DATABASE_TABLE, null, "$_ID = ?", arrayOf(id), null, null, null, null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

}