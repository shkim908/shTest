package com.example.calendarapp.ui.popup

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import com.example.calendarapp.databinding.FragmentAddScheduleBinding
import com.example.calendarapp.viewmodel.MainActivityViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.collections.hashMapOf as hashMapOf

class AddScheduleFragment : Fragment() {
    private lateinit var binding: FragmentAddScheduleBinding
    private val viewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddScheduleBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initBtn()
    }

    private fun initView(){
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            binding.dateTv.text = date
        }
    }


    private fun initBtn(){

        binding.timeTv.setOnClickListener {
            val cal = Calendar.getInstance()
            var hour = cal.get(Calendar.HOUR)
            var minute = cal.get(Calendar.MINUTE)
            val timeSetListener = OnTimeSetListener {
                override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                    hour = p1
                    minute = p2
                    


                }
            }
            TimePickerDialog(requireContext(), timeSetListener,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true).show()
        }

        binding.tvComplete.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val title = binding.titleEt.text.toString()
            val memo = binding.timeEt.text.toString()
            val date = binding.dateTv.text.toString()
            val event = hashMapOf(
                "title" to title,
                "memo" to memo
            )

            db.collection("Schedule").document(date)
                .collection("events").add(event)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(requireContext(), "success!!!", Toast.LENGTH_SHORT).show()
                }
        }
    }

}