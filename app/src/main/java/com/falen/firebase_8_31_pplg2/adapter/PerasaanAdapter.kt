package com.falen.firebase_8_31_pplg2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.falen.firebase_8_31_pplg2.R
import com.falen.firebase_8_31_pplg2.model.Perasaan

class PerasaanAdapter(
    private val perasaanList: List<Perasaan>,
    private val onItemClick: (Perasaan) -> Unit) :
    RecyclerView.Adapter<PerasaanAdapter.PerasaanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerasaanViewHolder {
        // Ganti R.layout.item_perasaan sesuai nama file XML item layout kamu
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_perasaan, parent, false)
        return PerasaanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PerasaanViewHolder, position: Int) {
        val perasaan = perasaanList[position]
        holder.tvPerasaan.text = perasaan.perasaan
        holder.tvTanggal.text = perasaan.tanggal
        holder.tvAlasan.text = perasaan.alasan
        holder.itemView.setOnClickListener {onItemClick(perasaan)}
    }

    override fun getItemCount(): Int {
        return perasaanList.size
    }

    class PerasaanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPerasaan: TextView = itemView.findViewById(R.id.tvPerasaan)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvAlasan: TextView = itemView.findViewById(R.id.tvAlasan)
    }
}
