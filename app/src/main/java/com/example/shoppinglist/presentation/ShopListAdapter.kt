package com.example.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    companion object {
        private const val VIEW_TYPE_ENABLED = 101
        private const val VIEW_TYPE_DISABLED = 100
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
//        val layoutResId = if (viewType == VIEW_TYPE_ENABLED) {
//            R.layout.item_shop_enabled
//        } else {
//            R.layout.item_shop_disabled
//        }
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_shop_disabled,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

//    override fun getItemViewType(position: Int): Int {
//        val shopItem = shopList[position]
//        return if (shopItem.enabled) {
//            VIEW_TYPE_ENABLED
//        } else {
//            VIEW_TYPE_DISABLED
//        }
//    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        if (!shopItem.enabled) {
            holder.tvName.text = shopItem.name
            holder.tvCount.text = shopItem.count.toString()
            holder.tvName.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    android.R.color.holo_red_light
                )
            )
        }
    }

    override fun onViewRecycled(holder: ShopItemViewHolder) {
        holder.tvName.text = ""
        holder.tvCount.text = ""
        holder.tvName.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                android.R.color.white
            )
        )
    }

    override fun getItemCount() = shopList.size

    class ShopItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvCount: TextView = itemView.findViewById(R.id.tv_count)
    }
}