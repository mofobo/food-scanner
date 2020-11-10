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
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.features.common.search.gallery.ImageGalleryAdapter
import ch.mofobo.foodscanner.features.common.search.gallery.ImageGalleryLayoutManager
import ch.mofobo.foodscanner.utils.recyclerview.RecyclerViewDividerMarginItemDecoration
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

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
        nutrients_table_webview.loadUrl("file:///android_asset/nutrients_template.html")
    }

    private fun prepareAdapter() {
        image_gallery_rv.layoutManager = ImageGalleryLayoutManager(context)

        image_gallery_rv.addItemDecoration(
            RecyclerViewDividerMarginItemDecoration(
                resources.getDimension(R.dimen.image_gallery_item_margin).toInt()
            )
        )

        imageGalleryAdapter = ImageGalleryAdapter()
        image_gallery_rv.adapter = imageGalleryAdapter
        imageGalleryAdapter.setData(listOf("", "", ""))

    }

    private fun oberveViewModel() {

        viewModel.product.observe(viewLifecycleOwner, Observer {
            it?.data?.let {
                displayProduct(it)
            }
        })

        viewModel.actions.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SearchViewModel.Action.NutrientsTable -> nutrients_table_webview.loadData(it.html, "text/html", "UTF-8")
            }
        })
    }

    private fun displayProduct(product: Product) {
        product.let {
            product.let {
                imageGalleryAdapter.setData(it.getImages("large"))
                viewModel.retrieveNutrientsTable(it.nutrients, readeFileFromAssets("nutrients_template.html"))

            }
        }
        name_tv.text = product.name_translations.french
    }


    /*suspend private fun handleNutrientsTable(nutrients: Nutrients) {
        var nutrientsHtmlTemplateStr = readeFileFromAssets("nutrients_template.html")
        Log.d("FUCK", nutrientsHtmlTemplateStr)

        val nutrientInfosHTML = mutableListOf<String>()
        for (property in Nutrients::class.memberProperties) {
            val nutrient = property.call(nutrients) as NutrientInfo?
            nutrient?.let {
                val name = nutrient.nameTranslations.getTranslation(Lang.FRENCH, property.name)
                val qty = nutrient.getQty()
                val nutriRec = "69%"
                Log.d("FUCK", name)
                Log.d("FUCK", qty)
                val nutrientInfoHtml = String.format(NUTRIENT_MAIN_HTML_TEMPLATE, name, qty, nutriRec)
                nutrientInfosHTML.add(nutrientInfoHtml)
            }
        }
        nutrientsHtmlTemplateStr = nutrientsHtmlTemplateStr.replace("[NUTRIENTS_ITEMS]", nutrientInfosHTML.joinToString(separator = ""))
        nutrients_table_webview.loadData(nutrientsHtmlTemplateStr, "text/html", "UTF-8")
    }*/

    private fun readeFileFromAssets(fileName: String): String {
        var reader: BufferedReader? = null
        var result = ""
        try {
            reader = BufferedReader(InputStreamReader(requireActivity().assets.open(fileName), "UTF-8"))

            var line: String?
            do {
                line = reader.readLine()
                result += line ?: ""
            } while (line != null)

        } catch (e: IOException) { /*log the exception*/
        } finally {
            reader?.let {
                try {
                    it.close()
                } catch (e: IOException) { /* log the exception */
                }
            }
        }
        return result
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    companion object {

        private const val LAYOUT_ID = R.layout.fragment_search

    }

}