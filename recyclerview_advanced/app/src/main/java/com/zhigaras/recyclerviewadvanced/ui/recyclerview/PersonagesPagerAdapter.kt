package com.zhigaras.recyclerviewadvanced.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        val item = getItem(position)
        item?.let {
            holder.binding.testText.text = item.name
            Glide.with(holder.binding.avatar.context)
                .load(item.image)
                .into(holder.binding.avatar)
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