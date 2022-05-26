package com.github.hank9999.infoquery


import com.github.hank9999.infoquery.bot.Bot
import com.github.hank9999.infoquery.bot.WebHook
import org.fusesource.jansi.AnsiConsole
import kotlin.system.exitProcess

object InfoQuery {

    @JvmStatic
    fun main(args: Array<String>) {
        AnsiConsole.systemInstall()
        if (!Config.checkExists()) {
            println("未找到配置文件")
            println("已生成配置文件，请配置后再启动程序")
            exitProcess(1)
        }
        Config.setValue()
        Config.checkConfig()
        Bot.fetchMe()
        WebHook().initialize(Config.Bot.host!!, Config.Bot.port!!, Config.Bot.path!!)
    }
}