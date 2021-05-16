package com.github.hank9999.infoquery.utils

import com.github.hank9999.infoquery.types.HttpResponse
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class Http {
    companion object {
        fun get(url: String, headers: Map<String, String>): HttpResponse {
            val client = OkHttpClient()
            val builder = Request.Builder().url(url)
            for (item in headers.entries) {
                builder.addHeader(item.key, item.value)
            }
            val request = builder.build()

            client.newCall(request).execute().use { response ->
                val h = mutableMapOf<String, String>()
                for ((name, value) in response.headers) {
                    h[name] = value
                }

                return HttpResponse(response.code, response.body!!.string(), h)
            }
        }

        fun get(url: String, headers: Map<String, String>, params: Map<String, String>): HttpResponse {
            val client = OkHttpClient()
            val url2 = url.toHttpUrlOrNull()!!
            val httpUrl = HttpUrl.Builder()
                .scheme(url2.scheme)
                .host(url2.host)
            for (item in url2.pathSegments) {
                httpUrl.addPathSegment(item)
            }
            for (item in params.entries) {
                httpUrl.addQueryParameter(item.key, item.value)
            }
            val builder = Request.Builder().url(httpUrl.toString())
            for (item in headers.entries) {
                builder.addHeader(item.key, item.value)
            }

            val request = builder.build()

            client.newCall(request).execute().use { response ->
                val h = mutableMapOf<String, String>()
                for ((name, value) in response.headers) {
                    h[name] = value
                }

                return HttpResponse(response.code, response.body!!.string(), h)
            }
        }

        fun post(url: String, headers: Map<String, String>, body: String, type: String) {
            val client = OkHttpClient()
            val builder = Request.Builder().url(url).post(body.toRequestBody(type.toMediaType()))
            for (item in headers.entries) {
                builder.addHeader(item.key, item.value)
            }
            val request = builder.build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
            }
        }
    }


//    fun getAsynchronous(url: String, headers: Map<String, String>) {
//        val client = OkHttpClient()
//        val builder = Request.Builder().url(url)
//        for (item in headers.entries) {
//            builder.addHeader(item.key, item.value)
//        }
//        val request = builder.build()
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                response.use {}
//            }
//        })
//    }
}