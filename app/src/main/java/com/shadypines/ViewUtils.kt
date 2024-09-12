package com.shadypines

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import kotlin.math.roundToInt

/**
 * This is a class that contains utils for the view
 * @author Al Mujahid Khan
 * */
class ViewUtils {
    companion object {

        /**
         * This method converts pixels to dp
         *
         * @param px desired pixels
         * @return amount in dp
         * */
        fun pxToDp(px: Int): Float {
            val densityDpi = Resources.getSystem().displayMetrics.densityDpi.toFloat()
            return px / (densityDpi / 160f)
        }

        /**
         * This method converts dp to pixels
         *
         * @param dp desired amount of dp
         * @return amount in pixels
         * */
        fun dpToPx(dp: Int): Float {
            val density = Resources.getSystem().displayMetrics.density
            return (dp * density).roundToInt().toFloat()
        }

        /**
         * This method returns bitmap from the view
         *
         * @param view desired view
         * @return bitmap of the view
         * */
        fun getBitmapFromView(view: View): Bitmap? {
            val bitmap =
                Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            view.draw(Canvas(bitmap))

            return bitmap
        }






        /**
         * This method provides width of the screen
         *
         * @param context UI context
         * @return [Int] screen width in pixels
         * */
        fun getScreenWidth(context: Context): Int {
            val windowManager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            return dm.widthPixels
        }

        /**
         * This method provides height of the screen
         *
         * @param context UI context
         * @return [Int] screen height in pixels
         * */
        fun getScreenHeight(context: Context): Int {
            val windowManager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            return dm.heightPixels
        }





        /**
         * This method clears the light state of status bar
         *
         * @param activity current activity
         * */
        fun clearLightStatusBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val window = activity.window
                val flags = window.decorView.systemUiVisibility
                val modifiedFlags = flags and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
                window.decorView.systemUiVisibility = modifiedFlags
            }
        }

        /**
         * This method sets the light state of status bar
         *
         * @param activity current activity
         * */
        fun setLightStatusBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val window = activity.window
                val flags = window.decorView.systemUiVisibility
                val modifiedFlags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.decorView.systemUiVisibility = modifiedFlags
            }
        }

        /**
         * This method clears the light state of system navigation bar
         *
         * @param activity current activity
         * */
        fun clearLightSystemNavigationBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                val window = activity.window
                val flags = window.decorView.systemUiVisibility
                val modifiedFlags = flags and (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv())
                window.decorView.systemUiVisibility = modifiedFlags
            }
        }

        /**
         * This method sets the light state of system navigation bar
         *
         * @param activity current activity
         * */
        fun setLightSystemNavigationBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                val window = activity.window
                val flags = window.decorView.systemUiVisibility
                val modifiedFlags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                window.decorView.systemUiVisibility = modifiedFlags
            }
        }

    }
}