package ch.mofobo.foodscanner.features.common.search.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import ch.mofobo.foodscanner.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_search_image_view_holder.view.*

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(url: String) {
        if (!url.isNullOrBlank()) Picasso.get().load(url).into(itemView.image)
    }

    companion object {
        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_search_image_view_holder

        fun create(
            inflater: LayoutInflater,
            parent: ViewGroup
        ): ImageViewHolder {
            val view = inflater.inflate(LAYOUT_ID, parent, false)
            return ImageViewHolder(view)
        }
    }
}