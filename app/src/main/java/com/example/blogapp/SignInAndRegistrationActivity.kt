package com.example.blogapp

import android.os.Bundle
import android.service.autofill.UserData
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.blogapp.databinding.ActivitySignInAndRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SignInAndRegistrationActivity : AppCompatActivity() {
    private val binding: ActivitySignInAndRegistrationBinding by lazy {
        ActivitySignInAndRegistrationBinding.inflate(layoutInflater)
    }


    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()



        // for visisbility of fields
        val action = intent.getStringExtra("action")
        // adjust visibility and for login
        if (action == "login"){
            binding.loginEmailaddress.visibility = View.VISIBLE
            binding.loginPassword.visibility = View.VISIBLE
            binding.loginButton.visibility = View.VISIBLE


            binding.registerButton.isEnabled = false
            binding.registerButton.alpha = 0.5f
            binding.registerName.visibility = View.GONE
            binding.registerEmail.visibility = View.GONE
            binding.registerPassword.visibility = View.GONE
            binding.cardView2.visibility = View.GONE
            binding.registerNewuser.isEnabled = false
            binding.registerNewuser.alpha = 0.5f


        } else if (action == "register"){
            binding.loginButton.isEnabled = false
            binding.loginButton.alpha = 0.5f


            // registration
            binding.registerButton.setOnClickListener{

                // Get data from edittextfield
                val registerName = binding.registerName.text.toString()
                val registerEmail = binding.registerEmail.text.toString()
                val registerPass = binding.registerEmail.text.toString()
                if (registerName.isEmpty() || registerEmail.isEmpty() || registerPass.isEmpty()){
                    Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
                }
                else{
                    auth.createUserWithEmailAndPassword(registerEmail, registerPass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                val user = auth.currentUser
                                user?.let {
                                    val userReference = database.getReference("users")
                                    val userId:String = user.uid
                                    val userData = UserData(registerName, registerPass)

                                }
                            } else{

                            }

                        }
                }



            }
        }
    }
}