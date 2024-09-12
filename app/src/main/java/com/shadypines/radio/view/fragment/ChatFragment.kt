package com.shadypines.radio.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shadypines.radio.databinding.FragmentChatBinding
import com.shadypines.radio.utils.AppSettings
import im.delight.android.webview.AdvancedWebView


class ChatFragment : Fragment() {

    var show : Int = 0
    lateinit var binding: FragmentChatBinding
    private var mCM: String? = null
    var mUM: ValueCallback<Uri>? = null
    var mUMA: ValueCallback<Array<Uri>>? = null
    val FCR = 1
    private var webView: WebView? = null


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false)
        webView = binding.webViewChatting
        binding.textMarqueChatFm.text = AppSettings.title + " - " + AppSettings.album + "    "
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true)

        webView?.settings?.javaScriptEnabled = true
        webView?.settings?.domStorageEnabled = true

        binding.textMarqueChatFm.isSelected = true
        binding.textMarqueChatFm.text = AppSettings.title + " - " + AppSettings.album + "    "
        startCounter()
        binding.forwardButton.setOnClickListener {
            if (webView?.canGoForward()!!) {
                webView?.goForward()
            }
        }
      binding.bottoam.visibility = View.GONE
        val param = webView?.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0,0,0,0)
        webView?.layoutParams = param

        webView?.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView, progress: Int) {
                Log.e("progress", "" + progress)
                if (progress == 100) { //...page is fully loaded.
                    if (webView?.canGoBack()!!) {
                        binding.bottoam.visibility = View.VISIBLE
                        param.setMargins(0,0,0,25)
                        webView?.layoutParams = param
                    }else {
                        binding.bottoam.visibility = View.GONE
                        param.setMargins(0,0,0,0)
                        webView?.layoutParams = param
                    }
                    // TODO - Add whatever code you need here based on web page load completion...
                }
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                mUMA = filePathCallback
                val intent = Intent(Intent.ACTION_GET_CONTENT) // Or Intent.ACTION_OPEN_DOCUMENT
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*" // Set MIME type to image
                // Check if there is an activity that can handle the intent
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivityForResult(intent, FCR)
                } else {
                    // No activity to handle the intent, handle the error
                    Log.e("ChatFragment", "No Activity found to handle select image intent")
                }
                return true
            }
        }

        // webView?.setMixedContentAllowed(false)
        webView?.loadUrl("https://www6.cbox.ws/box/?boxid=848135&boxtag=S3UNz0")
    }

    private val handler = Handler(Looper.getMainLooper())
    private var currentText: String = ""
    private val updateTextRunnable: Runnable = object : Runnable {
        override fun run() {
            val newText = "${AppSettings.title} - ${AppSettings.album}    "
            if (newText != currentText) {
                binding.textMarqueChatFm.text = newText
                currentText = newText
            }
            handler.postDelayed(this, 1000)
        }
    }

    fun startCounter() {
        handler.post(updateTextRunnable)
    }


    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }


    override fun onPause() {
        super.onPause()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateTextRunnable)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == FCR && resultCode == Activity.RESULT_OK) {
            if (null == mUMA) {
                return
            }
            if (intent == null) {
                // Handle the case when no image is selected
                return
            }
            val result: Uri? = intent.data
            if (result != null) {
                mUMA?.onReceiveValue(arrayOf(result))
            } else {
                mUMA?.onReceiveValue(null)
            }
            mUMA = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

