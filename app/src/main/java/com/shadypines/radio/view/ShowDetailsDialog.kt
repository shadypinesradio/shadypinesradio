package com.shadypines.radio.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.fragment.app.DialogFragment
import com.shadypines.radio.databinding.LayoutDialogOnboardingBinding

class ShowDetailsDialog : DialogFragment() {
    private lateinit var binding: LayoutDialogOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutDialogOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        initViewPager()
    }

    private fun setListener() {
        binding.btnFinish.setOnClickListener {
            dismiss()
        }
        binding.closeBtn.setOnClickListener {
            dismiss()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViewPager() {
        val url = arguments?.getString("URL")
        if (url != null) {
            binding.webviewShow.loadUrl(url)
        }
        val webSettings: WebSettings = binding.webviewShow.settings!!
        webSettings.javaScriptEnabled = true
        binding.webviewShow.settings?.loadWithOverviewMode = true
        binding.webviewShow.settings?.useWideViewPort = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            binding.webviewShow.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        } else {
            binding.webviewShow.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
