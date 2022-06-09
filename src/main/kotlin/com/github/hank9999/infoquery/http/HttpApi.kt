package com.github.hank9999.infoquery.http

import com.github.hank9999.infoquery.Config
import com.github.hank9999.infoquery.bot.types.types.MessageTypes
import com.github.hank9999.infoquery.http.exceptions.HttpException
import com.github.hank9999.infoquery.json.JSON.Companion.json
import com.github.hank9999.infoquery.json.JSON.Companion.t
import com.github.hank9999.infoquery.json.JSON.Operator.get
import com.github.hank9999.infoquery.json.JSON.Operator.invoke
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import okhttp3.FormBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class HttpApi {
    object Message {
        fun create(content: String, target_id: String, type: MessageTypes = MessageTypes.TEXT, quote: String = "", temp_target_id: String = ""): JsonElement {
            val bucket = "message/create"
            val route = "message/create"
            val sleepTime = RateLimit.getSleepTime(bucket)
            Thread.sleep(sleepTime)
            val formData = FormBody.Builder()
                .add("content", content)
                .add("target_id", target_id)
            if (type != MessageTypes.TEXT) {
                formData.add("type", type.type.toString())
            }
            if (quote.isNotEmpty()) {
                formData.add("quote", quote)
            }
            if (temp_target_id.isNotEmpty()) {
                formData.add("temp_target_id", temp_target_id)
            }
            val resp = Http.post("$api/$route", authHeader, formData.build())
            val respJson = json.parseToJsonElement(resp.body)
            if (respJson["code"](t.int) != 0) {
                throw HttpException("HttpApi ERROR ${respJson["code"](t.int)} $route ${respJson["message"](t.string)}")
            }
            if (resp.headers.containsKey("x-rate-limit-limit")) {
                RateLimit.updateRateLimitInfo(
                    bucket,
                    resp.headers["x-rate-limit-limit"]!![0].toInt(),
                    resp.headers["x-rate-limit-remaining"]!![0].toInt(),
                    resp.headers["x-rate-limit-reset"]!![0].toInt()
                )
            }
            return respJson["data"]
        }
    }

    object User {
        fun me(): com.github.hank9999.infoquery.bot.types.User {
            val bucket = "user/me"
            val route = "user/me"
            val sleepTime = RateLimit.getSleepTime(bucket)
            Thread.sleep(sleepTime)
            val resp = Http.get("$api/$route", authHeader)
            val respJson = json.parseToJsonElement(resp.body)
            if (respJson["code"](t.int) != 0) {
                throw HttpException("HttpApi ERROR ${respJson["code"](t.int)} $route ${respJson["message"](t.string)}")
            }
            if (resp.headers.containsKey("x-rate-limit-limit")) {
                RateLimit.updateRateLimitInfo(
                    bucket,
                    resp.headers["x-rate-limit-limit"]!![0].toInt(),
                    resp.headers["x-rate-limit-remaining"]!![0].toInt(),
                    resp.headers["x-rate-limit-reset"]!![0].toInt()
                )
            }
            return json.decodeFromJsonElement(respJson["data"])
        }
    }

    companion object {
        const val api = "https://www.kaiheila.cn/api/v3"
        val authHeader = mapOf("Authorization" to "Bot ${Config.Bot.token}")
        private val logger: Logger = LoggerFactory.getLogger(HttpApi::class.java)
    }
}