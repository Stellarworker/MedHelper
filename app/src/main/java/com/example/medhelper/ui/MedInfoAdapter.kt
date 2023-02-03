package com.example.medhelper.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medhelper.R
import com.example.medhelper.domain.MedInformation
import com.example.medhelper.utils.convertLongToTime

class MedInfoAdapter : RecyclerView.Adapter<MedInfoAdapter.MedInfoViewHolder>() {
    private var medInfoList: MutableList<MedInformation> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(medInfoList: List<MedInformation>) {
        this.medInfoList = medInfoList.toMutableList()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(medInfoList: List<MedInformation>) {
        this.medInfoList.addAll(medInfoList)
        notifyDataSetChanged()
    }

    inner class MedInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val date: AppCompatTextView =
            itemView.findViewById(R.id.med_info_date)
        private val time: AppCompatTextView =
            itemView.findViewById(R.id.med_info_item_time)
        private val systolicPressure: AppCompatTextView =
            itemView.findViewById(R.id.med_info_item_systolic_pressure)
        private val diastolicPressure: AppCompatTextView =
            itemView.findViewById(R.id.med_info_item_diastolic_pressure)
        private val heartRate: AppCompatTextView =
            itemView.findViewById(R.id.med_info_item_heart_rate)

        fun bind(medInfo: MedInformation) {
            date.text = convertLongToTime(
                medInfo.dateTime,
                itemView.context.getString(R.string.med_info_date_pattern)
            )
            time.text = convertLongToTime(
                medInfo.dateTime,
                itemView.context.getString(R.string.med_info_time_pattern)
            )
            systolicPressure.text = medInfo.systolicPressure.toString()
            diastolicPressure.text = medInfo.diastolicPressure.toString()
            heartRate.text = medInfo.heartRate.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedInfoViewHolder {
        return MedInfoViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.med_info_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: MedInfoViewHolder, position: Int) {
        holder.bind(medInfoList[position])
    }

    override fun getItemCount() = medInfoList.size

}