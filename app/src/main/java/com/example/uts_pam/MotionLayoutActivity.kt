package com.example.uts_pam

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton


class MotionLayoutActivity : AppCompatActivity() {

    private lateinit var onboardingAdapter: OnBoardingAdapter
    private lateinit var layoutOnboardingIndicator: LinearLayout
    private lateinit var buttonBoardingAction: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_layout)

        layoutOnboardingIndicator = findViewById(R.id.onBoarding_indikator)
        buttonBoardingAction = findViewById(R.id.tombol_OnBoard1)

        setupOnboardingItems()

        val onboardingViewPager = findViewById<ViewPager2>(R.id.onBoarding_View)
        onboardingViewPager.adapter = onboardingAdapter

        setupOnboardingIndicator()
        onboardingViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnboardingIndicator(position)
            }
        })

        buttonBoardingAction.setOnClickListener {
            if (onboardingViewPager.currentItem + 1 < onboardingAdapter.itemCount) {
                onboardingViewPager.currentItem = onboardingViewPager.currentItem + 1
            } else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setupOnboardingItems() {
        val onboardingItems = ArrayList<OnBoardingItem>()

        val foodItems = OnBoardingItem().apply {
            title = "Selamat Datang di Teman Informatika"
            description = "Aplikasi ini dibangun untuk mengumpulkan orang - orang dengan minat di bidang Informatika."
            imageOnBoarding = R.drawable.splash_screen
        }
        val foodDelivery = OnBoardingItem().apply {
            title = "Kenali Lebih Banyak Orang di Bidang yang Sama"
            description = "Disini kamu dapat mengenal orang - orang yang sedang belajar ilmu Informatika dari berbagai tempat."
            imageOnBoarding = R.drawable.logo_motion_layout_2
        }
        val foodPayment = OnBoardingItem().apply {
            title = "Lihat dan Ajak untuk Kolaborasi"
            description = "Cari orang - orang yang ingin kamu ajak dalam proyek kamu. \nTunggu apalagi ayo segera daftar!!!"
            imageOnBoarding = R.drawable.logo_motion_layout_3
        }

        onboardingItems.add(foodItems)
        onboardingItems.add(foodDelivery)
        onboardingItems.add(foodPayment)

        onboardingAdapter = OnBoardingAdapter(onboardingItems)
    }


    private fun setupOnboardingIndicator() {
        val indicators = arrayOfNulls<ImageView>(onboardingAdapter.itemCount)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.onboarding_indikator_aktif
                )
            )
            indicators[i]?.layoutParams = layoutParams
            layoutOnboardingIndicator.addView(indicators[i])
        }
    }

    private fun setCurrentOnboardingIndicator(index: Int) {
        val childCount = layoutOnboardingIndicator.childCount
        for (i in 0 until childCount) {
            val imageView = layoutOnboardingIndicator.getChildAt(i) as ImageView
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    if (i == index) R.drawable.onboarding_indikator_aktif else R.drawable.onboarding_indikator_nonaktif
                )
            )
        }

        buttonBoardingAction.text = if (index == onboardingAdapter.itemCount - 1) "Start" else "Next"
    }
}
