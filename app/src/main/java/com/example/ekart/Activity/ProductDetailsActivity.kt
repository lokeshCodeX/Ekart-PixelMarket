package com.example.ekart.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.ekart.MainActivity
import com.example.ekart.R

import com.example.ekart.databinding.ActivityProductDetailsBinding
import com.example.ekart.roomdb.AppDatabase
import com.example.ekart.roomdb.ProDao
import com.example.ekart.roomdb.ProModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityProductDetailsBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProductDetailsBinding.inflate(layoutInflater)

        getProductDetails(intent.getStringExtra("id"))


        setContentView(binding.root)

    }

    private fun getProductDetails(proid: String?) {

        Firebase.firestore.collection("products")
            .document(proid!!).get().addOnSuccessListener {

                val list = it.get("productImages") as ArrayList<String>
                val name=it.getString("productName")
                val productSp=it.getString("productSp")
                val productDesc=it.getString("productDescription")

                binding.textView7.text=name
                binding.textView8.text=productSp
                binding.textView9.text=productDesc

                val slideList=ArrayList<SlideModel>()
                for (data in list){
                    slideList.add(SlideModel(data, scaleType = ScaleTypes.CENTER_CROP))
                }


                cartAction(proid,name,productSp,it.getString("productCoverImg"))

                binding.imageSlider.setImageList(slideList)



            }.addOnFailureListener {
                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()

            }



    }

    private fun cartAction(proid: String, name: String?, productSp: String?, coverImg: String?) {


        val productDao=AppDatabase.getInstance(this).productDao()

        if(productDao.isexit(proid)!=null){

            binding.textView10.text="Go to Cart"


        }else{

            binding.textView10.text="Add to Cart"


        }

        binding.textView10.setOnClickListener {
            if(productDao.isexit(proid)!=null){

                openCart()

            }else{
                addToCart(productDao,proid,name,productSp,coverImg)

            }
        }


    }

    private fun addToCart(productDao:ProDao, proid: String, name: String?, productSp: String?, coverImg: String?) {

      val data =ProModel(proid,name,coverImg,productSp)

      lifecycleScope.launch(Dispatchers.IO){

          productDao.insertProduct(data)
          binding.textView10.text="Go To Cart"



      }



    }

    private fun openCart() {

        val preference=this.getSharedPreferences("info", MODE_PRIVATE)
        val editor=preference.edit()
        editor.putBoolean("isCart",true)
        editor.apply()

        startActivity(Intent(this,MainActivity::class.java))

        finish()



    }
}