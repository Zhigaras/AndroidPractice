package edu.skillbox.m18_permissions.presentation


import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import edu.skillbox.m18_permissions.data.database.PhotoModel
import edu.skillbox.m18_permissions.databinding.PhotoElementBinding

class GalleryPhotoAdapter : Adapter<GalleryPhotoViewHolder>() {
    
    private var data: List<PhotoModel> = emptyList()
    fun setData(data: List<PhotoModel>) {
        this.data = data
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryPhotoViewHolder {
        return GalleryPhotoViewHolder(
            PhotoElementBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    
    override fun onBindViewHolder(holder: GalleryPhotoViewHolder, position: Int) {
        val item = data.getOrNull(position)
        item?.let { item ->
            Glide.with(holder.binding.photoImageView.context)
                .load(Uri.parse(item.photoUri))
                .into(holder.binding.photoImageView)
        }
    }
    
    override fun getItemCount(): Int = data.size
    
}

class GalleryPhotoViewHolder(val binding: PhotoElementBinding) :
   ViewHolder(binding.root)