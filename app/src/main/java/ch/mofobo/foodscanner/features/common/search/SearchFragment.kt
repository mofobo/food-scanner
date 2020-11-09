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
import ch.mofobo.foodscanner.R
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : DialogFragment() {

    private lateinit var navController: NavController

    private val viewModel: SearchViewModel by viewModel()

    val args: SearchFragmentArgs by navArgs()

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
        productName.text = args.barcode
    }

    private fun oberveViewModel() {
        viewModel.product.observe(viewLifecycleOwner, Observer {
            it?.data?.let {
                productName.text = it.name_translations.french
            }
        })
    }


    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    companion object {

        private const val LAYOUT_ID = R.layout.fragment_search
    }

}