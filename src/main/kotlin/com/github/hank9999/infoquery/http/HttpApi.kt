package com.github.hank9999.infoquery.http

import com.github.hank9999.infoquery.Config
import com.github.hank9999.infoquery.bot.types.types.MessageTypes
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
    object Channel {
        fun create(content: String, target_id: String, type: MessageTypes = MessageTypes.TEXT, quote: String = "", temp_target_id: String = ""): JsonElement {
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
            val resp = Http.post("$api/message/create", authHeader, formData.build())
            val respJson = json.parseToJsonElement(resp.body)
            if (respJson["code"](t.int) != 0) {
                logger.error("HttpApi ERROR ${respJson["code"](t.int)} message/create ${respJson["message"](t.string)}")
                return json.parseToJsonElement("{}")
            }
            return respJson["data"]
        }
    }

    object User {
        fun me(): com.github.hank9999.infoquery.bot.types.User {
            val resp = Http.get("$api/user/me", authHeader)
            val respJson = json.parseToJsonElement(resp.body)
            if (respJson["code"](t.int) != 0) {
                logger.error("HttpApi ERROR ${respJson["code"](t.int)} message/create ${respJson["message"](t.string)}")
                return com.github.hank9999.infoquery.bot.types.User()
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