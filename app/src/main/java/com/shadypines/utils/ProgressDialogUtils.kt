package com.shadypines.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import com.shadypines.radio.R
import java.lang.ref.WeakReference

class ProgressDialogUtils private constructor() {

    private var mAlertDialogReference: WeakReference<AlertDialog?> = WeakReference(null)
    private var mContextReference: WeakReference<Context?> = WeakReference(null)

    // Handler and Runnable for ellipsis animation
    private val handler = Handler()
    private var dotCount = 0
    private val runnable = object : Runnable {
        override fun run() {
            val textView = mAlertDialogReference.get()?.findViewById<AppCompatTextView>(R.id.text_view_message)
            val baseText = "Loading"
            val ellipsis = ".".repeat(dotCount)
            textView?.text = "$baseText$ellipsis"

            if (dotCount < 5) {
                dotCount++
            } else {
                dotCount = 0
            }

            handler.postDelayed(this, 100) // Update every 500 milliseconds
        }
    }

    @SuppressLint("InflateParams")
    fun showProgressDialog(context: Context): AlertDialog? {
        if (context !is Activity) {
            throw IllegalArgumentException("Context passed to ProgressDialogUtils must be an Activity context.")
        }

        hideProgressDialog()

        mContextReference = WeakReference(context)

        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater
                .from(context)
                .inflate(R.layout.progresss_dialog_layout, null, false)

        view.findViewById<AppCompatTextView>(R.id.text_view_message).setTypeface(null, Typeface.NORMAL)
        builder.setCancelable(true)
        builder.setView(view)

        val alertDialog = builder.create()
        mAlertDialogReference = WeakReference(alertDialog)

        alertDialog.window?.let { window ->
            // Applying animation style
            window.attributes?.windowAnimations = R.style.DialogAnimation

            // Positioning dialog at the top and a little bit down
            val windowParams = window.attributes
            windowParams.gravity = Gravity.TOP
            windowParams.y = 100
            window.attributes = windowParams

            // Adjusting dialog width and background
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }

        alertDialog.show()

        // Start the ellipsis animation
        handler.post(runnable)

        return alertDialog
    }

    fun hideProgressDialog() {
        mAlertDialogReference.get()?.let { dialog ->
            mContextReference.get()?.let { context ->
                if (context is Activity && !context.isFinishing) {
                    handler.removeCallbacks(runnable)  // Stop the ellipsis animation
                    dialog.dismiss()
                    mAlertDialogReference.clear()
                    mContextReference.clear()
                }
            }
        }
    }

    companion object {
        @Volatile private var sInstance: ProgressDialogUtils? = null

        fun on(): ProgressDialogUtils {
            return sInstance ?: synchronized(this) {
                val instance = ProgressDialogUtils()
                sInstance = instance
                instance
            }
        }
    }
}
