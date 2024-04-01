package com.example.uts_pam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserDetailFragment : Fragment() {

    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mendapatkan argument (id pengguna) dari activity sebelumnya
        arguments?.let {
            userId = it.getInt("userId")
        }

        // Memuat data pengguna dari API
        loadUserData()
    }

    private fun loadUserData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getUser(userId)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        // Tampilkan data pengguna
                        displayUserData(it)
                    }
                } else {
                    // Tangani kasus jika respons tidak berhasil
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Tangani kasus jika terjadi kesalahan jaringan
            }
        })
    }

    private fun displayUserData(user: User) {
        // Mendapatkan referensi tampilan dari layout fragment
        val nameTextView = view?.findViewById<TextView>(R.id.nameTextView)
        val emailTextView = view?.findViewById<TextView>(R.id.emailTextView)
        val avatarImageView = view?.findViewById<ImageView>(R.id.avatarImageView)

        // Memastikan semua referensi tampilan tidak null
        if (nameTextView != null && emailTextView != null && avatarImageView != null) {
            // Tampilkan data pengguna pada UI
            nameTextView.text = "${user.first_name} ${user.last_name}"
            emailTextView.text = user.email
            Glide.with(requireContext()).load(user.avatar).into(avatarImageView)
        }
    }


    companion object {
        fun newInstance(userId: Int): UserDetailFragment {
            val fragment = UserDetailFragment()
            val args = Bundle()
            args.putInt("userId", userId)
            fragment.arguments = args
            return fragment
        }
    }
}