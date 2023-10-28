package com.example.ekart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ekart.Adapter.AllOrderAdapter
import com.example.ekart.Model.AllOrderModel
import com.example.ekart.R
import com.example.ekart.databinding.AllOrderItemLayoutBinding
import com.example.ekart.databinding.FragmentMoreBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MoreFragment : Fragment() {

private  lateinit var binding: FragmentMoreBinding

private lateinit var list: ArrayList<AllOrderModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentMoreBinding.inflate(layoutInflater)

        list= ArrayList()

        val preference=requireContext().getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)




        Firebase.firestore.collection("allOrders").whereEqualTo("userId",preference.getString("number","")).get().addOnSuccessListener {

            list.clear()

            for (doc in it) {

                val data = doc.toObject(AllOrderModel::class.java)
                list.add(data)

            }

            binding.recyclerView.adapter = AllOrderAdapter(list, requireContext())

        }
        return binding.root

    }

}