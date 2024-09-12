package com.shadypines.utils

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory

class ReviewUtils {

    companion object {
        /**
         * Ask for review from the user after the first time the alarm plays
         */
        fun askForReview(activity: Activity) {
            val manager = ReviewManagerFactory.create(activity)
            val request: Task<ReviewInfo> = manager.requestReviewFlow()

            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // We got the ReviewInfo object
                    val reviewInfo = task.result
                    val flow: Task<Void> = manager.launchReviewFlow(activity, reviewInfo)

                    flow.addOnCompleteListener { flowTask ->
                        if (flowTask.isSuccessful) {
                            // The review flow has finished successfully
                            // Note: The API does not indicate whether the user reviewed or not,
                            // or even whether the review dialog was shown. Thus, no matter the result,
                            // continue your app flow.
                        } else {
                            // Handle any errors (e.g., the user is not eligible for the review flow)
                            val errorCode = flowTask.exception?.message
                            // Log or handle the error
                        }
                    }
                } else {
                    // Handle the error from requestReviewFlow
                    val errorCode = task.exception?.message
                    // Log or handle the error
                }
            }
        }
    }
}