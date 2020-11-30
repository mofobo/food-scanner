package ch.mofobo.foodscanner.features.scanner.camera

import android.Manifest
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.Surface
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
import androidx.navigation.fragment.NavHostFragment
import ch.mofobo.foodscanner.MainActivity
import ch.mofobo.foodscanner.R
import ch.mofobo.foodscanner.features.scanner.camera.analyzer.BarcodeAnalyzer
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.fragment_camera.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.Executors

class CameraFragment : DialogFragment() {

    private var isCameraInitialized: Boolean = false
    private lateinit var navController: NavController

    private val viewModel: CameraViewModel by viewModel()

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    private lateinit var cameraSelector: CameraSelector
    private lateinit var preview: Preview
    private lateinit var imageAnalysis: ImageAnalysis

    val mainActivity: MainActivity
        get() = requireActivity() as MainActivity

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
        if (mainActivity.cameraPermissionManager.needCameraPermissions()) {
            mainActivity.cameraPermissionManager.requestCameraPermissions()
        } else if (isCameraInitialized) {
            startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        if (!mainActivity.cameraPermissionManager.needCameraPermissions()) stopCamera()
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }


    private fun prepareView() {
    }

    private fun prepareCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({

            cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            preview = Preview.Builder().apply {
                setTargetResolution(Size(previewView.width, previewView.height))
            }.build()


            imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(previewView.width, previewView.height))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { qrResult ->
                        previewView?.let {
                            it.post {
                                qrResult?.let { result ->
                                    viewModel.onBarcodeScanned(result.text)
                                }
                            }
                        }
                    })
                }

            if (!mainActivity.cameraPermissionManager.needCameraPermissions() && !isCameraInitialized) {
                isCameraInitialized = true
                startCamera()
            }


        }, ContextCompat.getMainExecutor(requireContext()))

    }

    private fun oberveViewModel() {
        viewModel.actions.observe(viewLifecycleOwner, Observer {
            it.let {
                when (it) {
                    is CameraViewModel.Action.SearchBarcode -> navController.navigate(CameraFragmentDirections.actionNavigationToDetails(-1, it.barcode))
                }
            }
        })


        mainActivity.cameraPermissionManager.cameraPermissionEmitter.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> startCamera()
                false -> {
                    Toast.makeText(requireContext(), "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                }
            }
        })
    }


    private fun startCamera() {
        val cameraProvider = cameraProviderFuture.get()
        cameraProvider.unbindAll()
        val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
        preview.setSurfaceProvider(previewView.createSurfaceProvider(camera.cameraInfo))
    }

    private fun stopCamera() {
        val cameraProvider = cameraProviderFuture.get()
        cameraProvider.unbindAll()
    }

    companion object {
        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_camera
    }

}