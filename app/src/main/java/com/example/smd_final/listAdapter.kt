package com.example.smd_final

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private val records: List<Record>) :
    RecyclerView.Adapter<ListAdapter.RecordViewHolder>() {

    inner class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentId: TextView = itemView.findViewById(R.id.textStudentId)
        val name: TextView = itemView.findViewById(R.id.textStudentName)
        val department: TextView = itemView.findViewById(R.id.textDepartment)
        val year: TextView = itemView.findViewById(R.id.textYear)
        val registrationDate: TextView = itemView.findViewById(R.id.textRegistrationDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_card, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListAdapter.RecordViewHolder, position: Int) {
        val record = records[position]
        holder.studentId.text = "ID: ${record.studentId}"
        holder.name.text = "Name: ${record.name}"
        holder.department.text = "Department: ${record.department}"
        holder.year.text = "Year: ${record.yearOfStudy}"
        holder.registrationDate.text = "Registered: ${record.dateOfRegistration}"
    }

    override fun getItemCount(): Int = records.size
}
