package com.example.smd_final
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity: AppCompatActivity() {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_page)

        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val login=findViewById<TextView>(R.id.goToLogin)


        login.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(android.R.id.content, LoginFragment())
                .addToBackStack(null)
                .commit()

        }

        signUpButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.signUpEmail).text.toString().trim()
            val password = findViewById<EditText>(R.id.signUpPassWord).text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid != null) {
                        val fragment = FillRecordForm().apply {
                            arguments = Bundle().apply {
                                putString("uid", uid)
                            }
                        }

                        supportFragmentManager
                            .beginTransaction()
                            .replace(android.R.id.content, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
