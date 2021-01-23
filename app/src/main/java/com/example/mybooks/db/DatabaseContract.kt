package com.example.mybooks.db

import android.provider.BaseColumns

class DatabaseContract {
    internal class BooksColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "book"
            const val _ID = "_id"
            const val TITLE = "title"
            const val AUTHOR = "author"
            const val GENRE = "genre"
            const val QUOTES = "quotes"
            const val DATE = "date"
        }
    }
}