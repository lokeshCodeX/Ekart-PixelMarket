package com.example.ekart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat.getCategory
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ekart.Adapter.CategoryAdapter
import com.example.ekart.Adapter.ProductAdapter
import com.example.ekart.Model.AddProductModel
import com.example.ekart.Model.CategoryModel
import com.example.ekart.R
import com.example.ekart.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentHomeBinding.inflate(layoutInflater)


        val preference=requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)


        if(preference.getBoolean("isCart",false))

            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)



        getSliderImage()
        getCategory()
        getProduct()

        return binding.root
    }

    private fun getSliderImage() {

        Firebase.firestore.collection("slider").document("item").get()

            .addOnSuccessListener {

                Glide.with(requireContext()).load(it.get("img")).into(binding.sliderImage)


        }



    }

    private fun getProduct() {


        val list=ArrayList<AddProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                list.clear()

                for(doc in it.documents){

                    val data=doc.toObject(AddProductModel::
                    class.java)
                    list.add(data!!)


                }
               binding.productRecycler.adapter=ProductAdapter(requireContext(),list)
            }




    }

    private fun getCategory() {


        val list=ArrayList<CategoryModel>()
        Firebase.firestore.collection("category")
            .get().addOnSuccessListener {
                list.clear()

                for(doc in it.documents){

                    val data=doc.toObject(CategoryModel::
                    class.java)
                    list.add(data!!)


                }
                binding.categoryRecycler.adapter=CategoryAdapter(requireContext(),list)
            }



    }


}