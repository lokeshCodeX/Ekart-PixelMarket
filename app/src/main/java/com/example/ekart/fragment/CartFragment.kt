package com.example.ekart.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.ekart.Activity.AddressActivity
import com.example.ekart.Adapter.CartAdapter
import com.example.ekart.CategoryActivity
import com.example.ekart.R
import com.example.ekart.databinding.FragmentCartBinding
import com.example.ekart.roomdb.AppDatabase
import com.example.ekart.roomdb.ProModel


class CartFragment : Fragment() {

    private lateinit var binding:FragmentCartBinding

    private lateinit var list: ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentCartBinding.inflate(layoutInflater)

        val preference=requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor=preference.edit()
        editor.putBoolean("isCart",false)
        editor.apply()

        val dao =AppDatabase.getInstance(requireContext()).productDao()

        list=ArrayList()

        dao.getAllProducts().observe(requireActivity()){
            binding.cartRecycler.adapter=CartAdapter(requireContext(),it)


            list.clear()
            for (data in it){

              list.add(data.productId)


            }
            totalCost(it)
        }


        return binding.root
    }

    private fun totalCost(data: List<ProModel>?) {

        var total=0
        for(item in data!!){
            total+=item.productSp!!.toInt()


        }

        binding.textView12.text="Total item in cart is ${data.size}"
        binding.textView13.text="total payment : $total"

        binding.checkOut.setOnClickListener(){

            val intent = Intent(context,AddressActivity ::class.java)

            val b=Bundle()
            b.putStringArrayList("productIds",list)

            b.putString( "totalCost",total.toString())




            intent.putExtras(b)


            startActivity(intent)

        }


    }

}
