package com.shadypines.radio.view.authentication.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shadypines.radio.databinding.ActivitySignUpBinding
import com.shadypines.radio.view.authentication.signin.SignInActivity
import com.shadypines.utils.ProgressDialogUtils
import com.validator.easychecker.EasyChecker
import com.validator.easychecker.exceptions.InputErrorException
import com.validator.easychecker.util.PasswordPattern
//import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), SignUpView {
    val signUpPresenter = SignUpPresenter(this)
    //val schedulePresenter = SchedulePresenter(this)
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivitySignUpBinding.inflate(LayoutInflater.from(this))
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        setContentView(binding.root)
        setListener()
    }

    private fun setListener() {
        binding.btnSignUp.setOnClickListener {
            validateSignUpShow()
        }
        binding.txtSignIn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
        binding.imageBackSignUp.setOnClickListener {
           finish();
        }
    }

    private fun validateSignUpShow() {
        try {
            EasyChecker.validateInput(
                this, 6,
                PasswordPattern.PASSWORD_PATTERN_NONE,
                    binding.etFirstNameSignUp,
                    binding.etUserNameSignUp,
                    binding.etEmailSignUp,
                    binding.etPasswordSignUp,
                    binding.etConfirmPasswordSignUp
            )
            ProgressDialogUtils.on().showProgressDialog(this)
            signUpPresenter.signUp(
                    binding.etFirstNameSignUp?.text.toString(),"",
                    binding.etEmailSignUp?.text.toString(),
                    binding.etPasswordSignUp?.text.toString(),
                    binding.etConfirmPasswordSignUp?.text.toString(),
                isSendEmail = "True",
                    binding.etUserNameSignUp?.text.toString()
            )

        } catch (exception: InputErrorException) {
            Toast.makeText(this, exception.messageText, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSignUpSuccess() {
        runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(this, "Please check your email to verify your account.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    override fun onSignUpError(message: String) {
        runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}