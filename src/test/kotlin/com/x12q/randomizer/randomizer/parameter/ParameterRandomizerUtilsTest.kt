package com.x12q.randomizer.randomizer.parameter

import com.x12q.randomizer.randomizer.RDClassData
import com.x12q.randomizer.randomizer.TestObjects
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import io.kotest.matchers.shouldBe
import kotlin.test.*

class ParameterRandomizerUtilsTest {


    @Test
    fun paramRandomizer() {
        fun condition(paramInfo: ParamInfo): Boolean {
            val clazzData = paramInfo.paramClass
            val kParam: KParameter = paramInfo.kParam
            val parentClass: RDClassData = paramInfo.parentClass
            return parentClass.kClass == TestObjects.Class1::class && kParam.name == "tm12"
        }

        fun ifApplicable(paramInfo: ParamInfo): String {
            return "${paramInfo.kParam.name}: random value 123"
        }

        val rdmizer = paramRandomizer<String>(
            condition = ::condition,
            generateRandomIfApplicable = ::ifApplicable
        )

        val abc = RDClassData.from<TestObjects.Class1>()
        val kParam = abc.kClass.primaryConstructor!!.parameters.first { it.name == "tm12" }

        rdmizer.isApplicableTo(
            parameterClassData = RDClassData.from<Int>(),
            parameter = kParam,
            parentClassData = RDClassData.from<TestObjects.Class1>()
        ) shouldBe condition(
            ParamInfo(
                paramClass = RDClassData.from<Int>(),
                kParam = kParam,
                parentClass = RDClassData.from<TestObjects.Class1>()
            )
        )

        rdmizer.random(
            parameterClassData = RDClassData.from<Int>(),
            parameter = kParam,
            parentClassData = RDClassData.from<TestObjects.Class1>()
        ) shouldBe ifApplicable(
            ParamInfo(
                paramClass = RDClassData.from<Int>(),
                kParam = kParam,
                parentClass = RDClassData.from<TestObjects.Class1>()
            )
        )
    }
}
