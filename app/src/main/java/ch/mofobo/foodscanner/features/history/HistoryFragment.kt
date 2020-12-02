package ch.mofobo.foodscanner.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.util.Consumer
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.mofobo.foodscanner.R
import ch.mofobo.foodscanner.domain.model.Lang
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.features.history.list.ProductListAdapter
import ch.mofobo.foodscanner.features.history.list.ProductViewHolder
import ch.mofobo.foodscanner.features.history.list.SwipeToDeleteHelperCallBack
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private lateinit var navController: NavController

    private val viewModel: HistoryViewModel by viewModel()

    private lateinit var productListAdapter: ProductListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT_ID, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()
        prepareView()
        observeViewModel()

        viewModel.getProducts()
    }

    private fun navigateTo(destination: Int) {
        navController.navigate(destination)
    }

    private fun prepareView() {
        prepareAdapter()
    }

    private fun prepareAdapter() {
        products_rv.layoutManager = LinearLayoutManager(requireContext())
        val itemClickListener = Consumer<Product> { product -> navController.navigate(HistoryFragmentDirections.actionNavigationToDetails(product.id, null)) }
        productListAdapter = ProductListAdapter(itemClickListener)
        products_rv.adapter = productListAdapter

        val swipeToDeleteHelperCallBack = object : SwipeToDeleteHelperCallBack(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val product = (viewHolder as ProductViewHolder).product
                val position = viewHolder.pos
                viewModel.removeProduct(product)

                Snackbar.make(viewHolder.itemView, "${product.display_name_translations.getTranslation(Lang.GERMAN, "")} removed", Snackbar.LENGTH_LONG).setAction("UNDO") {
                    product.let { prod ->
                        viewModel.products.value?.let {
                            viewModel.addProduct(prod, it.size - position)
                        }
                    }
                }.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteHelperCallBack)
        itemTouchHelper.attachToRecyclerView(products_rv)
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner, Observer {
            productListAdapter.setData(it)
        })
    }

    companion object {
        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_history
    }
}