package com.example.ekart.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ekart.MainActivity
import com.example.ekart.R
import com.example.ekart.databinding.ActivityLoginBinding
import com.example.ekart.databinding.ActivityOtpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button3.setOnClickListener(){

            if(binding.userOtp.text!!.isEmpty()){

                Toast.makeText(this,"Please provide otp",Toast.LENGTH_SHORT).show()

            }
            else
            {
                verifyUser(binding.userOtp.text.toString())
            }
        }
    }

    private fun verifyUser(otp: String) {

        val credential = PhoneAuthProvider.getCredential(intent.getStringExtra("verificationId")!!,otp)
        signInWithPhoneAuthCredential(credential)




    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information



                    val preferences=this
                        .getSharedPreferences("user", MODE_PRIVATE)
                    val editor=preferences.edit()
                    editor.putString("number",intent.getStringExtra("number")!!)


                    editor.apply()

                    startActivity(Intent(this,MainActivity::class.java))


                    finish()
                } else {

                   Toast.makeText(this,"Somethin went wrong dear",Toast.LENGTH_SHORT).show()

                }
            }
    }
}