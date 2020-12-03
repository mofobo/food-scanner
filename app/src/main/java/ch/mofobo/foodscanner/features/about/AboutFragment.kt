package ch.mofobo.foodscanner.features.about

import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ch.mofobo.foodscanner.BuildConfig
import ch.mofobo.foodscanner.R
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_details.toolbar
import kotlinx.android.synthetic.main.fragment_scanner.*
import org.koin.android.viewmodel.ext.android.viewModel


class AboutFragment : DialogFragment() {

    private lateinit var navController: NavController
    private val viewModel: AboutViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT_ID, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)
        prepareView()
    }

    private fun prepareView() {
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        toolbar.title = requireContext().getString(R.string.about_toolbar_title)

        setVersion()

        git.setOnClickListener { goToGitWebPage() }
        developer.setOnClickListener { goToLinkedinWebPage() }
        store.setOnClickListener { gotToGooglePlayAppPage() }
        email.setOnClickListener { mailToDeveloper() }
    }

    private fun setVersion() {
        val packageInfo: PackageInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)

        val versionCode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode
        }

        val environment = BuildConfig.ENVIRONMENT

        version.text = "v. ${packageInfo.versionName} ($versionCode)${if (environment.isNullOrBlank()) "" else " - $environment"}"
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    private fun goToGitWebPage() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.FOODSCANNER_GITHUB_URL))
        startActivity(browserIntent)
    }

    private fun goToLinkedinWebPage() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.LINKEDIN_MOFOBO_URL))
        startActivity(browserIntent)
    }

    private fun gotToGooglePlayAppPage() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(BuildConfig.GOOGLE_PLAY_APP_PAGE_URL)
            setPackage("com.android.vending")
        }
        startActivity(intent)
    }

    private fun mailToDeveloper() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, BuildConfig.DEVELOPER_EMAIL)
            putExtra(Intent.EXTRA_SUBJECT, requireContext().getString(R.string.app_name))
            putExtra(Intent.EXTRA_TEXT, "Hello Mohammed")
        }
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            startActivity(Intent.createChooser(intent, "Send email..."))
        }

    }

    companion object {
        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_about
    }

}