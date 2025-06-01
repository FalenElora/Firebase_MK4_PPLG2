package com.falen.firebase_8_31_pplg2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.falen.firebase_8_31_pplg2.adapter.PerasaanAdapter
import com.falen.firebase_8_31_pplg2.databinding.ActivityMainBinding
import com.falen.firebase_8_31_pplg2.model.Perasaan
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var perasaanAdapter: PerasaanAdapter
    private val perasaanList = mutableListOf<Perasaan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Auth dan Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Setup tombol
        binding.btnSignOut.setOnClickListener(this)
        binding.btnEmailVerify.setOnClickListener(this)
        binding.btnAddPerasaan.setOnClickListener {
            val intent = Intent(this, InputPerasaanActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_PERASAAN)
        }

        // Setup RecyclerView
        perasaanAdapter = PerasaanAdapter(perasaanList) { perasaan ->
            val intent = Intent(this, EditPerasaanActivity::class.java)
            intent.putExtra("perasaanId", perasaan.id)
            startActivity(intent)
        }
        binding.rvPerasaan.layoutManager = LinearLayoutManager(this)
        binding.rvPerasaan.adapter = perasaanAdapter

        loadPerasaanData()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        } else {
            updateUI(currentUser)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnSignOut -> signOut()
            R.id.btnEmailVerify -> sendEmailVerification()
        }
    }

    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Verification email sent to ${user.email}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        user?.let {
            val name = it.displayName ?: "No Name"
            val email = it.email ?: "No Email"
            binding.tvName.text = "Name: $name"
            binding.tvUserId.text = "User ID: $email"
            binding.btnEmailVerify.visibility = if (it.isEmailVerified) View.GONE else View.VISIBLE
        }
    }

    private fun loadPerasaanData() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("perasaan")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { documents ->
                perasaanList.clear()
                for (document in documents) {
                    val perasaan = document.toObject(Perasaan::class.java)
                    perasaan.id = document.id
                    perasaanList.add(perasaan)
                }
                perasaanAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_PERASAAN && resultCode == RESULT_OK) {
            loadPerasaanData()
        }
    }

    companion object {
        const val REQUEST_CODE_ADD_PERASAAN = 1
    }
    override fun onResume() {
        super.onResume()
        loadPerasaanData()
    }
}
