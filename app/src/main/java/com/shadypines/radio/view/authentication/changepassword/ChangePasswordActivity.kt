package com.shadypines.radio.view.authentication.changepassword

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shadypines.radio.databinding.ActivityChangePasswordBinding
import com.shadypines.radio.view.activity.TempActivity
import com.shadypines.utils.ProgressDialogUtils
import com.validator.easychecker.EasyChecker
import com.validator.easychecker.exceptions.InputErrorException
import com.validator.easychecker.util.PasswordPattern

class ChangePasswordActivity : AppCompatActivity(), ChangePasswordView {
    private lateinit var binding : ActivityChangePasswordBinding
    val changePasswordPresenter = ChangePasswordPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivityChangePasswordBinding.inflate(LayoutInflater.from(this))
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        setContentView(binding.root)

        setListener()

    }

    private fun setListener() {
        binding.btnChangePassSubmit.setOnClickListener {
            validateSignInShow();
        }
    }

    private fun validateSignInShow() {
        try {
            EasyChecker.validateInput(
                    this, 6,
                    PasswordPattern.PASSWORD_PATTERN_NONE,
                    binding.etCurrentPassword, binding.etNewPassword,
            )
            ProgressDialogUtils.on().showProgressDialog(this)

            changePasswordPresenter.changePassword(
                    binding.etCurrentPassword.text.toString(),
                    binding.etNewPassword.text.toString(),
            )

        } catch (exception: InputErrorException) {
            Toast.makeText(this, exception.messageText, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onChangePasswordSuccess() {
        runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(this, "Change password in Successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, TempActivity::class.java))
            finish()
        }
    }

    override fun onChangePasswordError(message: String) {
        runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}