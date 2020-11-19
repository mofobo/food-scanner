package ch.mofobo.foodscanner.features.scanner.camera

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.mofobo.foodscanner.utils.SingleLiveEvent

/**
 * Helper to check and request camera permissions
 * @param activity the activity which implements onRequestPermissionsResult()

 */
class CameraPermissionManager(private val activity: Activity) {

    val cameraPermissionEmitter = SingleLiveEvent<Boolean>()

    /**
     * Checks if permission to access 'camera' is granted or not
     *
     * @return false if the permissions are already granted, and true if not.
     */
    fun needCameraPermissions(): Boolean {
        var needPermissions = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            needPermissions = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        }

        return needPermissions
    }

    /**
     * Requests the user's permission to 'camera' access.
     * To receive the result in your rx callback, you need to implement onRequestPermissionsResult()
     * in your activity and call the same method of this LocationPermissionManager there.
     *
     * @return true if the permissions are granted, and false if not.
     */
    fun requestCameraPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS)

        } else {
            cameraPermissionEmitter.postValue(true)
        }

    }

    /**
     *
     * Provide the response of the permission request by calling this
     * method in your activity's onRequestPermissionsResult()
     *
     * @param requestCode the received requestCode
     * @param grantResults the received grantResults
     */
    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        val permissionsGranted = requestCode == REQUEST_CODE_PERMISSIONS && grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
        cameraPermissionEmitter.postValue(permissionsGranted)
    }

    companion object {
        // This is an arbitrary number we are using to keep track of the permission
        // request. Where an app has multiple context for requesting permission,
        // this can help differentiate the different contexts.
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}


