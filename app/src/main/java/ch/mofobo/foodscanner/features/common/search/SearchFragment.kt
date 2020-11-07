package ch.mofobo.foodscanner.features.common.search

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import ch.mofobo.foodscanner.R
import ch.mofobo.foodscanner.features.scanner.camera.analyzer.BarcodeAnalyzer
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.Executors

class SearchFragment : DialogFragment() {

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT_ID, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oberveViewModel()

    }

    private fun oberveViewModel() {
        searchViewModel.text.observe(viewLifecycleOwner, Observer {
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