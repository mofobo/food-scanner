package ch.mofobo.foodscanner.features.scanner

import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ch.mofobo.foodscanner.MainActivity
import ch.mofobo.foodscanner.R
import ch.mofobo.foodscanner.features.common.SharedViewModel
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_scanner.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class ScannerFragment : Fragment() {

    //private val sharedViewModel: SharedViewModel by viewModel()
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var navController: NavController

    private val mainActivity: MainActivity
        get() = requireActivity() as MainActivity


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT_ID, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)

        navController = view.findNavController()
        prepareView()
        prepareAds()
        observeViewModel()
    }

    private fun prepareView() {
        clear_btn.isEnabled = sharedViewModel.barcode.length != 0
        search_btn.isEnabled = sharedViewModel.barcode.length != 0

        clear_btn.setOnClickListener { resetBarcodeManualInput() }
        search_btn.setOnClickListener {
            navigateToProductDetails()
        }

        barcode_manual_input.setText(sharedViewModel.barcode)

        barcode_manual_input.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

                override fun afterTextChanged(editable: Editable) {
                    sharedViewModel.barcode = editable.toString()
                    val isTextAvailable = editable.length != 0
                    clear_btn.isEnabled = isTextAvailable
                    search_btn.isEnabled = isTextAvailable
                }
            })

        scan_btn.isEnabled = hasCameraHardware()
        scan_btn.setOnClickListener {
            checkPermissionAndNavigateToCamera()
        }
    }

    private fun checkPermissionAndNavigateToCamera() {
        if (mainActivity.cameraPermissionManager.needCameraPermissions()) {
            mainActivity.cameraPermissionManager.requestCameraPermissions()
        } else {
            navigateToCamera()
        }
    }

    private fun prepareAds() {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun navigateToCamera() {
        navController.navigate(ScannerFragmentDirections.actionNavigationScannerToCamera())
        resetBarcodeManualInput()
    }

    private fun navigateToProductDetails() {
        navController.navigate(ScannerFragmentDirections.actionNavigationToDetails(-1, sharedViewModel.barcode))
        resetBarcodeManualInput()
    }

    /** Check if this device has a camera */
    private fun hasCameraHardware(): Boolean {
        return requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    fun resetBarcodeManualInput() {
        sharedViewModel.barcode = ""
        barcode_manual_input.text?.clear()
    }

    private fun observeViewModel() {
        mainActivity.cameraPermissionManager.cameraPermissionEmitter.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> navigateToCamera()
                false -> {
                    Toast.makeText(requireContext(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    companion object {
        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_scanner
    }
}