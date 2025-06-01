package com.falen.firebase_8_31_pplg2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class EditPerasaanActivity : AppCompatActivity() {
    private lateinit var etPerasaan: EditText
    private lateinit var etTanggal: EditText
    private lateinit var etAlasan: EditText
    private lateinit var etGambar: EditText
    private lateinit var btnUbah: Button
    private lateinit var btnHapus: Button
    private lateinit var firestore: FirebaseFirestore
    private lateinit var perasaanId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_perasaan)
        etPerasaan = findViewById(R.id.etPerasaan)
        etTanggal = findViewById(R.id.etTanggal)
        etAlasan = findViewById(R.id.etAlasan)
        btnUbah = findViewById(R.id.btnUbah)
        btnHapus = findViewById(R.id.btnHapus)
        firestore = FirebaseFirestore.getInstance()
        perasaanId = intent.getStringExtra("perasaanId") ?: ""
        ambilData()
        btnUbah.setOnClickListener {
            val update = mapOf(
                "perasaan" to etPerasaan.text.toString(),
                "tanggal" to etTanggal.text.toString(),
                "alasan" to etAlasan.text.toString()
            )
            firestore.collection("perasaan").document(perasaanId).update(update).addOnSuccessListener {
                Toast.makeText(this, "Emoji diubah", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        btnHapus.setOnClickListener {
            firestore.collection("perasaan").document(perasaanId).delete().addOnSuccessListener {
                Toast.makeText(this, "Emoji dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    private fun ambilData() {
        firestore.collection("perasaan").document(perasaanId).get().addOnSuccessListener { doc ->
            etPerasaan.setText(doc.getString("perasaan"))
            etTanggal.setText(doc.getString("tanggal"))
            etAlasan.setText(doc.getString("alasan"))
        }
    }
}