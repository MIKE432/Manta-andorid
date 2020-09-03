package com.apusart.manta.ui.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.gallery_slider.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object MiddleLayer {
    private object MiddleLayerHelper {

        fun storeBitmapToCache(bitmap: Bitmap, fileName: String, context: Context) {

            try {
                val file = File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "share_image_" + System.currentTimeMillis() + ".png"
                )
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun getBitmapUriFromCache(fileName: String, context: Context): Uri? {
            var bmpUri: Uri? = null
            val file = File(context.cacheDir, fileName)

            if(file.exists()) {
                bmpUri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
            }

            return bmpUri
        }
    }

    fun loadIntoImageView(url: String?, imageView: ImageView, context: Context) {

        Glide
            .with(context)
            .asBitmap()
            .load(Const.baseUrl + url)
            .into(object: CustomTarget<Bitmap>() {

                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
//                        MiddleLayerHelper.storeBitmapToCache(resource, path, context)
                    imageView.setImageBitmap(resource)
                }
            })
    }

}