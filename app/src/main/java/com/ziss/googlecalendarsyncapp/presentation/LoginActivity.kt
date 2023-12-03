package com.ziss.googlecalendarsyncapp.presentation

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.ziss.googlecalendarsyncapp.MainActivity
import com.ziss.googlecalendarsyncapp.databinding.ActivityLoginBinding
import com.ziss.googlecalendarsyncapp.viewmodels.LoginViewModel


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var googleSignInClient: GoogleSignInClient

    private val launcherSignInIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

                try {
                    val account = task.getResult(ApiException::class.java)
                    MainActivity.start(this, account)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            MainActivity.start(this, account)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        googleSignInClient = viewModel.loginWithGoogle(this)

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val signInIntent = googleSignInClient.signInIntent
        launcherSignInIntent.launch(signInIntent)
    }
}