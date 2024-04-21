package com.x12q.randomizer.test

import com.x12q.randomizer.randomizer.RDClassData
import com.x12q.randomizer.randomizer.class_randomizer.randomizer
import com.x12q.randomizer.randomizer.parameter.paramRandomizer
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

object TestSamples {

    val comp = DaggerTestComp.create()

    data class Class1(val lst: List<Float>, val tm12: String) {


        companion object {
            val dt = RDClassData.from<Class1>()
            val tm12FixedRandomizer = paramRandomizer<String>(
                condition = { paramInfo ->
                    val clazzData = paramInfo.paramClass
                    val kParam: KParameter = paramInfo.kParam
                    val parentClass: RDClassData = paramInfo.parentClass
                    parentClass.kClass == Class1::class && kParam.name == "tm12"
                },
                makeRandomIfApplicable = { paramInfo ->
                    "${paramInfo.kParam.name}: random value 123}"
                },
            )
            val tm12KParam by lazy {
                Class1::class.primaryConstructor!!.parameters.first { it.name == "tm12" }
            }
        }
    }

    data class Class2(val a: Class1, val t: String) {
        companion object {
            val dt = RDClassData.from<Class2>()
            val classFixedRandomizer = randomizer(
                condition = {
                    it.kClass == Class2::class
                },
                makeRandomIfApplicable = {
                    Class2(
                        a = Class1(listOf(1.1f, 2.2f), tm12 = ""),
                        t = ""
                    )
                }
            )
        }
    }

    data class ClassWithGeneric<T>(val t: T)
}