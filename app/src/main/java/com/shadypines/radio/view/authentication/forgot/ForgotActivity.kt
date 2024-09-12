package com.shadypines.radio.view.authentication.forgot

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shadypines.radio.databinding.ActivityForgotBinding
import com.shadypines.radio.view.authentication.signin.SignInActivity
import com.shadypines.radio.view.authentication.social.SocialActivity
import com.shadypines.utils.ProgressDialogUtils
import com.validator.easychecker.EasyChecker
import com.validator.easychecker.exceptions.InputErrorException
import com.validator.easychecker.util.PasswordPattern
//import kotlinx.android.synthetic.main.activity_forgot.*
//import kotlinx.android.synthetic.main.activity_sign_in.*

class ForgotActivity : AppCompatActivity(), ForgotView {
    val forgotPresenter = ForgotPresenter(this)
    private lateinit var binding:ActivityForgotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivityForgotBinding.inflate(LayoutInflater.from(this))
        this.window.setFlags(
            WindowManager.LayoutParams
                .FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        setContentView(binding.root)
        setListener()


    }

    private fun setListener() {
        binding.btnForgot.setOnClickListener {
            validateForgotPassShow();
        }
        binding.textBackToLogin?.setOnClickListener {
            startActivity(Intent(this, SocialActivity::class.java));
        }
        binding.imageBackForgot?.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
    }

    private fun validateForgotPassShow() {
        try {
            EasyChecker.validateInput(
                this, 6,
                PasswordPattern.PASSWORD_PATTERN_NONE,
                    binding.etForgotEmail
            )
            ProgressDialogUtils.on().showProgressDialog(this)
            forgotPresenter.forgotResetPassword(
                    binding.etForgotEmail?.text.toString(),
            )

        } catch (exception: InputErrorException) {
            Toast.makeText(this, exception.messageText, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onForgotSuccess() {
        runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(this, "A password reset link has been sent to your email.",
                Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

    }

    override fun onForgotError(message: String) {
        runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

}