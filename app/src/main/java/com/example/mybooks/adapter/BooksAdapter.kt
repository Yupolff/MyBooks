package com.example.mybooks.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mybooks.BookAddUpdateActivity
import com.example.mybooks.CustomOnItemClickListener
import com.example.mybooks.R
import com.example.mybooks.entity.Book
import kotlinx.android.synthetic.main.item_books.view.*

class BooksAdapter (private val activity: Activity): RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {
    var listBooks = ArrayList<Book>()
        set(listBooks) {
            if (listBooks.size > 0) {
                this.listBooks.clear()
            }
            this.listBooks.addAll(listBooks)

            notifyDataSetChanged()
        }

    fun addItem(book: Book) {
        this.listBooks.add(book)
        notifyItemInserted(this.listBooks.size - 1)
    }

    fun updateItem(position: Int, book: Book) {
        this.listBooks[position] = book
        notifyItemChanged(position, book)
    }

    fun removeItem(position: Int) {
        this.listBooks.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listBooks.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_books, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(listBooks[position])
    }

    override fun getItemCount(): Int = this.listBooks.size

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(book: Book) {
            with(itemView){
                tv_item_title.text = book.title
                tv_item_date.text = book.date
                tv_item_author.text = book.author
                tv_item_genre.text = book.genre
                tv_item_quotes.text = book.quotes
                cv_item_book.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, BookAddUpdateActivity::class.java)
                        intent.putExtra(BookAddUpdateActivity.EXTRA_POSITION, position)
                        intent.putExtra(BookAddUpdateActivity.EXTRA_BOOK, book)
                        activity.startActivityForResult(intent, BookAddUpdateActivity.REQUEST_UPDATE)
                    }
                }))
            }
        }
    }
}