package dev.pengilly.android.navdeeplinktest

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dev.pengilly.android.navdeeplinktest.R

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = getNavController()
        navController.graph = navController.getAppNavGraph()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getNavController().handleDeepLink(intent)
    }

    private fun getNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController
    }

}