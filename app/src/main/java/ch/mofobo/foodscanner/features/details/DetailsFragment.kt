package ch.mofobo.foodscanner.features.details

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.transition.Scene
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import ch.mofobo.foodscanner.R
import ch.mofobo.foodscanner.common.StringUtils
import ch.mofobo.foodscanner.domain.exception.BaseException
import ch.mofobo.foodscanner.domain.exception.BaseException.*
import ch.mofobo.foodscanner.domain.model.Lang
import ch.mofobo.foodscanner.domain.model.NutrientInfo
import ch.mofobo.foodscanner.domain.model.Product
import ch.mofobo.foodscanner.features.details.gallery.ImageGalleryAdapter
import ch.mofobo.foodscanner.features.details.gallery.ImageGalleryLayoutManager
import ch.mofobo.foodscanner.utils.recyclerview.RecyclerViewDividerMarginItemDecoration
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_details_scene_details.*
import kotlinx.android.synthetic.main.fragment_details_scene_not_found.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class DetailsFragment : DialogFragment() {

    private lateinit var navController: NavController
    private val viewModel: DetailsViewModel by viewModel()

    val args: DetailsFragmentArgs by navArgs()

    private var product: Product? = null

    private lateinit var imageGalleryAdapter: ImageGalleryAdapter

    private lateinit var sceneProductDetails: Scene
    private lateinit var sceneLoading: Scene
    private lateinit var sceneProductNotFound: Scene

    private val currentScene: Scene?
        get() = Scene.getCurrentScene(scene_container)

    private val animateSceneTransition: Boolean
        get() = currentScene != null

    private val lang: Lang
        get() = Lang.valueOf(requireContext().getString(R.string.enum_lang))

    private lateinit var fadeTransition: Transition

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT_ID, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        prepareView()
        oberveViewModel()

        changeScene(sceneLoading)
        val id = if (args.id == -1L) null else args.id
        viewModel.searchProduct(id, args.barcode)
    }

    private fun prepareView() {

        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        toolbar.title = requireContext().getString(R.string.details_toolbar_title)

        fadeTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.fade_out_in_together)
        sceneLoading = Scene.getSceneForLayout(scene_container, LAYOUT_SCENE_LOADING_ID, requireContext())
        sceneProductDetails = Scene.getSceneForLayout(scene_container, LAYOUT_SCENE_DETAILS_ID, requireContext())
        sceneProductNotFound = Scene.getSceneForLayout(scene_container, LAYOUT_SCENE_NOT_FOUND_ID, requireContext())
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
        imageGalleryAdapter.setData(emptyList())

    }

    private fun oberveViewModel() {

        viewModel.product.observe(viewLifecycleOwner, Observer {
            product = it
            if (product != null) changeScene(sceneProductDetails) else changeScene(sceneProductNotFound)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ProductNotFoundException -> {
                    if (viewModel.product.value != null) {
                        Toast.makeText(requireContext(), R.string.exception_product_offline, Toast.LENGTH_LONG).show()
                    } else {
                        changeScene(sceneProductNotFound)
                    }
                }
                is ProductSearchException -> {
                }
                is NetworkException -> {
                    if (viewModel.product.value != null) {
                        Toast.makeText(requireContext(), R.string.exception_product_offline, Toast.LENGTH_LONG).show()
                    } else {
                        changeScene(sceneProductNotFound)
                        Toast.makeText(requireContext(), R.string.exception_no_internet, Toast.LENGTH_LONG).show()
                    }
                }
                is UnknownException -> Toast.makeText(requireContext(), R.string.exception_unknown, Toast.LENGTH_LONG).show()
            }


        })
    }

    private fun changeScene(newScene: Scene) {
        if (newScene == currentScene) return

        when (animateSceneTransition) {
            true -> TransitionManager.go(newScene, fadeTransition)
            false -> newScene.enter()
        }

        when (newScene) {
            sceneLoading -> initLoadingScene()
            sceneProductDetails -> initProductDetailsScene()
            sceneProductNotFound -> initProductNotFoundScene()
        }
    }

    private fun initLoadingScene() {
        if (currentScene != sceneLoading) return
    }

    private fun initProductDetailsScene() {
        if (currentScene != sceneProductDetails) return
        prepareAdapter()
        displayProduct(product!!)
    }

    private fun initProductNotFoundScene() {
        if (currentScene != sceneProductNotFound) return

        barcode.text = (viewModel.error.value as BaseException).barcode
    }


    private fun displayProduct(product: Product) {
        product.let {

            name_tv.text = it.name_translations.getAnyTranslation(Lang.valueOf(requireContext().getString(R.string.enum_lang)), it.barcode)

            imageGalleryAdapter.setData(it.getImages("large"))

            Handler().postDelayed({ displayNutrients(it) }, 10)
        }
    }

    private fun displayNutrients(product: Product) {
        var htmlTemplate = requireContext().getString(R.string.details_nutrients_table_html)
        val nutrients = product.nutrients
        val nutrientInfosHTML = mutableListOf<String>()
        val vitaminsInfosHTML = mutableListOf<String>()

        val quantityStr = StringUtils.trimTrailingZeroAndAddSuffix(product.quantity.toDouble(), " ${product.unit}", "-") ?: "-"
        val portionStr = StringUtils.trimTrailingZeroAndAddSuffix(product.portion_quantity.toDouble(), " ${product.portion_unit}", "-") ?: "-"

        fun formatNutrientLine(nutrient: NutrientInfo?, defaultName: String, isMainNutrient: Boolean = true): Boolean {

            if (nutrient == null) return false

            val name = nutrient.nameTranslations.getTranslation(lang, defaultName)
            val perHundred = StringUtils.trimTrailingZeroAndAddSuffix(nutrient.perHundred, " ${nutrient.unit}", "")
            val perPortion = StringUtils.trimTrailingZeroAndAddSuffix(nutrient.perPortion, " ${nutrient.unit}", "")
            val perDay = StringUtils.trimTrailingZeroAndAddSuffix(nutrient.perDay, "", "")

            val template = if (isMainNutrient) NUTRIENT_MAIN_HTML_TEMPLATE else NUTRIENT_SUB_HTML_TEMPLATE
            val result = String.format(template, name, perHundred, perPortion, perDay)

            return nutrientInfosHTML.add(result)
        }

        fun formatVitamineLine(nutrient1: NutrientInfo?, nutrient2: NutrientInfo?, defaultName: String) {
            var nutrient1Str: String? = null
            var nutrient2Str: String? = null

            if (nutrient1 != null) {
                val name = nutrient1.nameTranslations.getTranslation(lang, defaultName)
                val perDay = StringUtils.trimTrailingZeroAndAddSuffix(nutrient1.perDay, "", "")
                nutrient1Str = "$name $perDay%"
            }

            if (nutrient2 != null) {
                val name = nutrient2.nameTranslations.getTranslation(lang, defaultName)
                val perDay = StringUtils.trimTrailingZeroAndAddSuffix(nutrient2.perDay, "", "")
                nutrient2Str = "$name $perDay%"
            }


            if (nutrient1Str.isNullOrBlank() && nutrient1Str.isNullOrBlank()) return

            val result = String.format(VITAMINE_HTML_TEMPLATE, nutrient1Str, nutrient2Str ?: "")
            vitaminsInfosHTML.add(result)
        }

        var hasMainNutrient: Boolean = false
        formatNutrientLine(nutrients.energy, "")
        formatNutrientLine(nutrients.energy_kcal, "")
        formatNutrientLine(nutrients.cholesterol, "")
        hasMainNutrient = formatNutrientLine(nutrients.carbohydrates, "")
        formatNutrientLine(nutrients.saccharose, "", !hasMainNutrient)
        formatNutrientLine(nutrients.fructose, "", !hasMainNutrient)
        formatNutrientLine(nutrients.glucose, "", !hasMainNutrient)
        formatNutrientLine(nutrients.fiber, "", !hasMainNutrient)
        formatNutrientLine(nutrients.sugars, "", !hasMainNutrient)
        hasMainNutrient = formatNutrientLine(nutrients.fat, "")
        formatNutrientLine(nutrients.saturated_fat, "", !hasMainNutrient)
        formatNutrientLine(nutrients.monounsaturatedFattyAcids, "", !hasMainNutrient)
        formatNutrientLine(nutrients.polyunsaturatedFattyAcids, "", !hasMainNutrient)
        hasMainNutrient = formatNutrientLine(nutrients.salt, "")
        formatNutrientLine(nutrients.sodium, "", !hasMainNutrient)
        formatNutrientLine(nutrients.iodine, "", !hasMainNutrient)
        formatNutrientLine(nutrients.selenium, "", !hasMainNutrient)
        formatNutrientLine(nutrients.lactose, "")
        formatNutrientLine(nutrients.polyols, "")
        formatNutrientLine(nutrients.protein, "")
        
        formatVitamineLine(nutrients.biotin, nutrients.calcium, "")
        formatVitamineLine(nutrients.folicAcid, nutrients.iron, "")
        formatVitamineLine(nutrients.magnesium, nutrients.omega_3_fatty_acids, "")
        formatVitamineLine(nutrients.omega_6_fatty_acids, nutrients.phosphorus, "")
        formatVitamineLine(nutrients.provitaminACarotene, nutrients.vitaminA, "")
        formatVitamineLine(nutrients.vitaminB12Cobalamin, nutrients.vitaminB1Thiamin, "")
        formatVitamineLine(nutrients.vitaminB3Niacin, nutrients.vitaminB5PanthothenicAcid, "")
        formatVitamineLine(nutrients.vitaminB6Pyridoxin, nutrients.vitaminB2Riboflavin, "")
        formatVitamineLine(nutrients.vitaminCAscorbicAcid, nutrients.vitaminDCholacalciferol, "")
        formatVitamineLine(nutrients.vitaminETocopherol, nutrients.vitaminK, "")
        formatVitamineLine(nutrients.zinco, null, "")

        htmlTemplate = htmlTemplate.replace("[QUANTITY]", quantityStr)
        htmlTemplate = htmlTemplate.replace("[PORTION]", portionStr)
        htmlTemplate = htmlTemplate.replace("[UNIT]", product.unit)
        htmlTemplate = htmlTemplate.replace("[NUTRIENTS_ITEMS]", nutrientInfosHTML.joinToString(separator = ""))
        htmlTemplate = htmlTemplate.replace("[VITAMINS_ITEMS]", vitaminsInfosHTML.joinToString(separator = ""))

        nutrients_table_webview.loadDataWithBaseURL("", htmlTemplate, "text/html", "UTF-8", "")

    }

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
        private const val NUTRIENT_MAIN_HTML_TEMPLATE =
            "<tr><th colspan=\"2\"><b>%1s</b></th><td style=\"text-align:right\"><b>%2s</b></td><td style=\"text-align:right\"><b>%3s</b></td><td><b>%4s</b></td></tr>"
        private const val NUTRIENT_SUB_HTML_TEMPLATE =
            "<tr><td class=\"blank-cell\"></td><th>%1s</th><td style=\"text-align:right\"><b>%2s</b></td><td style=\"text-align:right\"><b>%3s</b></td><td style=\"text-align:right\"><b>%4s</b></td></tr>"
        private const val VITAMINE_HTML_TEMPLATE = "<tr><td colspan=\"2\"> • %1s</td><td> • %2s</td></tr>"

        private val DEFAULT_LANG = Lang.ENGLISH

        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_details

        @LayoutRes
        private const val LAYOUT_SCENE_LOADING_ID = R.layout.fragment_details_scene_loading

        @LayoutRes
        private const val LAYOUT_SCENE_DETAILS_ID = R.layout.fragment_details_scene_details

        @LayoutRes
        private const val LAYOUT_SCENE_NOT_FOUND_ID = R.layout.fragment_details_scene_not_found
    }

}