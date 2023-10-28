package com.example.ekart.Activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ekart.R
import com.example.ekart.databinding.ActivityAddressBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddressActivity : AppCompatActivity() {

private lateinit var binding:ActivityAddressBinding
    private lateinit var preferences:SharedPreferences

    private lateinit var totalCost:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences=this.getSharedPreferences("user", MODE_PRIVATE)

       totalCost =intent.getStringExtra("totalCost")!!

        loadUserInfo()

        binding.proceed.setOnClickListener(){

            validateData(
                
                binding.userNumber.text.toString(),
                binding.userName.text.toString(),
                binding.userPincode.text.toString(),
                binding.userCity.text.toString(),
                binding.userState.text.toString(),
                binding.userVillage.text.toString()
                
                
            )

              startActivity(Intent(this,CheckOutActivity::class.java))

        }
    }

    private fun validateData(number: String, name: String, pinCode: String, city: String,  state:String, village: String) {





        if( state.isEmpty() ||pinCode.isEmpty()||city.isEmpty()||state.isEmpty()||village.isEmpty()){

            Toast.makeText(this,"please fill all entity",Toast.LENGTH_SHORT).show()

        }else{

            storeData(pinCode,city,state,village)
        }


    }

    private fun storeData(
        pinCode: String,
        city: String,
        state: String,
        village: String
    ) {

        val map= hashMapOf<String,Any>()

        map["village"]=village
        map["state"]=state
        map["city"]=city
        map["pinCode"]=pinCode




        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .update(map).addOnSuccessListener {


                val b=Bundle()
                b.putStringArrayList("productIds",intent.getStringArrayListExtra("productIds"))

                b.putString( "totalCost",totalCost)


                val intent=Intent(this,CheckOutActivity::class.java)



                intent.putExtras(b)


                startActivity(intent)




                            }
            .addOnFailureListener {
                Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show()


            }







    }

    private fun loadUserInfo() {


        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .get().addOnSuccessListener {


                binding.userName.setText(it.getString("name"))
                binding.userNumber.setText(it.getString("number"))

                binding.userVillage.setText(it.getString("village"))
                binding.userCity.setText(it.getString("city"))
                binding.userState.setText(it.getString("state"))
                binding.userPincode.setText(it.getString("pinCode"))

            }.addOnFailureListener {

                Toast.makeText(this,"Something went wrong ",Toast.LENGTH_SHORT).show()


            }
    }
}