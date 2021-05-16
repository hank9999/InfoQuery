package com.github.hank9999.infoquery.kaiheila.types

data class RoleList(
    val data: Data
) {
    data class Data(
        val items: List<R>
    ) {
        data class R(
            val role_id: Int,
            val name: String
        )
    }
}
