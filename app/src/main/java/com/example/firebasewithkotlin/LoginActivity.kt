package com.example.firebasewithkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebasewithkotlin.databinding.ActivityLoginBinding
import com.example.firebasewithkotlin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }


        binding.btnLogin.setOnClickListener{
            val email = binding.edtEmail.text.toString()
            val pass = binding.edtPass.text.toString()


            if(email.isNotEmpty() && pass.isNotEmpty()){
                login(email,pass)
            }
        }


    }

    private fun login(email: String, pass: String) {


        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                   val intent  = Intent(this,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this,task.exception.toString(),Toast.LENGTH_SHORT).show()

                }
            }

    }
}