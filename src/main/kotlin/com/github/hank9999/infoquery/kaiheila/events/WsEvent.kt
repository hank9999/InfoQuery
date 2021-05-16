package com.github.hank9999.infoquery.kaiheila.events

import com.github.hank9999.infoquery.kaiheila.types.WsSignalling
import com.github.hank9999.infoquery.kaiheila.KaiheilaWs
import com.github.hank9999.infoquery.kaiheila.types.WsStatus
import com.github.hank9999.infoquery.kaiheila.utils.MessageHandler
import com.github.hank9999.infoquery.kaiheila.utils.WsTimer
import com.github.hank9999.infoquery.utils.Json
import java.util.*
import kotlin.system.exitProcess

class WsEvent {

    private fun receiveHelloMessage(wsText: WsSignalling) {
        if (wsText.d!!.code!! == 0) {
            KaiheilaWs.logger.info("已收到开黑啦的Hello消息, 连接成功创立")
            KaiheilaWs.status = WsStatus.Connected
            Timer().schedule(WsTimer.Ping(), Date(), 30 * 1000)
        } else {
            KaiheilaWs.status = WsStatus.NotConnected
            KaiheilaWs.logger.error("开黑啦Hello错误")
            if (wsText.d.code!! == 40103) {
                KaiheilaWs.logger.error("正在尝试重新连接")
                KaiheilaWs.connect()
            } else {
                exitProcess(1)
            }
        }
    }

    private fun receiveMessage(wsText: WsSignalling, text: String) {
        if (wsText.sn!! <= KaiheilaWs.sn) {
            return
        }
        if (KaiheilaWs.status == WsStatus.Connected) {
            KaiheilaWs.recvQueue[wsText.sn] = text
        }
        while (true) {
            if (KaiheilaWs.recvQueue.contains(KaiheilaWs.sn + 1)) {
                val context = KaiheilaWs.recvQueue[KaiheilaWs.sn + 1]
                KaiheilaWs.recvQueue.remove(KaiheilaWs.sn + 1)
                KaiheilaWs.sn += 1
                MessageHandler().onEvent(context!!)
            } else {
                break
            }
        }
    }

    fun handleEvent(text: String) {
        val wsText = Json.deserialize(text, WsSignalling::class)
        if (wsText == null) {
            KaiheilaWs.logger.error("解析开黑啦消息失败")
            return
        }

        when (wsText.s) {
            1 -> receiveHelloMessage(wsText)
            0 -> receiveMessage(wsText, text)
            3 -> KaiheilaWs.pongTime = System.currentTimeMillis()
            5 -> KaiheilaWs.connect()
            6 -> KaiheilaWs.logger.info("已收到开黑啦所有离线消息")
            else -> KaiheilaWs.logger.error("暂不支持处理该消息类型 {}", text)
        }
    }
}