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
import ch.mofobo.foodscanner.utils.loadUrl
import kotlinx.android.synthetic.main.fragment_history_product_view_holder.view.*


class ProductViewHolder(itemView: View, private val itemClickConsumer: Consumer<Product>) : RecyclerView.ViewHolder(itemView) {

    lateinit var product: Product
    var pos=-1

    fun bind(product: Product, position: Int) {
        this.product = product
        this.pos=position
        this.product.images.firstOrNull()?.let { images -> itemView.thumb.let { imageView -> imageView.loadUrl(images.thumb) } }

        itemView.name.text = this.product.display_name_translations.getTranslation(Lang.ENGLISH, this.product.barcode)

        itemView.setOnClickListener { itemClickConsumer.accept(this.product) }
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