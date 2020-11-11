package ch.mofobo.foodscanner.features.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ch.mofobo.foodscanner.R
import kotlinx.android.synthetic.main.fragment_history.*
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private lateinit var navController: NavController

    private val viewModel: HistoryViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT_ID, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()
        prepareView()
        observeViewModel()
    }

    private fun navigateTo(destination: Int) {
        navController.navigate(destination)
    }

    private fun prepareView() {

    }

    private fun observeViewModel() {
        viewModel.text.observe(viewLifecycleOwner, Observer {
            text_notifications.text = it
        })
    }

    companion object {
        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_history
    }
}