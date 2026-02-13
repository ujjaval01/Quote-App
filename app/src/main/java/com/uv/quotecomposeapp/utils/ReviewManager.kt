package com.uv.quotecomposeapp.utils

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory

fun launchInAppReview(activity: Activity) {

    val manager = ReviewManagerFactory.create(activity)
    val request = manager.requestReviewFlow()

    request.addOnCompleteListener { task ->
        if (task.isSuccessful) {

            val reviewInfo = task.result
            val flow = manager.launchReviewFlow(activity, reviewInfo)

            flow.addOnCompleteListener {
                // Review flow finished
            }
        }
    }
}
