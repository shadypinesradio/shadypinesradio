package com.shadypines.radio.view.authentication.signin

//import kotlinx.android.synthetic.main.activity_sign_in.*
//import kotlinx.android.synthetic.main.dialog_sign_in_layout.*
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.shadypines.radio.R
import com.shadypines.radio.databinding.ActivitySignInBinding
import com.shadypines.radio.view.authentication.forgot.ForgotActivity
import com.shadypines.radio.view.authentication.signup.SignUpActivity
import com.shadypines.utils.Constants
import com.shadypines.utils.ProgressDialogUtils
import com.shadypines.utils.TimeUtils
import com.validator.easychecker.EasyChecker
import com.validator.easychecker.exceptions.InputErrorException
import com.validator.easychecker.util.PasswordPattern
import java.util.*


class SignInActivity : AppCompatActivity(), SignInView {

    val signInPresenter = SignInPresenter(this)
    var googleSignInClient: GoogleSignInClient? = null

    //it's not right way..
    var presenter: SignInPresenter? = null
    val RC_SIGN_IN = 12039
    private lateinit var callbackManager: CallbackManager
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivitySignInBinding.inflate(LayoutInflater.from(this))
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        setContentView(binding.root)
        binding.parentLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            binding.parentLayout.getWindowVisibleDisplayFrame(r)
            val screenHeight: Int = binding.parentLayout.getRootView().getHeight()
            val keypadHeight: Int = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                binding.scrollView.smoothScrollTo(0, binding.btnSignIn.y.toInt())
            }
        }

        setListener()



        //todo: Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    private fun setListener() {

        binding.txtSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.textForgot.setOnClickListener {
            startActivity(Intent(this, ForgotActivity::class.java))
        }
        /*text_need?.setOnClickListener {
            startActivity(Intent(this, ProfileDetailsActivity::class.java))
        }*/
        binding.btnSignIn.setOnClickListener {
            validateSignInShow()
        }
        binding.imageBack.setOnClickListener {
            finish()
        }

    }

    /**
     * email validator..
     */
    private fun validateSignInShow() {
        try {
            var view = this.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(this)
            }
            val imm =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            ProgressDialogUtils.on().showProgressDialog(this)

            signInPresenter.signIn(
                    binding.etEmailSignIn?.text.toString(),
                    binding.etPasswordSignIn?.text.toString(),
            )

        } catch (exception: InputErrorException) {
            Toast.makeText(this, exception.messageText, Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * sign in success..
     */
    override fun onSignInSuccess() {

         //SharedPrefUtils.write(Constants.Preferences.EMAIL,)

        runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(this, "Sign in Successfully", Toast.LENGTH_SHORT).show()
            Constants.Default.CAME_FROM_LOGIN_Schedule = true
            Constants.Default.CAME_FROM_LOGIN_Home = true
            finish()
            Constants.Preferences.valued = 12
        }

    }


    /**
     * sign in error.
     */
    override fun onSignInError(message: String) {
        runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * google sign in success..
     */
    override fun onGoogleSignInSuccess(email: String, day: String) {
        runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(this, "Sign in Successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    /**
     * google sign in error..
     */
    override fun onGoogleSignInError(message: String) {
        runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * todo: google sign in
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d("infos",task.toString())
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                ProgressDialogUtils.on().showProgressDialog(this)
                presenter?.socialGoogleSignIn(
                    "shady"+account.email!!, account.id!!, account.displayName!!,
                    TimeUtils.getCurrentDayName().toUpperCase(Locale.getDefault())
                )
            } catch (e: ApiException) {
                e.printStackTrace()
                // Google Sign In failed, update UI appropriately
                Toast.makeText(
                    this, "Google sign-in failed, try again!", Toast
                        .LENGTH_LONG
                ).show()
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}