package ch.mofobo.foodscanner.features.scanner.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import ch.mofobo.foodscanner.R
import ch.mofobo.foodscanner.features.scanner.camera.analyzer.BarcodeAnalyzer
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.fragment_camera.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.Executors

class CameraFragment : DialogFragment() {

    private lateinit var navController: NavController

    private val viewModel: CameraViewModel by viewModel()

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    // This is an array of all the permission specified in the manifest.
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT_ID, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        prepareView()
        prepareCamera()
        oberveViewModel()
    }

    override fun onResume() {
        super.onResume()
        // requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        if (!allPermissionsGranted()) {
            requireActivity().requestPermissions(REQUIRED_PERMISSIONS, Companion.REQUEST_CODE_PERMISSIONS)
        }

    }


    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    private fun navigateTo(destination: NavDirections) {
        navController.navigate(destination)
    }

    private fun prepareView() {
    }

    private fun prepareCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        // Request camera permissions
        if (allPermissionsGranted()) {
            cameraProvider()
        }
    }

    private fun oberveViewModel() {
        viewModel.actions.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is CameraViewModel.Action.SearchBarcode -> navController.navigate(CameraFragmentDirections.actionNavigationToDetails(-1, it.barcode))
                }
            }
        })


        viewModel.text.observe(viewLifecycleOwner, Observer {
        })
    }


    private fun cameraProvider() {
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            startCamera(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun startCamera(cameraProvider: ProcessCameraProvider) {
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val preview = Preview.Builder().apply {
            setTargetResolution(Size(previewView.width, previewView.height))
        }.build()

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(previewView.width, previewView.height))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { qrResult ->
                    previewView.post {
                        qrResult?.let { result ->
                            viewModel.onBarcodeScanned(result.text)
                        }
                    }
                })
            }

        cameraProvider.unbindAll()

        val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
        preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.cameraInfo))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraProvider()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                // finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_camera

        // This is an arbitrary number we are using to keep track of the permission
        // request. Where an app has multiple context for requesting permission,
        // this can help differentiate the different contexts.
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}