package com.github.hank9999.infoquery.kaiheila

sealed class Hardcore {
    sealed class Api {
        sealed class Websocket {
            companion object {
                const val gatewayUrl: String = "https://www.kaiheila.cn/api/v3/gateway/index?compress=0"
            }
        }
        sealed class Message {
            companion object {
                const val creareUrl: String = "https://www.kaiheila.cn/api/v3/message/create"
            }
        }
        sealed class Role {
            companion object {
                const val guildList: String = "https://www.kaiheila.cn/api/v3/guild-role/list"
            }
        }
    }
}