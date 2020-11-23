package ch.mofobo.foodscanner.features.history.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.util.Consumer
import androidx.recyclerview.widget.RecyclerView
import ch.mofobo.foodscanner.R
import ch.mofobo.foodscanner.domain.model.Lang
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.utils.Dali
import kotlinx.android.synthetic.main.fragment_history_product_view_holder.view.*


class ProductViewHolder(itemView: View, private val itemClickConsumer: Consumer<Product>) : RecyclerView.ViewHolder(itemView) {

    fun bind(product: Product) {

        product.images.firstOrNull()?.let { images -> itemView.thumb.let { imageView -> Dali.get().load(images.thumb).into(imageView) } }

        itemView.name.text = product.display_name_translations.getTranslation(Lang.ENGLISCH, product.barcode)

        itemView.setOnClickListener { itemClickConsumer.accept(product) }
    }

    companion object {
        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_history_product_view_holder

        fun create(
            inflater: LayoutInflater,
            parent: ViewGroup,
            itemClickConsumer: Consumer<Product>
        ): ProductViewHolder {
            val view = inflater.inflate(LAYOUT_ID, parent, false)
            return ProductViewHolder(view, itemClickConsumer)
        }
    }
}