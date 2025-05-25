package com.example.smd_final

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListFragment : Fragment() {

    private lateinit var list: RecyclerView
    private lateinit var edit: Button
    private lateinit var depHeading: TextView
    private var adapter: ListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = view.findViewById(R.id.enRolledList)
        edit = view.findViewById(R.id.btnEditProfile)
        depHeading=view.findViewById<TextView>(R.id.head)

        edit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainList, EditForm())
                .addToBackStack(null)
                .commit()
        }

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData() // Refresh when returning from EditForm
    }

    private fun loadData() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val recordsRef = FirebaseDatabase.getInstance().getReference("Records")

        // First, listen for changes in the current user's department
        recordsRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(userSnapshot: DataSnapshot) {
                if (userSnapshot.exists()) {
                    val department = userSnapshot.child("department").getValue(String::class.java)
                    Log.d("deptt", department.toString())

                    if (department != null) {
                        // Then listen for real-time changes in the list of users from that department
                        FirebaseDatabase.getInstance().getReference("Records")
                            .orderByChild("department")
                            .equalTo(department)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(deptSnapshot: DataSnapshot) {
                                    val records = mutableListOf<Record>()
                                    for (data in deptSnapshot.children) {
                                        val record = data.getValue(Record::class.java)
                                        if (record != null) {
                                            records.add(record)
                                        }
                                    }

                                    depHeading.text = department
                                    adapter = ListAdapter(records)
                                    list.layoutManager = LinearLayoutManager(requireContext())
                                    list.adapter = adapter
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
