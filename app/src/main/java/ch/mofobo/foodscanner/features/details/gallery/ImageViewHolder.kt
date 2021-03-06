package ch.mofobo.foodscanner.features.details.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import ch.mofobo.foodscanner.R
import ch.mofobo.foodscanner.utils.Dali
import ch.mofobo.foodscanner.utils.loadUrl
import kotlinx.android.synthetic.main.fragment_details_image_view_holder.view.*

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(url: String) {
        itemView.image.loadUrl(url)
    }

    companion object {
        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_details_image_view_holder

        fun create(
            inflater: LayoutInflater,
            parent: ViewGroup
        ): ImageViewHolder {
            val view = inflater.inflate(LAYOUT_ID, parent, false)
            return ImageViewHolder(view)
        }
    }
}