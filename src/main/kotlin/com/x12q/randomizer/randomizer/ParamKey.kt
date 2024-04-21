package com.x12q.randomizer.randomizer

import kotlin.reflect.KParameter

data class ParamKey(
    val classData: RDClassData,
    val kParameter: KParameter,
)
