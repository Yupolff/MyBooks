package com.example.mybooks

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mybooks.db.BooksHelper
import com.example.mybooks.db.DatabaseContract
import com.example.mybooks.db.DatabaseContract.BooksColumns.Companion.DATE
import com.example.mybooks.entity.Book
import kotlinx.android.synthetic.main.activity_book_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class BookAddUpdateActivity : AppCompatActivity() , View.OnClickListener {
    private var isEdit = false
    private var book: Book? = null
    private var position: Int = 0
    private lateinit var bookHelper: BooksHelper

    companion object {
        const val EXTRA_BOOK = "extra_book"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_add_update)

        bookHelper = BooksHelper.getInstance(applicationContext)
        bookHelper.open()

        book = intent.getParcelableExtra(EXTRA_BOOK)
        if (book != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            book = Book()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Ubah"
            btnTitle = "Update"

            book?.let {
                edt_title.setText(it.title)
                edt_author.setText(it.author)
                edt_genre.setText(it.genre)
                edt_quotes.setText(it.quotes)
            }

        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_submit.text = btnTitle
        btn_submit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_submit) {
            val title = edt_title.text.toString().trim()
            val author = edt_author.text.toString().trim()
            val genre = edt_genre.text.toString().trim()
            val quotes = edt_quotes.text.toString().trim()

            if (title.isEmpty()) {
                edt_title.error = "Field can not be blank"
                return
            }

            book?.title = title
            book?.author = author
            book?.genre = genre
            book?.quotes = quotes

            val intent = Intent()
            intent.putExtra(EXTRA_BOOK, book)
            intent.putExtra(EXTRA_POSITION, position)

            val values = ContentValues()
            values.put(DatabaseContract.BooksColumns.TITLE, title)
            values.put(DatabaseContract.BooksColumns.AUTHOR, author)
            values.put(DatabaseContract.BooksColumns.GENRE, genre)
            values.put(DatabaseContract.BooksColumns.QUOTES, quotes)

            if (isEdit) {
                val result = bookHelper.update(book?.id.toString(), values).toLong()
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(this@BookAddUpdateActivity, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
                }
            } else {
                book?.date = getCurrentDate()
                values.put(DATE, getCurrentDate())
                val result = bookHelper.insert(values)

                if (result > 0) {
                    book?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@BookAddUpdateActivity, "Gagal menambah data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

        private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd ", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            dialogTitle = "Hapus Buku"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, id ->
                if (isDialogClose) {
                    finish()
                } else {
                    val result = bookHelper.deleteById(book?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@BookAddUpdateActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}