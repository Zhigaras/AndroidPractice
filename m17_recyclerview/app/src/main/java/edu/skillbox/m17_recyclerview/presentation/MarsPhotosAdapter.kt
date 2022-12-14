package edu.skillbox.m17_recyclerview.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import edu.skillbox.m17_recyclerview.databinding.ListElementBinding
import edu.skillbox.m17_recyclerview.entity.MarsRoversPhoto


class MarsPhotosAdapter : Adapter<MarsPhotosViewHolder>() {
    
    private var data: List<MarsRoversPhoto> = emptyList()
    fun setData(data: List<MarsRoversPhoto>) {
        this.data = data
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsPhotosViewHolder {
        val binding = ListElementBinding.inflate(LayoutInflater.from(parent.context))
        return MarsPhotosViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: MarsPhotosViewHolder, position: Int) {
        val item = data.getOrNull(position)
        item?.let {
            Glide
                .with(holder.binding.imageView.context)
                .load(it.imgSrc)
                .into(holder.binding.imageView)
        }
    }
    
    override fun getItemCount(): Int = data.size
}

class MarsPhotosViewHolder(val binding: ListElementBinding) : ViewHolder(binding.root)