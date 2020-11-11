package ch.mofobo.foodscanner.features.scanner

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ch.mofobo.foodscanner.R
import kotlinx.android.synthetic.main.fragment_scanner.*
import org.koin.android.viewmodel.ext.android.viewModel

class ScannerFragment : Fragment() {

    private lateinit var navController: NavController

    private val viewModel: ScannerViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT_ID, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()
        prepareView()
        observeViewModel()
    }

    private fun prepareView() {
        clear_btn.isEnabled = false
        search_btn.isEnabled = false

        clear_btn.setOnClickListener { barcode_manual_input.text?.clear() }
        search_btn.setOnClickListener {
            navController.navigate(ScannerFragmentDirections.actionNavigationToSearch("7613312089040"))
        }

        scan_btn.setOnClickListener {
            navController.navigate(ScannerFragmentDirections.actionNavigationScannerToCamera())
        }


        barcode_manual_input.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

                override fun afterTextChanged(editable: Editable) {
                    val isTextAvailable = editable.length != 0
                    clear_btn.isEnabled = isTextAvailable
                    search_btn.isEnabled = isTextAvailable
                }
            })
    }

    private fun observeViewModel() {
    }

    companion object {
        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_scanner
    }
}