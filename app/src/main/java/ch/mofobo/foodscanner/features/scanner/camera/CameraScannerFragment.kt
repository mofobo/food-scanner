package ch.mofobo.foodscanner.features.scanner.camera

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ch.mofobo.foodscanner.R

class CameraScannerFragment : DialogFragment() {

    private lateinit var cameraScannerViewModel: CameraScannerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cameraScannerViewModel =
            ViewModelProviders.of(this).get(CameraScannerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_camera_scanner, container, false)
        cameraScannerViewModel.text.observe(viewLifecycleOwner, Observer {

            // seteDATA FROM ViewModel
            //fighter_left.name.text = it
        })
        return root
    }

    override fun onResume() {
        super.onResume()
        // requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

    }

    override fun onPause() {
        super.onPause()
        // requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }


    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

}