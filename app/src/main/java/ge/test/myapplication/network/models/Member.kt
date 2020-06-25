package ge.test.myapplication.network.models

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class Member(
    val id: Int,
    val position: Int,
    val name: String,
    val imageUrl: String,
    val hours: String,
    var image: Bitmap?
) {
}