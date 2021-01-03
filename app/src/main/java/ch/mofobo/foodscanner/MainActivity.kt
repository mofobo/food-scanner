package ch.mofobo.foodscanner

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ch.mofobo.foodscanner.features.common.SharedViewModel
import ch.mofobo.foodscanner.features.scanner.ScannerFragmentDirections
import ch.mofobo.foodscanner.features.scanner.camera.CameraPermissionManager
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var alert: AlertDialog

    private var selectedLocale: Locale? = null

    private lateinit var navController: NavController

    val cameraPermissionManager = CameraPermissionManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

        setPreferredLocale()

        setContentView(
            R.layout.activity_main
        )


        val navView: BottomNavigationView = findViewById(
            R.id.nav_view
        )

        navController = findNavController(
            R.id.nav_host_fragment
        )
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_scanner,
                R.id.navigation_history
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        MobileAds.initialize(this) { }


        setPreferredLocale()

        prepareLanguageSelectionDialog()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.one -> {
                showLanguageSelectionDialog()
                return true
            }
            R.id.two -> {
                navigateToAbout()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun prepareLanguageSelectionDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle(R.string.settings_i18n_title)
        val items = arrayOf("English", "Français", "Deutsch", "Italiano")

        val checkedItem = when ((sharedViewModel.getLocale() ?: Locale.getDefault()).language) {
            Locale.ENGLISH.language -> 0
            Locale.FRENCH.language -> 1
            Locale.GERMAN.language -> 2
            Locale.ITALIAN.language -> 3
            else -> 0
        }

        alertDialog.setSingleChoiceItems(items, checkedItem) { _, which ->
            selectedLocale = when (which) {
                0 -> Locale.ENGLISH
                1 -> Locale.FRENCH
                2 -> Locale.GERMAN
                3 -> Locale.ITALIAN
                else -> Locale.ENGLISH
            }
        }
        alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.setButton(Dialog.BUTTON_POSITIVE, "OK") { _, _ ->
            if (selectedLocale == null) return@setButton

            sharedViewModel.setLocale(selectedLocale!!)

            val intent = this.intent
            this.finish()
            startActivity(intent)
        }
    }

    private fun showLanguageSelectionDialog() {
        alert.show()
    }


    private fun navigateToAbout() {
        navController.navigate(ScannerFragmentDirections.actionNavigationToAbout())
    }

    private fun setPreferredLocale() {
        val preferredLocale = sharedViewModel.getLocale()
        preferredLocale.let {
            val config = Configuration()
            config.locale = it
            val resources = resources
            resources.updateConfiguration(config, resources.displayMetrics)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        cameraPermissionManager.onRequestPermissionsResult(requestCode, grantResults)
    }
}