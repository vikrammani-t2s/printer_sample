package com.tcpprintersear.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tcpprintersear.app.`interface`.ItemCallback
import com.tcpprintersear.app.model.IPSearchModel

class ItemAdapter(private var items: List<IPSearchModel>,var itemCallback: ItemCallback) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    var selectPosition =-1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.itemView.setOnClickListener {
            selectPosition = holder.adapterPosition
            itemCallback.itemClick(item.strIp)
        }
        holder.bind(item)
    }
    fun updateAdapter( updates: List<IPSearchModel>){
          this.items = updates
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.text_view)

        fun bind(item: IPSearchModel) {
            textView.text = item.strIp +" "+item.strMac
        }
    }
}
