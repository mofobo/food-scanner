package ch.mofobo.foodscanner.features.history.list

import androidx.recyclerview.widget.DiffUtil
import ch.mofobo.foodscanner.domain.model.Product

class ProductDiffCallback(private val old: List<Product>, private val new: List<Product>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val item1 = old[oldItemPosition]
        val item2 = new[newItemPosition]
        return item1.isSameAs(item2)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldItemPosition, newItemPosition)
    }
}