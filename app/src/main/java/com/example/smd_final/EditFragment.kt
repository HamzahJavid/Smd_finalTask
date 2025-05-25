package com.example.smd_final

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditForm() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.edit_credentials, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = view.findViewById<EditText>(R.id.editStudentId)
        val department = view.findViewById<EditText>(R.id.editDepartment)
        val registrationDate = view.findViewById<EditText>(R.id.editDate)
        val name = view.findViewById<EditText>(R.id.editName)
        val yearOfStudy = view.findViewById<EditText>(R.id.editYear)
        val saveButton = view.findViewById<Button>(R.id.btnSave)

        saveButton.setOnClickListener {
            val studentIdInput = id.text.toString().trim()
            val deptInput = department.text.toString().trim()
            val registrationDateInput = registrationDate.text.toString().trim()
            val nameInput = name.text.toString().trim()
            val yearOfStudyInput = yearOfStudy.text.toString().trim()

            if (studentIdInput.isEmpty()) {
                Toast.makeText(requireContext(), "Student ID cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val uid = FirebaseAuth.getInstance().currentUser?.uid


            val recordsRef = FirebaseDatabase.getInstance().getReference("Records")

            val query = recordsRef.orderByChild("studentId").equalTo(studentIdInput)
            query.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    Toast.makeText(requireContext(), "Student ID already exists", Toast.LENGTH_SHORT).show()
                } else {
                    val record = Record(
                        studentId = studentIdInput,
                        name = nameInput,
                        department = deptInput,
                        yearOfStudy = yearOfStudyInput,
                        dateOfRegistration = registrationDateInput
                    )

                    recordsRef.child(uid.toString()).setValue(record)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Record saved", Toast.LENGTH_SHORT).show()
                            val fragment = ListFragment()
                            val page=view.findViewById<FrameLayout>(R.id.editCard)
                            parentFragmentManager.popBackStack()
                        }
                        .addOnFailureListener { error ->
                            Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener { error ->
                Toast.makeText(requireContext(), "Error checking ID: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }


    }
}
