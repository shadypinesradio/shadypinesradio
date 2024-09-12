package com.shadypines.radio.view.authentication.social

//import kotlinx.android.synthetic.main.activity_sign_in.*
//import kotlinx.android.synthetic.main.activity_sign_in.image_back
//import kotlinx.android.synthetic.main.activity_social.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.shadypines.radio.R
import com.shadypines.radio.databinding.ActivitySocialBinding
import com.shadypines.radio.view.authentication.signin.SignInActivity
import com.shadypines.radio.view.authentication.signin.SignInPresenter
import com.shadypines.radio.view.authentication.signin.SignInView
import com.shadypines.utils.Constants
import com.shadypines.utils.ProgressDialogUtils
import com.shadypines.utils.TimeUtils
import java.util.*

class SocialActivity : AppCompatActivity(), SignInView {

    val signInPresenter = SignInPresenter(this)
    var googleSignInClient: GoogleSignInClient? = null
    private lateinit var binding:ActivitySocialBinding

    //it's not right way..
    var presenter: SignInPresenter? = null
    val RC_SIGN_IN = 12039
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialBinding.inflate(LayoutInflater.from(this))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        setContentView(binding.root)

        callbackManager = CallbackManager.Factory.create();
        presenter = SignInPresenter(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setListener();
    }

    override fun onResume() {
        super.onResume()
        if (Constants.Preferences.valued == 12) {
            Constants.Preferences.valued = 0
            finish()

        }
    }
    private fun setListener() {
        binding.imageBackSocial?.setOnClickListener {
            finish()
        }
        binding.btnGoogleSignIn.setOnClickListener {
            val signInIntent = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        binding.btnEmailSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

    }


    /**
     *  sign in success and sign in error not necessary....
     */
    override fun onSignInSuccess() {
    }

    override fun onSignInError(message: String) {
    }

    override fun onGoogleSignInSuccess(email: String, day: String) {
        runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(this, "Sign in Successfully", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
            Constants.Default.CAME_FROM_LOGIN_Schedule = true
            Constants.Default.CAME_FROM_LOGIN_Home = true
            onBackPressed()
        }
    }

    override fun onGoogleSignInError(message: String) {
        runOnUiThread {
            ProgressDialogUtils.on().hideProgressDialog()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)!!
                ProgressDialogUtils.on().showProgressDialog(this)
                presenter?.socialGoogleSignIn(
                    "shady" + account.email!!, account.id!!, account.displayName!!,
                    TimeUtils.getCurrentDayName().toUpperCase(Locale.getDefault())
                )
            } catch (e: ApiException) {
                e.printStackTrace()
                Log.e("google sign-in error: ", e.toString())
                // Handle Google Sign-In failure
                Toast.makeText(
                    this, "Google sign-in failed, try again!", Toast.LENGTH_LONG
                ).show()
            }
        } else {
            // Handle other activity results if needed
        }
    }

}