package com.amazing.ebookapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amazing.ebookapp.R
import com.amazing.ebookapp.model.Books
import com.bumptech.glide.Glide

class DataAdapter(
    private val onClick: (Books) -> Unit,
    private val longOnClick: (Books) -> Unit
) : ListAdapter<Books, DataAdapter.DataViewHolder>(DiffCallback()) {

    inner class DataViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val bookName = view.findViewById<TextView>(R.id.tvBookName)
        val description = view.findViewById<TextView>(R.id.tvDescription)
        val author = view.findViewById<TextView>(R.id.tvAuthor)
        val category = view.findViewById<TextView>(R.id.tvCategory)
        val bookImage = view.findViewById<ImageView>(R.id.imageView)

    }

    class DiffCallback() : DiffUtil.ItemCallback<Books>() {
        override fun areItemsTheSame(oldItem: Books, newItem: Books): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Books, newItem: Books): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.bookName.text = item.bookName
        holder.description.text = item.description
        holder.author.text = item.author
        holder.category.text = item.category

        Glide.with(holder.itemView.context).load(item.bookImageUrl)
            .placeholder(R.drawable.placeholder).error(R.drawable.brokenimage)
            .into(holder.bookImage)

        holder.itemView.setOnClickListener {
            onClick(item)
        }

        holder.itemView.setOnLongClickListener {
            longOnClick(item)
            true
        }


    }
}