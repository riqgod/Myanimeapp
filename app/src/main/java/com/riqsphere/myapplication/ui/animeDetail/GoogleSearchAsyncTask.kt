package com.riqsphere.myapplication.ui.animeDetail

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import com.riqsphere.myapplication.utils.ImageHandler
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class GoogleSearchAsyncTask(private val imageBg: ImageView, private val context: Context) : AsyncTask<String, Int, String>() {
    @Override
    override fun doInBackground(vararg strings: String?): String? {

        var animeSearch = strings[0]!!
        animeSearch =  animeSearch.replace(" ", "+")

        val urlString = "https://www.googleapis.com/customsearch/v1?q=$animeSearch&cx=${keyCse}&imgSize=huge&num=1&searchType=image&key=${apiKey}"

        var url:URL? = null
        try {
            url = URL(urlString)
        } catch (e: MalformedURLException) {
        }
        val tag = "google api cse"
        Log.d(tag, "AsyncTask - doInBackground, url=$url")

        // Http connection
        var conn: HttpURLConnection? = null
        try {
            conn = url!!.openConnection() as HttpURLConnection?
        } catch (e: IOException) {
            Log.e(tag, "Http connection ERROR $e")
        }
        var responseCode = 0
        var responseMessage = ""
        try {
            responseCode = conn!!.responseCode
            responseMessage = conn.responseMessage
        } catch (e: IOException) {
            Log.e(tag, "Http getting response code ERROR $e")
        }

        Log.d(tag, "Http response code =$responseCode message=$responseMessage")

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
                val errorMsg =
                    "Http ERROR response $responseMessage\nMake sure to replace in code your own Google API key and Search Engine ID"
                Log.e(tag, errorMsg)
                return errorMsg
            }
        } catch (e: IOException) {
            Log.e(tag, "Http Response ERROR $e")
        }
        conn?.disconnect()


        return null
    }

    override fun onPostExecute(result:String) {
        val result2 = parseJSon(result)
        Log.d("image url download",result2)
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
    }
}