package ch.mofobo.foodscanner.features.scanner

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import ch.mofobo.foodscanner.R
import ch.mofobo.foodscanner.domain.model.Product
import kotlinx.android.synthetic.main.fragment_scanner.*
import org.koin.android.viewmodel.ext.android.viewModel

class ScannerFragment : Fragment() {

    private val scannerViewModel: ScannerViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_scanner, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clear_btn.isEnabled = false
        search_btn.isEnabled = false

        observeViewModel()

        barcode_input.addTextChangedListener(
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
        scannerViewModel.products.observe(viewLifecycleOwner, Observer {
            val product: Product? = it.data
            if (product != null) {
                or_label.text = product.data.display_name_translations.fr
            }
        })
    }
}