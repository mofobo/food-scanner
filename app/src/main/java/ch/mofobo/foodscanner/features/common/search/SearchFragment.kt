package ch.mofobo.foodscanner.features.common.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import ch.mofobo.foodscanner.R
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : DialogFragment() {

    private lateinit var navController: NavController

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT_ID, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        prepareView()
        oberveViewModel()
    }

    private fun navigateTo(destination: Int) {
        navController.navigate(destination)
    }

    private fun prepareView() {

    }

    private fun oberveViewModel() {
        viewModel.text.observe(viewLifecycleOwner, Observer {
            text.text = it
        })
    }

    override fun onResume() {
        super.onResume()
    }


    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    companion object {

        private const val LAYOUT_ID = R.layout.fragment_search
    }

}