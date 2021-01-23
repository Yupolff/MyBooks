package com.example.mybooks

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybooks.adapter.BooksAdapter
import com.example.mybooks.db.BooksHelper
import com.example.mybooks.entity.Book
import com.example.mybooks.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: BooksAdapter
    private lateinit var bookHelper: BooksHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Books"

        rv_books.layoutManager = LinearLayoutManager(this)
        rv_books.setHasFixedSize(true)
        adapter = BooksAdapter(this)
        rv_books.adapter = adapter

        fab_add.setOnClickListener {
            val intent = Intent(this@MainActivity, BookAddUpdateActivity::class.java)
            startActivityForResult(intent, BookAddUpdateActivity.REQUEST_ADD)
        }

        bookHelper = BooksHelper.getInstance(applicationContext)
        bookHelper.open()

        if (savedInstanceState == null) {
            loadBooksAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Book>(EXTRA_STATE)
            if (list != null) {
                adapter.listBooks= list
            }
        }
    }

    private fun loadBooksAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val deferredBooks = async(Dispatchers.IO) {
                val cursor = bookHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressbar.visibility = View.INVISIBLE
            val books = deferredBooks.await()
            if (books.size > 0) {
                adapter.listBooks = books
            } else {
                adapter.listBooks = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listBooks)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                BookAddUpdateActivity.REQUEST_ADD -> if (resultCode == BookAddUpdateActivity.RESULT_ADD) {
                    val book = data.getParcelableExtra<Book>(BookAddUpdateActivity.EXTRA_BOOK)

                    if (book != null) {
                        adapter.addItem(book)
                    }
                    rv_books.smoothScrollToPosition(adapter.itemCount - 1)

                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }
                BookAddUpdateActivity.REQUEST_UPDATE ->
                    when (resultCode) {
                        BookAddUpdateActivity.RESULT_UPDATE -> {

                            val book = data.getParcelableExtra<Book>(BookAddUpdateActivity.EXTRA_BOOK)
                            val position = data.getIntExtra(BookAddUpdateActivity.EXTRA_POSITION, 0)

                            if (book != null) {
                                adapter.updateItem(position, book)
                            }
                            rv_books.smoothScrollToPosition(position)

                            showSnackbarMessage("Satu item berhasil diubah")
                        }

                        BookAddUpdateActivity.RESULT_DELETE -> {
                            val position = data.getIntExtra(BookAddUpdateActivity.EXTRA_POSITION, 0)

                            adapter.removeItem(position)

                            showSnackbarMessage("Satu item berhasil dihapus")
                        }
                    }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bookHelper.close()
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_books, message, Snackbar.LENGTH_SHORT).show()
    }


}
