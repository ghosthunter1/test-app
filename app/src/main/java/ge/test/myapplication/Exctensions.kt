package ge.test.myapplication

import android.graphics.Bitmap
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import ge.test.myapplication.network.models.Member


fun ImageView.loadCircleImage(url: String, member: Member? = null, updateAdapter: ((bitmap: Bitmap?) -> Unit)? =null) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .addListener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                member?.image = resource
                updateAdapter?.invoke(resource)
                return false
            }

        })
        .signature(ObjectKey(System.currentTimeMillis().toString()))
        .apply(RequestOptions().circleCrop())
        .into(this)

}

fun ImageView.loadCircleImageFromDrawable(image: Bitmap) {
    Glide.with(this)
        .load(image)
        .signature(ObjectKey(System.currentTimeMillis().toString()))
        .apply(RequestOptions().circleCrop())
        .into(this)

}

fun TextView.setNumberDifferentSizeText(text: String) {
    val spannable = SpannableString(text)
    for ((i, k) in spannable.withIndex()) {
        if (k.isDigit()) {
            spannable.setSpan(RelativeSizeSpan(1.1f), i, i + 1, 0)
        } else {
            spannable.setSpan(RelativeSizeSpan(0.8f), i, i + 1, 0)
        }

    }
    setText(spannable)
}