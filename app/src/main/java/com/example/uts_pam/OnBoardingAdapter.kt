package com.example.uts_pam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OnBoardingAdapter(private val onBoardingItem: List<OnBoardingItem>) :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_motion_layout_onboarding_item, parent, false)
        return OnBoardingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.setOnBoardingData(onBoardingItem[position])
    }

    override fun getItemCount(): Int {
        return onBoardingItem.size
    }

    class OnBoardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.Title)
        private val description: TextView = itemView.findViewById(R.id.Description)
        private val imageOnBoarding: ImageView = itemView.findViewById(R.id.Image_OnBoarding)

        fun setOnBoardingData(onBoardingItem: OnBoardingItem) {
            title.text = onBoardingItem.title
            description.text = onBoardingItem.description
            imageOnBoarding.setImageResource(onBoardingItem.imageOnBoarding)
        }
    }
}
