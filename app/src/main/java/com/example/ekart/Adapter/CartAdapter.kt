package com.example.ekart.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ekart.Activity.ProductDetailsActivity
import com.example.ekart.databinding.LayoutCartItemBinding
import com.example.ekart.roomdb.AppDatabase
import com.example.ekart.roomdb.ProModel

class CartAdapter(val context: Context, val list: List<ProModel>):
RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

    inner class CartViewHolder(val binding: LayoutCartItemBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

        val binding=LayoutCartItemBinding.inflate(LayoutInflater.from(context),parent,false)

        return  CartViewHolder(binding)


    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        Glide.with(context).load(list[position].productImage).into(holder.binding.imageView4)



        holder.binding.textView11.text=list[position].productName
        holder.binding.textView12.text=list[position].productSp


        holder.itemView.setOnClickListener {


            val intent =Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)

        }

        val dao=AppDatabase.getInstance(context).productDao()


        holder.binding.imageView5.setOnClickListener(){

            dao.deleteProduct(ProModel(
                list[position].productId,
                list[position].productImage,
                list[position].productSp))


        }



    }
}