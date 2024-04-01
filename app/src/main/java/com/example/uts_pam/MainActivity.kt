package com.example.uts_pam

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), UserAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var searchView: SearchView
    private lateinit var img1: ImageView
    private lateinit var profil: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        img1 = findViewById(R.id.B_img_1)
        btnBackBerandaListener()

        profil = findViewById(R.id.UserProfil)
        btnToUserProfil()

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.SearchView)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.recycler_view_item_divider)?.let {
            itemDecoration.setDrawable(it)
        }
        recyclerView.addItemDecoration(itemDecoration)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val call = apiService.getUsers()

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>, response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    val userList = response.body()?.data ?: emptyList()
                    adapter.setData(userList)
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(
                call: Call<ApiResponse>, t: Throwable) {
                // Handle network failure
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    adapter.filterList(adapter.originalUserList)
                } else {
                    val filteredList = adapter.originalUserList.filter { user ->
                        user.first_name.contains(newText, ignoreCase = true) ||
                                user.last_name.contains(newText, ignoreCase = true)
                    }
                    adapter.filterList(filteredList)
                }
                return true
            }
        })
    }

    override fun onItemClick(user: User) {
        displayUserDetail(user.id)
    }

    private fun displayUserDetail(userId: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        val fragment = UserDetailFragment.newInstance(userId)
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun btnBackBerandaListener() {
        img1.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun btnToUserProfil() {
        profil.setOnClickListener {
            startActivity(Intent(this, ProfilUserActivity::class.java))
        }
    }
}
