package com.x12q.randomizer

import kotlin.reflect.KClass
import kotlin.reflect.KTypeParameter
import kotlin.reflect.full.primaryConstructor




data class Q1<K, V>(val l: Map<K, V>)
data class Q2<T>(val l: List<T>)
data class Q3<T>(val q2: Q2<T>, val l2: List<T>)
data class Q4<T>(val q3: Q3<T>)
data class A(val d: Double, val str: String)
data class Q5<E>(val q1: Q1<Int, E>)


data class Inner0<I0_1, I0_2>(
    val t1: I0_1,
    val t2: I0_2
)

data class Inner1<I1_1, I1_2, I1_3>(
    val inner0: Inner0<I1_2, I1_3>,
)

data class Q6<Q6_1, Q6_2>(
    val l: Inner1<Q6_1, Double, Q6_2>
)

fun main() {

    val q6 = RDClassData.from<Q6<Int, String>>()
    val q6ProvideMap = q6.makeCompositeDeclaredTypeMap(emptyMap())

    q6.kClass.primaryConstructor!!.parameters.forEach { inner1Param ->

        /**
         * Will this work?
         * => This will work because:
         * Each parameter can use the information from its enclosing class (enclosure) to construct a full map (with index) of generic - concrete type that it can use to query later.
         * Whatever parameter cannot get from enclosure, it can get from within itself.
         *
         * This process can be repeated for deeper parameter, each only need to construct 1 map from its enclosure's data.
         * Remember, each mapping must only the information from the immediate enclosure.
         */

        val inner1Class = inner1Param.type.classifier as KClass<*>
        val inner1RD = RDClassData(inner1Class, inner1Param.type)
        val inner1TypeMap: Map<String, RDClassData> = inner1RD.makeCompositeDeclaredTypeMap(q6ProvideMap)

        inner1Class.primaryConstructor!!.parameters.map { inner0 ->

            val inner0Class = inner0.type.classifier as KClass<*>
            val inner0RD = RDClassData(inner0Class, inner0.type)
            val inner0FullProvideMap = inner0RD.makeCompositeDeclaredTypeMap(inner1TypeMap)
            val index = inner0.index
            val inner0Classifier = inner0.type.classifier

            when (inner0Classifier) {
                is KClass<*> -> {
                    inner0Classifier.primaryConstructor!!.parameters.map { paramOfInner0 ->
                        val paramOfInner0 = paramOfInner0.type.classifier
                        when (paramOfInner0) {
                            is KTypeParameter -> {
                                val rdDataFromInner1 = inner0FullProvideMap[paramOfInner0.name]
                                println("+++++ rdDataFromInner1: ${paramOfInner0.name} :${rdDataFromInner1}")
                            }
                        }
                    }
                    println("")
                }

                is KTypeParameter -> {
                    // lookup type from the outer type map
                    // lookup type from within the parameter
                    val type = inner1Param.type.arguments[index].type!!
                    val c = type.classifier as KClass<*>
                    val rd = RDClassData(c, type)
                    println("inside: ${rd}")
                }
            }
        }
    }
}


//
//@Serializable
//data class ABC(val lst: List<Float>, val tm12: Int)
//
//data class A2(val t2: String) {
//    companion object {
//        class A2Randomizer : AbsSameClassParamRandomizer<A2>() {
//            override val paramClassData: RDClassData = RDClassData.from<A2>()
//
//            override fun random(
//                parameterClassData: RDClassData,
//                parameter: KParameter,
//                enclosingClassData: RDClassData
//            ): A2? {
//                return A2("from custom randomizer")
//            }
//        }
//    }
//}
//
//data class ABC2(
//    val abc: ABC,
//    val str: String,
//    val abc2: ABC,
//    val str2: String,
//    @Randomizable(A2Randomizer::class)
//    val a2: A2
//)
//
//data class A3(val i: Int, val str: String) {
//
//    @Randomizable(A3.Companion.A3Randomizer::class)
//    constructor(f: Float) : this(f.toInt(), "pppp")
//
//    companion object {
//        class A3Randomizer : AbsSameClassRandomizer<A3>() {
//            override val returnedInstanceData: RDClassData = RDClassData.from<A3>()
//
//            override fun random(): A3 {
//                return A3(1, "-")
//            }
//        }
//    }
//}
//
//class A2Randomizer : AbsSameClassParamRandomizer<A2>() {
//    override val paramClassData: RDClassData = RDClassData.from<A2>()
//
//    override fun random(parameterClassData: RDClassData, parameter: KParameter, enclosingClassData: RDClassData): A2? {
//        return A2("from custom randomizer")
//    }
//}
//
//
//class RD1<T1>(val t1: T1)
//
//class RD2<T2_1, T2_2, T2_3>(
//    val rd1: RD1<T2_2>,
//    val d: T2_1,
//    val x: T2_3,
//)
//
//class RD3<T3_1, T3_2, T3_3, T3_4>(
//    val rd2: RD2<T3_1, T3_2, T3_4>,
//    val c: T3_3,
//)
//
//val rd3 = RDClassData.from<RD3<Double, Short, Float, Int>>()
//
//
//fun main() {
//
//
//
//
//
//
//
////    RDClassData.from<Q3<Int>>().also {
////        println(it.kClass.typeParameters)
////        println(it.kType?.arguments)
////    }
//
////    println(List::class.isSubclassOf(Iterable::class))
////    println(Iterable::class.isSuperclassOf(List::class))
////    println(List::class.isSuperclassOf(List::class))
////    println(random<ABC2>(
////        randomizers = listOf(
////            intRandomizer {
////                99
////            },
////            floatRandomizer {
////                1.0f
////            },
////            stringRandomizer {
////                "abc123"
////            },
////            listRandomizer {
////                listOf(1f, 2f)
////            }
////        ),
////        paramRandomizers = listOf(
////            stringParamRandomizer(
//////                condition = {
//////                    it.paramName == "t2" && it.parentIs<A2>()
//////                },
////                random = {
////                    "${it.paramName}:__qwe__"
////                }
////            )
////        )
////    ))
////
////    println(
////        random<ABC2>(
////            randomizers = randomizers {
////                int {
////                    99
////                }
////                float {
////                    1f
////                }
////                string {
////                    "abc123"
////                }
////                list {
////                    listOf(1f, 2f)
////                }
////                add(classRandomizer {
////
////                })
////            },
////            paramRandomizers = paramRandomizers {
//////                add(paramRandomizer {
//////                    OtherClass(123)
//////                })
//////                add(paramRandomizer(
//////                    condition = {paramInfo ->
//////                        paramInfo.paramName == "someParamName"
//////                    },
//////                    random= {
//////                        OtherClass(456)
//////                    }
//////                ))
//////                string { paramInfo->
//////                    "${paramInfo.paramName} -- some str"
//////                }
//////                int(
//////                    condition = { paramInfo->
//////                        paramInfo.paramName="age" && paramInfo.enclosingKClass == Person::class
//////                    },
//////                    random= {
//////                        Random.nextInt(1000)
//////                    }
//////                )
////            }
////        )
////    )
//}
//