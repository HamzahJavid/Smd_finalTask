package com.example.smd_final
import android.util.Log
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FillRecordForm : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.create_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = view.findViewById<EditText>(R.id.editTextStudentId)
        val department = view.findViewById<EditText>(R.id.editTextDepartment)
        val registrationDate = view.findViewById<EditText>(R.id.editTextDate)
        val name = view.findViewById<EditText>(R.id.editTextName)
        val yearOfStudy = view.findViewById<EditText>(R.id.editTextYear)
        val saveButton = view.findViewById<Button>(R.id.btnCreateProfile)
        val page=view.findViewById<FrameLayout>(R.id.fragment_container)

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
            if (uid == null) {
                Toast.makeText(requireContext(), "UID not found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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

                    recordsRef.child(uid).setValue(record)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Record saved", Toast.LENGTH_SHORT).show()
                            val fragment = ListFragment()
                            page.removeAllViews()
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .commit()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                            Log.d("form",it.message.toString())
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Error checking ID: ${it.message}", Toast.LENGTH_SHORT).show()
                Log.d("F off",it.message.toString())
            }
        }
    }
}
