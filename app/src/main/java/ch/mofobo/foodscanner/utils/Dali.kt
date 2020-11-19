package ch.mofobo.foodscanner.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
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

            val url = urls[0]
            var bitmap: Bitmap? = null
            try {
                val inputStream: InputStream = URL(url).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.message?.let { Log.e("Error", it) }
                e.printStackTrace()
            }
            return bitmap
        }
    }

    companion object {
        fun get(): Dali {
            return Dali()
        }
    }
}