package com.falen.firebase_8_31_pplg2

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.falen.firebase_8_31_pplg2.model.Perasaan
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class InputPerasaanActivity : Activity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var etPerasaan: EditText
    private lateinit var etTanggal: EditText
    private lateinit var etAlasan: EditText
    private lateinit var btnSimpanPerasaan: Button
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_perasaan)
        auth = FirebaseAuth.getInstance()

        etPerasaan = findViewById(R.id.etPerasaan)
        etTanggal = findViewById(R.id.etTanggal)
        etAlasan = findViewById(R.id.etAlasan)
        btnSimpanPerasaan = findViewById(R.id.btnSimpanPerasaan)

        btnSimpanPerasaan.setOnClickListener {
            savePerasaan()
        }
    }

    private fun savePerasaan() {
        val perasaanText = etPerasaan.text.toString().trim()
        val tanggalText = etTanggal.text.toString().trim()
        val alasanText = etAlasan.text.toString().trim()

        if (perasaanText.isEmpty() || tanggalText.isEmpty() || alasanText.isEmpty()) {
            Toast.makeText(this, "Isi semua data dulu ya", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = auth.currentUser?.uid

        if (uid == null) {
            Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        // Buat data Map agar bisa tambah 'uid'
        val data = hashMapOf(
            "perasaan" to perasaanText,
            "tanggal" to tanggalText,
            "alasan" to alasanText,
            "uid" to uid
        )

        firestore.collection("perasaan")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal simpan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }



}