package com.ziss.googlecalendarsyncapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.ziss.googlecalendarsyncapp.databinding.ActivityMainBinding
import com.ziss.googlecalendarsyncapp.presentation.LoginActivity
import com.ziss.googlecalendarsyncapp.viewmodels.LoginViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient

    private var account: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.event)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        googleSignInClient = loginViewModel.loginWithGoogle(this)
        account = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_ACCOUNT, GoogleSignInAccount::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_ACCOUNT)
        }

        if (account != null) {
            binding.text.text = account?.displayName
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_action -> {
                logoutDialog().show()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logoutDialog(): AlertDialog.Builder {
        return AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.logout_alert)).setPositiveButton(getString(R.string.ok)) { _, _ ->
                LoginActivity.start(this@MainActivity)
                finish()
                logOut()
            }.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
            create()
        }
    }

    private fun logOut() {
        googleSignInClient.signOut().addOnSuccessListener {
            finish()
            LoginActivity.start(this)
        }
    }

    companion object {
        private const val EXTRA_ACCOUNT = "extra_account"

        fun start(context: Context, account: GoogleSignInAccount) {
            val intent = Intent(context, MainActivity::class.java).apply {
                this.putExtra(EXTRA_ACCOUNT, account)
            }
            context.startActivity(intent)
        }
    }
}