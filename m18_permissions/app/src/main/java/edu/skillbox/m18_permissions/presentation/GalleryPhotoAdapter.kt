package edu.skillbox.m18_permissions.presentation


import android.icu.text.DateTimePatternGenerator.DisplayWidth
import android.icu.text.ListFormatter.Width
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.bumptech.glide.Glide
import edu.skillbox.m18_permissions.data.database.PhotoModel
import edu.skillbox.m18_permissions.databinding.PhotoElementBinding

class GalleryPhotoAdapter(
    private val onClickListener: OnClickListener,
    private val elementWidth: Int,
) : Adapter<GalleryPhotoAdapter.GalleryPhotoViewHolder>() {
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
        
        val params =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.height = elementWidth
        params.width = elementWidth
        holder.itemView.layoutParams = params
        
        if (item != null) {
            holder.binding.apply {
                Glide.with(smallThumbnail.context)
                    .load(Uri.parse(item.photoUri))
                    .centerCrop()
                    .into(smallThumbnail)
                
                smallThumbnail.transitionName = item.photoUri
                root.setOnClickListener {
                    onClickListener.onClick(item, smallThumbnail)
                }
            }
        }
    }
    
    override fun getItemCount(): Int = data.size
    
    class OnClickListener(
        val clickListener: (PhotoModel, ImageView) -> Unit
    ) {
        fun onClick(
            data: PhotoModel,
            photoImageView: ImageView,
        ) = clickListener(data, photoImageView)
    }
    
    class GalleryPhotoViewHolder(val binding: PhotoElementBinding) :
        ViewHolder(binding.root)
}



