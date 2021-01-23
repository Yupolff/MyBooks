package com.example.mybooks.helper

import android.database.Cursor
import com.example.mybooks.db.DatabaseContract
import com.example.mybooks.entity.Book

object MappingHelper {

    fun mapCursorToArrayList(booksCursor: Cursor?): ArrayList<Book> {
        val booksList = ArrayList<Book>()

        booksCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.BooksColumns._ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.BooksColumns.TITLE))
                val author = getString(getColumnIndexOrThrow(DatabaseContract.BooksColumns.AUTHOR))
                val genre = getString(getColumnIndexOrThrow(DatabaseContract.BooksColumns.GENRE))
                val quotes = getString(getColumnIndexOrThrow(DatabaseContract.BooksColumns.QUOTES))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.BooksColumns.DATE))
                booksList.add(Book(id, title, author, genre, quotes, date))
            }
        }
        return booksList
    }
}