package com.ziss.googlecalendarsyncapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.ziss.googlecalendarsyncapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var account: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.event)

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