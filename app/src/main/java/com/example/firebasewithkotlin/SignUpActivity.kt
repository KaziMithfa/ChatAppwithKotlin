package com.example.firebasewithkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebasewithkotlin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var mAuth : FirebaseAuth
    private lateinit var databaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()


        binding.btnSignUpS.setOnClickListener{
            val name = binding.edtNameS.text.toString()
            val email = binding.edtEmailS.text.toString()
            val pass = binding.edtPassS.text.toString()

            if(name.isEmpty()){
                Toast.makeText(this,"Please, insert the name first.....", Toast.LENGTH_SHORT).show()
            }
            
            if(email.isNotEmpty() && pass.isNotEmpty()){
                signUp(email,pass,name)
            }
        }
    }

    private fun signUp(email: String, pass: String,name: String) {


        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    addDatatoFirebase(name,email,mAuth.currentUser?.uid!!)

                    val intent = Intent(this@SignUpActivity,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {

                    Toast.makeText(
                        baseContext,
                        "Some error occured",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }

    }

    private fun addDatatoFirebase(name: String, email: String, uid: String) {

        databaseRef = FirebaseDatabase.getInstance().getReference()

        val user = Users(name,email,uid)

        databaseRef.child("Users").child(uid)
            .setValue(user).addOnCompleteListener{


                Toast.makeText(this,"You have created your account successfully....", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener{

                Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
            }







    }
}