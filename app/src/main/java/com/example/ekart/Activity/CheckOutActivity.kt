package com.example.ekart.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ekart.MainActivity
import com.example.ekart.R
import com.example.ekart.databinding.ActivityAddressBinding
import com.example.ekart.databinding.ActivityCheckOutBinding
import com.example.ekart.roomdb.AppDatabase
import com.example.ekart.roomdb.ProModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject


class CheckOutActivity : AppCompatActivity() ,PaymentResultListener{

    private  lateinit var binding: ActivityCheckOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val checkout=Checkout()
        checkout.setKeyID("rzp_test_9sG8WU26qKQu1w")
        val price =intent.getStringExtra("totalCost")



        try {
            val options = JSONObject()
            options.put("name", "E-kart")
            options.put("description", "this is best e-commerce app:By->Lokesh thakur")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
          //  options.put("order_id", "order_DBJOWzybf0sJbb") //from response of step 3.
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", (price!!.toInt()*100).toString()) //pass amount in currency subunits
            options.put("prefill.email", "jadounthakur6497@gmail.com")
            options.put("prefill.contact", "9528673797")
            checkout.open(this, options)
        } catch (e:Exception){
            Toast.makeText(this
            ,"something went wrong ",Toast.LENGTH_SHORT).show()


        }

    }

    override fun onPaymentSuccess(p0: String?) {

        Toast.makeText(this,"payment success ",Toast.LENGTH_SHORT).show()



        uploadData()


    }

    private fun uploadData() {


        val id =intent.getStringArrayListExtra("productIds")
        for(currentId in id!!){

            fetchData(currentId)
        }


    }

    private fun fetchData(productId: String?) {

        val dao=AppDatabase.getInstance(this
        ).productDao()


        Firebase.firestore.collection("products")
            .document(productId!!).get().addOnSuccessListener {

                dao.deleteProduct(ProModel(productId))

                saveData(it.getString("productName"),
                it.getString("productSp"),
                   productId )


            }

    }

    private fun saveData(name: String?, price: String?, productId: String) {

        val preference=this.getSharedPreferences("user", MODE_PRIVATE)

        val data= hashMapOf<String,Any>()

        data["name"]=name!!
        data["price"]=price!!
        data["productId"]=productId
        data["status"]="Ordered"
        data["userId"]=preference.getString("number","")!!

        val firestore=Firebase.firestore.collection("allOrders")

        val key=firestore.document().id
        data["orderId"]=key

        firestore.document(key).set(data).addOnSuccessListener {


            Toast.makeText(this, "Ordered Placed",Toast.LENGTH_SHORT).show()

            startActivity(Intent(this,MainActivity::class.java))

            finish()



        }.addOnFailureListener {

            Toast.makeText(this,
                "Something went wrong ",Toast.LENGTH_SHORT).show()

        }



    }

    override fun onPaymentError(p0: Int, p1: String?) {

        Toast.makeText(this,"payment error",Toast.LENGTH_SHORT).show()



    }
}