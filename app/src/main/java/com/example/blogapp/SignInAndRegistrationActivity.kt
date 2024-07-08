package com.example.blogapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.blogapp.Model.UserData
import com.example.blogapp.databinding.ActivitySignInAndRegistrationBinding
import com.example.blogapp.register.WelcomeActivity
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
    private  val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null



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

        database = FirebaseDatabase.getInstance("https://blog-app-437af-default-rtdb.asia-southeast1.firebasedatabase.app")
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

            // Login in User
            binding.loginButton.setOnClickListener {
                val loginEmail = binding.loginEmailaddress.text.toString()
                val loginPass = binding.loginPassword.text.toString()
                if(loginEmail.isEmpty() || loginPass.isEmpty()){
                    Toast.makeText(this, "Please Fill All the Details", Toast.LENGTH_SHORT).show()
                }
                else{
                    auth.signInWithEmailAndPassword(loginEmail, loginPass)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                Toast.makeText(this, "Login Successfull😁", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            else{
                                Toast.makeText(this, "Login Failed. Enter correct details", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }

            // registering user
        } else if (action == "register"){
            binding.loginButton.isEnabled = false
            binding.loginButton.alpha = 0.5f


            // registration
            binding.registerButton.setOnClickListener{

                // Get data from edittextfield
                val registerName = binding.registerName.text.toString()
                val registerEmail = binding.registerEmail.text.toString()
                val registerPass = binding.registerPassword.text.toString()
                if (registerName.isEmpty() || registerEmail.isEmpty() || registerPass.isEmpty()){
                    Toast.makeText(this, "Please Fill All The Details", Toast.LENGTH_SHORT).show()
                }
                else{
                    auth.createUserWithEmailAndPassword(registerEmail, registerPass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                val user = auth.currentUser
                                auth.signOut()
                                // Register User
                                user?.let {
                                    // Save user data in Firebase realtime database
                                    val userReference = database.getReference("users")
                                    val userId:String = user.uid
                                    val userData = UserData(
                                        registerName, registerEmail
                                    )


                                    // LOG CHECKING

                                    // Upload image to firebase storage
                                    val storageReference = storage.reference.child("profile_image/$userId.jpg")
                                    storageReference.putFile(imageUri!!)
                                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, WelcomeActivity::class.java))
                                    finish()
                                }
                            } else{
                                val exception = task.exception
                                Toast.makeText(this, "User Registration Failed: ${exception?.message}", Toast.LENGTH_SHORT).show()

                            }

                        }
                }



            }
        }
        // setOnClickListener for choose image
        binding.cardView2.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST
            )

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null){
            imageUri = data.data
            Glide.with(this)
                .load(imageUri as Uri)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.registerUserImage)
        }
    }
}
