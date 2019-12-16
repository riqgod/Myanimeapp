package com.riqsphere.myapplication.ui.animeDetail

import android.content.Context
import android.os.AsyncTask
import android.widget.ImageView
import com.riqsphere.myapplication.utils.ImageHandler
import com.riqsphere.myapplication.utils.NetworkState
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class GoogleSearchAsyncTask(private val imageBg: ImageView, private val context: Context) : AsyncTask<String, Int, String>() {
    @Override
    override fun doInBackground(vararg strings: String?): String? {
        if (!NetworkState.shouldLoad()) {
            return null
        }

        var animeSearch = strings[0]!!
        animeSearch =  animeSearch.replace(" ", "+")

        val urlString =
            "https://www.googleapis.com/customsearch/v1?q=$animeSearch&cx=$dale$ahn&imgSize=huge&num=1&searchType=image&key=$foin$q$segredo"

        var url:URL? = null
        try {
            url = URL(urlString)
        } catch (e: MalformedURLException) {
        }
        val tag = "google api cse"

        // Http connection
        var conn: HttpURLConnection? = null
        try {
            conn = url!!.openConnection() as HttpURLConnection?
        } catch (e: IOException) {
        }
        var responseCode = 0
        var responseMessage = ""
        try {
            responseCode = conn!!.responseCode
            responseMessage = conn.responseMessage
        } catch (e: IOException) {
        }


        try {

            if(responseCode == 200) {
                val rd = BufferedReader(InputStreamReader(conn!!.inputStream))
                val sb: StringBuilder = StringBuilder()
                var line:String? = rd.readLine()

                while ( line != null) {
                    sb.append(line + "\n")
                    line = rd.readLine()
                }

                rd.close()

                return sb.toString()

            } else {
                return "Http ERROR response $responseMessage\nMake sure to replace in code your own Google API key and Search Engine ID"
            }
        } catch (e: IOException) {
        }
        conn?.disconnect()


        return null
    }

    override fun onPostExecute(result: String) {
        if (!NetworkState.shouldLoad()) {
            return
        }
        val result2 = parseJSon(result)
        ImageHandler.getInstance(context).load(result2).into(imageBg)
    }

    private fun parseJSon(string:String):String{
        var processedString = string.substringAfter("items")
        processedString = processedString.substringAfter("\"link\": \"")
        processedString = processedString.substringBefore("\"")
        processedString.substringBefore("\"")
        return processedString
    }

    companion object {
        private const val apiKey = "AIzaSyCDygTKwzesUMdO0QNbnGWbNGJ7PM9Pnpc"
        private const val keyCse = "013948103287608180558:zyl5sbvqbkk"

        private const val segredo = "SmNkssGijWOK4"
        private const val q = "h1c6L4PR77BLIFUv"
        private const val foin = "AIzaSyAcr"
        private const val dale = "01394810328760818055"
        private const val ahn= "8:qacwtjfwy55"
    }
}