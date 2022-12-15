package edu.skillbox.m17_recyclerview.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import edu.skillbox.m17_recyclerview.databinding.ListElementBinding
import edu.skillbox.m17_recyclerview.entity.MarsRoversPhoto


class MarsPhotosAdapter(
    private val onClick: (MarsRoversPhoto) -> Unit
) : Adapter<MarsPhotosViewHolder>() {
    
    private var data: List<MarsRoversPhoto> = emptyList()
    fun setData(data: List<MarsRoversPhoto>) {
        this.data = data
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsPhotosViewHolder {
        return MarsPhotosViewHolder(
            ListElementBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    
    override fun onBindViewHolder(holder: MarsPhotosViewHolder, position: Int) {
        val item = data.getOrNull(position)
        item?.let { item ->
            with(holder.binding) {
                camera.text = buildString {
                    append("Camera: ")
                    append(item.camera.name)
                }
                date.text = buildString {
                    append("Date: ")
                    append(item.earthDate)
                }
            }
            Glide
                .with(holder.binding.imageView.context)
                .load(item.imgSrc)
                .into(holder.binding.imageView)
        }
        
        holder.binding.root.setOnClickListener {
            holder.binding.imageView.transitionName = "transition_name_image"
            item?.let(onClick)
        }
    }
    
    override fun getItemCount(): Int = data.size
}

class MarsPhotosViewHolder(val binding: ListElementBinding) : ViewHolder(binding.root)