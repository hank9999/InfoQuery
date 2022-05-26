package com.github.hank9999.infoquery.json.types

import kotlin.reflect.KClass

data class T(
    val int: KClass<Int> = Int::class,
    val string: KClass<String> = String::class,
    val bool: KClass<Boolean> = Boolean::class,
    val long: KClass<Long> = Long::class
)
