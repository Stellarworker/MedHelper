package com.example.medhelper.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.example.medhelper.R
import com.example.medhelper.databinding.ActivityMainBinding
import com.example.medhelper.domain.AppMessage
import com.example.medhelper.domain.MedInformation
import com.example.medhelper.utils.ZERO
import com.example.medhelper.utils.hide
import com.example.medhelper.utils.makeSnackbar
import com.example.medhelper.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()
    private val adapter = MedInfoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMainViewModel()
        initButton()
        initRecyclerView()
        mainViewModel.requestAllData()
    }

    private fun initMainViewModel() {
        mainViewModel.messagesLiveData.observe(this) { message ->
            processMessages(message)
        }
    }

    private fun initButton() {
        binding.activityMainAddButton.setOnClickListener { mainViewModel.onAddButtonClick() }
    }

    private fun initRecyclerView() {
        binding.activityMainList.adapter = adapter
    }

    private fun showAddDialog() {
        val view = layoutInflater.inflate(R.layout.add_dialog, null)
        val systolicPressureEdit =
            view.findViewById<AppCompatEditText>(R.id.add_dialog_systolic_pressure)
        val diastolicPressureEdit =
            view.findViewById<AppCompatEditText>(R.id.add_dialog_diastolic_pressure)
        val heartRateEdit = view.findViewById<AppCompatEditText>(R.id.add_dialog_heart_rate)
        AlertDialog.Builder(this)
            .setTitle(this.getString(R.string.activity_main_add_dialog_title))
            .setNegativeButton(getString(R.string.activity_main_add_dialog_negative_button))
            { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.activity_main_add_dialog_positive_button))
            { _, _ ->
                val medInformation = MedInformation(
                    dateTime = System.currentTimeMillis(),
                    systolicPressure = systolicPressureEdit.text.toString().toIntOrNull()
                        ?: Int.ZERO,
                    diastolicPressure = diastolicPressureEdit.text.toString().toIntOrNull()
                        ?: Int.ZERO,
                    heartRate = heartRateEdit.text.toString().toIntOrNull() ?: Int.ZERO
                )
                mainViewModel.requestSaveData(medInformation)
            }
            .setView(view)
            .create()
            .show()
    }

    private fun showMedInformation(medInformation: List<MedInformation>) {
        if (adapter.itemCount > Int.ZERO) {
            adapter.addData(medInformation)
        } else {
            adapter.setData(medInformation)
        }
        with(binding) {
            activityMainLoadingProgress.hide()
            activityMainList.show()
            activityMainAddButton.show()
        }
    }

    private fun processMessages(appMessage: AppMessage) {
        with(appMessage) {
            when (this) {
                is AppMessage.MedInformationMessage -> showMedInformation(medInformation)
                is AppMessage.InfoSnackBar -> makeSnackbar(view = binding.root, text = text)
                is AppMessage.InfoToast -> Toast.makeText(applicationContext, text, length).show()
                is AppMessage.AddDialog -> showAddDialog()
            }
        }
    }

}