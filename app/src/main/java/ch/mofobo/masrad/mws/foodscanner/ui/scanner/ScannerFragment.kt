package ch.mofobo.masrad.mws.foodscanner.ui.scanner

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ch.mofobo.masrad.mws.foodscanner.R
import kotlinx.android.synthetic.main.fragment_scanner.*


class ScannerFragment : Fragment() {

    private lateinit var scannerViewModel: ScannerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        scannerViewModel =
            ViewModelProvider(this).get(ScannerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_scanner, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clear_btn.isEnabled = false
        search_btn.isEnabled = false

        barcode_input.addTextChangedListener(object : TextWatcher {
            // Before EditText text change.
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}


            // This method is invoked after user input text in EditText.
            override fun afterTextChanged(editable: Editable) {
                val isTextAvailable = editable.length != 0
                clear_btn.isEnabled = isTextAvailable
                search_btn.isEnabled = isTextAvailable
            }
        })


    }


}