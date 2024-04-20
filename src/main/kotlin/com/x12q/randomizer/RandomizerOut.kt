package com.x12q.randomizer

import com.x12q.randomizer.di.DaggerRDComponent
import com.x12q.randomizer.randomizer.RDClassData
import com.x12q.randomizer.randomizer.class_randomizer.ClassRandomizer
import com.x12q.randomizer.randomizer.parameter.ParameterRandomizer
import com.x12q.randomizer.randomizer.parameter.paramRandomizer
import com.x12q.randomizer.randomizer.class_randomizer.randomizer
import kotlin.random.Random
import kotlin.reflect.KParameter


inline fun <reified T : Any> makeRandomInstance(
    random: Random = Random,
): T {
    val comp = DaggerRDComponent.builder()
        .setRandom(random)
        .build()
    val randomizer = comp.randomizer()
    // generate random
    val q = RDClassData.from<T>()
    return randomizer.random(q) as T
}

inline fun <reified T : Any> makeRandomInstance(
    random: Random = Random,
    randomizers: Collection<ClassRandomizer<*>>,
    paramRandomizers: Collection<ParameterRandomizer<*>>,
): T {
    val comp = DaggerRDComponent.builder()
        .setRandom(random)
        .build()

    val randomizer = comp.randomizer()

    val randomizer2 = randomizer.copy(
        randomizerCollection = randomizer.randomizerCollection
            .addParamRandomizer(paramRandomizers)
            .addRandomizers(randomizers)
    )

    // generate random
    val q = RDClassData.from<T>()
    return randomizer2.random(q) as T
}


data class ABC(val lst:List<Float>)
data class ABC2(val a: ABC, val t:String)
data class Q<T>(val t:T)

fun main(){
    val comp = DaggerRDComponent.builder().build()
    println(comp.random().nextInt())
//    val abc = makeRandomInstance<ABC>()
//    println(abc)
//    val q=
//        makeRandomInstance<Q<Int>>()
//    println(q)
//
//    println(makeRandomInstance<ABC2>())
    makeRandomInstance<ABC>(
        randomizers = listOf(
            randomizer<Int> {
                123
            },
        ),
        paramRandomizers = listOf(
            paramRandomizer<Int> { paramInfo ->
                val clazzData = paramInfo.paramClassData
                val kParam: KParameter = paramInfo.kParam
                val parentClass: RDClassData = paramInfo.parentClass
                TODO()
            },

            )
    )

}

