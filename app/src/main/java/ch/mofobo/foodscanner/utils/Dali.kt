package ch.mofobo.foodscanner.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import ch.mofobo.foodscanner.R
import java.io.InputStream
import java.net.URL


class Dali {

    fun load(url: String): DaliCreator {
        return DaliCreator(url)
    }

    class DaliCreator(private val url: String) {
        fun into(imageView: ImageView) {
            DownloadImageAsyncTask(imageView).execute(url)
        }
    }

    class DownloadImageAsyncTask(private var imageView: ImageView) : AsyncTask<String?, Void?, Bitmap?>() {

        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }

        override fun doInBackground(vararg urls: String?): Bitmap? {

            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val inputStream: InputStream = URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.message?.let { Log.e("Error", it) }
                e.printStackTrace()
            }
            return mIcon11
        }
    }

    companion object {
        fun get(): Dali {
            return Dali()
        }
    }
}