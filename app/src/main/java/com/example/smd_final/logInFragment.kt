package com.example.smd_final

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.log_in_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val emailInput = view.findViewById<EditText>(R.id.signUpEmail)
        val passwordInput = view.findViewById<EditText>(R.id.signUpPassWord)
        val loginBtn = view.findViewById<Button>(R.id.logInBtn)
        val forgotPasswordTxt = view.findViewById<TextView>(R.id.forgotPasswordLink)
        val goToSignUp = view.findViewById<TextView>(R.id.goToSignUp)
        val page=view.findViewById<FrameLayout>(R.id.loginPage)


        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                    page.removeAllViews()
                    val frag = ListFragment()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.loginPage,frag)
                        .addToBackStack(null)
                        .commit()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        forgotPasswordTxt.setOnClickListener {
            val email = emailInput.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Enter your email to reset password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Password reset email sent", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to send reset email: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        goToSignUp.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity()::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
