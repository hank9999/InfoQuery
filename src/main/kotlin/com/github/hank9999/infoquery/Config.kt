package com.github.hank9999.infoquery

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess


class Config {

    object Bot {
        var token: String? = null
        var verify_token: String? = null
        var host: String? = "localhost"
        var port: Int? = 3000
        var path: String? = "/webhook"
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(Config::class.java)
        private const val configFile = "config.conf"

        fun checkExists(): Boolean {
            val file = File(configFile)
            if (file.exists()) {
                return true
            } else {
                val inputStream: InputStream = Config::class.java.getResourceAsStream("/$configFile")!!
                try {
                    Files.copy(inputStream, Paths.get(configFile))
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    logger.error("配置文件错误: 复制配置文件时未找到程序内文件")
                    exitProcess(1)
                } catch (e: IOException) {
                    e.printStackTrace()
                    logger.error("配置文件错误: 复制配置文件时IO错误")
                    exitProcess(1)
                }
            }
            return false
        }

        private fun checkBotConfig(): Boolean {
            if (Bot.token == null || Bot.token!!.isEmpty()) {
                logger.error("配置文件错误: bot.token 不存在或为空")
                return false
            } else if (Bot.verify_token == null || Bot.verify_token!!.isEmpty()) {
                logger.error("配置文件错误: bot.verify_token 不存在或为空")
                return false
            } else if (Bot.host == null || Bot.host!!.isEmpty()) {
                logger.error("配置文件错误: bot.host 不存在或为空")
                return false
            } else if (Bot.port == null || (Bot.port!! < 0 || Bot.port!! > 65535)) {
                logger.error("配置文件错误: bot.port 不存在或不合法")
                return false
            } else if (Bot.path == null || Bot.path!!.isEmpty()) {
                logger.error("配置文件错误: bot.path 不存在或为空")
                return false
            }
            return true
        }


        fun checkConfig() {
            if (!checkBotConfig()) {
                exitProcess(1)
            }
        }

        fun setValue() {
            val loader = HoconConfigurationLoader.builder()
                .path(Paths.get(configFile))
                .build()
            val root: CommentedConfigurationNode
            try {
                root = loader.load()
            } catch (e: IOException) {
                logger.error("加载配置文件时发生错误: " + e.message)
                if (e.cause != null) {
                    e.cause!!.printStackTrace()
                }
                exitProcess(1)
            }

            Bot.token = root.node("bot", "token").string
            Bot.verify_token = root.node("bot", "verify_token").string
            Bot.host = root.node("bot", "host").string
            Bot.port = root.node("bot", "port").int
            Bot.path = root.node("bot", "path").string
        }
    }

}