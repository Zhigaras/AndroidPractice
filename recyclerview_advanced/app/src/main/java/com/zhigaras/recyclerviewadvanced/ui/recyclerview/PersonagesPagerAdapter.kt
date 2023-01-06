package com.zhigaras.recyclerviewadvanced.ui.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zhigaras.recyclerviewadvanced.R
import com.zhigaras.recyclerviewadvanced.databinding.PersonageElementBinding
import com.zhigaras.recyclerviewadvanced.model.Personage

class PersonagesPagerAdapter : PagingDataAdapter<Personage, PersonageViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonageViewHolder {
        return PersonageViewHolder(
            PersonageElementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    
    override fun onBindViewHolder(holder: PersonageViewHolder, position: Int) {
        val item = getItem(position) ?: return
        val status = item.status
        val statusImage = when (status.lowercase()) {
            "alive" -> R.drawable.status_alive
            "dead" -> R.drawable.status_dead
            else -> R.drawable.status_unknown
        }
        holder.binding.apply {
            name.text = item.name
            statusText.text = buildString {
                append(status)
                append(" | ")
                append(item.species)
            }
            statusCircle.setImageResource(statusImage)
            originDescription.text = item.origin.name
            locationDescription.text = item.location.name
            Glide.with(avatar.context)
                .load(item.image)
                .into(avatar)
        }
    }
    
}

class DiffUtilCallback : DiffUtil.ItemCallback<Personage>() {
    override fun areItemsTheSame(oldItem: Personage, newItem: Personage): Boolean =
        oldItem.id == newItem.id
    
    
    override fun areContentsTheSame(oldItem: Personage, newItem: Personage): Boolean =
        oldItem == newItem
    
}

class PersonageViewHolder (val binding: PersonageElementBinding) : RecyclerView.ViewHolder(binding.root)