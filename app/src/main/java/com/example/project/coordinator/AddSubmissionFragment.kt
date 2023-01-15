package com.example.project.coordinator

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.project.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddSubmissionFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private val db = FirebaseFirestore.getInstance()

    private lateinit var userId: String

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    var getDeadline = ""
    var tv_textTime :TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_submission, container, false)

        tv_textTime = view.findViewById(R.id.tv_textTime)

        userId= FirebaseAuth.getInstance().currentUser!!.uid

        val btn_timePicker = view.findViewById<Button>(R.id.btn_timePicker)

        val te_label = view.findViewById<TextInputEditText>(R.id.te_label)
        val te_batch = view.findViewById<TextInputEditText>(R.id.te_batch)
        val btn_addSubmission = view.findViewById<Button>(R.id.btn_addSubmission)

        pickDate(btn_timePicker)

        btn_addSubmission.setOnClickListener {
            val te_Label = te_label.text.toString().trim()
            val te_Batch = te_batch.text.toString().trim()

//            val deadline = SimpleDateFormat("dd-MM-yyyy HH:mm").parse(getDeadline)
            val deadline = getDeadline

            // Current Date Time format
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

            // Current Date Time
            val currentDateTime = Calendar.getInstance().time

            // Convert current date time from (Date to String)
            val dateCreated = dateFormat.format(currentDateTime)


            val newDocument = db.collection("submission").document()

            val data = mapOf(
                "label" to te_Label,
                "batch" to te_Batch,
                "deadline" to deadline,
                "date_created" to dateCreated,
                "submission_id" to newDocument.id,
                "status" to ""
            )

            newDocument.set(data)
                .addOnSuccessListener { documentReference ->
                    // The data was successfully saved
                    Toast.makeText(
                        context, "Submission has been add successfully",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { e ->
                    // The save failed
                    Toast.makeText(
                        context, "Fail to add submission, please try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
        return view
    }

    private fun getDateTimeCalender(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate(btn_timePicker: Button){
        btn_timePicker.setOnClickListener{
            getDateTimeCalender()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayofMonth: Int) {
        savedDay = dayofMonth
        savedMonth = month + 1
        savedYear = year

        getDateTimeCalender()
        TimePickerDialog(context, this, hour, minute, true).show()
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        savedHour = hourOfDay
        savedMinute = minute

        getDeadline = String.format("%02d", savedDay) + "-" + String.format("%02d", savedMonth) + "-" + savedYear +
                " " + String.format("%02d", savedHour) + ":" + String.format("%02d", savedMinute)

        tv_textTime!!.text = "${String.format("%02d", savedDay)}/${String.format("%02d", savedMonth)}/" +
                "$savedYear ${String.format("%02d", savedHour)}:" + "${String.format("%02d", savedMinute)}"
    }
}