package com.example.smd_final

import android.widget.EditText

data class Record(
    var studentId: String = "",
    var name: String = "",
    var department: String = "",
    var yearOfStudy: String = "",
    var dateOfRegistration: String = ""  // Store as ISO format string (e.g., "2025-05-21")
)
