package ch.mofobo.foodscanner.features.history.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.Consumer
import androidx.recyclerview.widget.RecyclerView
import ch.mofobo.foodscanner.domain.model.Product


class ProductListAdapter(private val itemClickConsumer: Consumer<Product>) : RecyclerView.Adapter<ProductViewHolder>() {

    private val items = arrayListOf<Product>()

    fun setData(newItems: List<Product>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return RecyclerView.NO_ID
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductViewHolder.create(inflater, parent, itemClickConsumer)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = items[position]
        holder.bind(product)
    }
}