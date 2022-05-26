package com.github.hank9999.infoquery.bot

import com.github.hank9999.infoquery.bot.types.User
import com.github.hank9999.infoquery.http.HttpApi

class Bot {
    companion object {
        var me = User()
        fun fetchMe() {
            me = HttpApi.User.me()
        }
    }
}