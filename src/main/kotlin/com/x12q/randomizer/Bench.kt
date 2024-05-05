package com.x12q.randomizer

import com.x12q.randomizer.randomizer.RDClassData
import com.x12q.randomizer.randomizer.builder.paramRandomizers
import com.x12q.randomizer.randomizer.builder.randomizers
import com.x12q.randomizer.randomizer.clazz.classRandomizer
import com.x12q.randomizer.randomizer.param.AbsSameClassParamRandomizer
import com.x12q.randomizer.randomizer.param.paramRandomizer
import com.x12q.randomizer.randomizer.primitive.*
import kotlinx.serialization.Serializable
import kotlin.random.Random
import kotlin.reflect.KParameter


@Serializable
data class ABC(val lst: List<Float>, val tm12: Int)
data class A2(val t2: String)
data class ABC2(
    val abc: ABC,
    val str: String,
    val abc2: ABC,
    val str2: String,
    @Randomizable(A2Randomizer::class)
    val a2: A2
)


class A2Randomizer : AbsSameClassParamRandomizer<A2>() {
    override val paramClassData: RDClassData = RDClassData.from<A2>()

    override fun random(parameterClassData: RDClassData, parameter: KParameter, enclosingClassData: RDClassData): A2? {
        return A2("from custom randomizer")
    }
}

data class Q<T>(val t: T)

fun main() {
    println(random<ABC2>(
        randomizers = listOf(
            intRandomizer {
                99
            },
            floatRandomizer {
                1.0f
            },
            stringRandomizer {
                "abc123"
            },
            listRandomizer {
                listOf(1f, 2f)
            }
        ),
        paramRandomizers = listOf(
            stringParamRandomizer(
//                condition = {
//                    it.paramName == "t2" && it.parentIs<A2>()
//                },
                random = {
                    "${it.paramName}:__qwe__"
                }
            )
        )
    ))

    println(
        random<ABC2>(
            randomizers = randomizers {
                int {
                    99
                }
                float {
                    1f
                }
                string {
                    "abc123"
                }
                list {
                    listOf(1f, 2f)
                }
                add(classRandomizer {

                })
            },
            paramRandomizers = paramRandomizers {
//                add(paramRandomizer {
//                    OtherClass(123)
//                })
//                add(paramRandomizer(
//                    condition = {paramInfo ->
//                        paramInfo.paramName == "someParamName"
//                    },
//                    random= {
//                        OtherClass(456)
//                    }
//                ))
//                string { paramInfo->
//                    "${paramInfo.paramName} -- some str"
//                }
//                int(
//                    condition = { paramInfo->
//                        paramInfo.paramName="age" && paramInfo.enclosingKClass == Person::class
//                    },
//                    random= {
//                        Random.nextInt(1000)
//                    }
//                )
            }
        )
    )
}

