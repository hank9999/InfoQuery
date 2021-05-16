package com.github.hank9999.infoquery.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.reflect.KClass

class Json {
    companion object {
        fun <T : Any> deserialize(json: String, type: KClass<T>): T? {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val jsonAdapter = moshi.adapter(type.java)
            return jsonAdapter.fromJson(json)
        }

        inline fun <reified T : Any> serialize(data: T): String? {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val jsonAdapter = moshi.adapter(T::class.java)
            return jsonAdapter.toJson(data)
        }
    }
}