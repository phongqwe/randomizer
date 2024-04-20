package com.x12q.randomizer.randomizer.parameter

import com.x12q.randomizer.randomizer.RDClassData
import kotlin.reflect.KParameter

interface ParamInfo {
    val paramClassData: RDClassData
    val kParam: KParameter
    val parentClass: RDClassData
}
