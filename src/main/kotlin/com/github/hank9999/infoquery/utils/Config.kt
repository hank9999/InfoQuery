package com.github.hank9999.infoquery.utils

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
        var id: String? = null
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
            } else if (Bot.id == null || Bot.id!!.isEmpty()) {
                logger.error("配置文件错误: bot.id 不存在或为空")
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
            Bot.id = root.node("bot", "id").string
        }
    }

}