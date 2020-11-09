package ch.mofobo.foodscanner.features.common.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import ch.mofobo.foodscanner.R
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.features.common.search.gallery.ImageGalleryAdapter
import ch.mofobo.foodscanner.features.common.search.gallery.ImageGalleryLayoutManager
import ch.mofobo.foodscanner.utils.recyclerview.RecyclerViewDividerMarginItemDecoration
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : DialogFragment() {

    private lateinit var navController: NavController
    private val viewModel: SearchViewModel by viewModel()

    val args: SearchFragmentArgs by navArgs()

    private lateinit var imageGalleryAdapter: ImageGalleryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT_ID, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        prepareView()
        oberveViewModel()

        viewModel.searchProduct(args.barcode)
    }

    private fun navigateTo(destination: Int) {
        navController.navigate(destination)
    }

    private fun prepareView() {
        prepareAdapter()
        name.text = args.barcode
    }

    private fun prepareAdapter() {
        imageGallery.layoutManager = ImageGalleryLayoutManager(context)

        imageGallery.addItemDecoration(
            RecyclerViewDividerMarginItemDecoration(
                resources.getDimension(R.dimen.image_gallery_item_margin).toInt()
            )
        )

        imageGalleryAdapter = ImageGalleryAdapter()
        imageGallery.adapter = imageGalleryAdapter
        imageGalleryAdapter.setData(listOf("","",""))

    }

    private fun oberveViewModel() {

        viewModel.product.observe(viewLifecycleOwner, Observer {
            it?.data?.let {
                displayProduct(it)
            }
        })
    }

    private fun displayProduct(product: Product) {
        product.let {
            product.let {
                imageGalleryAdapter.setData(it.getImages("large"))
            }
        }
        name.text = product.name_translations.french
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    companion object {

        private const val LAYOUT_ID = R.layout.fragment_search
    }

}