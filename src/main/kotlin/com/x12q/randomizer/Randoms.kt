package com.x12q.randomizer

import com.x12q.randomizer.di.DaggerRandomizerComponent
import com.x12q.randomizer.randomizer.ClassRandomizer
import com.x12q.randomizer.randomizer.ParameterRandomizer
import com.x12q.randomizer.randomizer.builder.ParamRandomizerListBuilder
import com.x12q.randomizer.randomizer.builder.RandomizerListBuilder
import com.x12q.randomizer.randomizer.config.RandomizerConfig
import com.x12q.randomizer.randomizer.context.RandomizerCollectionBuilder
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

inline fun <reified T> randomWithBuilder(
    random: Random = Random,
    randomizers: RandomizerListBuilder = RandomizerListBuilder(),
    paramRandomizers: ParamRandomizerListBuilder = ParamRandomizerListBuilder(),
    defaultRandomConfig: RandomizerConfig = RandomizerConfig.default,
    collectionBuilder: RandomizerCollectionBuilder? = null,
): T {

    val c = collectionBuilder?:RandomizerCollectionBuilder()
    randomizers.addCtx(c)

    return random(
        random = random,
        randomizers = randomizers.build(),
        paramRandomizers = paramRandomizers.build(),
        defaultRandomConfig = defaultRandomConfig,
        collectionBuilder = collectionBuilder
    )
}
/**
 * Make a random instance of [T] with the option to specify [randomizers] and [paramRandomizers] that
 * can override default random logic.
 */
inline fun <reified T> random(
    random: Random = Random,
    randomizers: Collection<ClassRandomizer<*>> = emptyList(),
    paramRandomizers: Collection<ParameterRandomizer<*>> = emptyList(),
    defaultRandomConfig: RandomizerConfig = RandomizerConfig.default,
    collectionBuilder: RandomizerCollectionBuilder? = null,
): T {

    val cb = collectionBuilder ?: RandomizerCollectionBuilder()

    val comp = DaggerRandomizerComponent.builder()
        .setRandom(random)
        .build()

    val baseRandomizer = comp.randomizer()

    cb.addClassRandomizers(randomizers)
        .addParamRandomizer(paramRandomizers)

    val collection = runBlocking {
        val col = cb.awaitComplete()
        val col0 = baseRandomizer.lv1RandomizerCollection
        col0.mergeWith(col)
    }

    val randomizer = baseRandomizer.copy(
        lv1RandomizerCollection = collection,
        defaultRandomConfig = defaultRandomConfig,
    )

    val clzzData = RDClassData.from<T>()
    return randomizer.random(clzzData) as T
}

/**
 * Make a random instance of [T] with the option to specify [randomizers] and [paramRandomizers] that
 * can override default random logic.
 */
//inline fun <reified T> random(
//    random: Random = Random,
//    randomizers: Collection<ClassRandomizer<*>> = emptyList(),
//    paramRandomizers: Collection<ParameterRandomizer<*>> = emptyList(),
//    defaultRandomConfig: RandomizerConfig = RandomizerConfig.default
//): T {
//
//    val comp = DaggerRandomizerComponent.builder()
//        .setRandom(random)
//        .build()
//
//    val randomizer = comp.randomizer().let {rdm->
//        rdm.copy(
//            lv1RandomizerCollection = rdm
//                .lv1RandomizerCollection
//                .addParamRandomizer(paramRandomizers)
//                .addRandomizers(randomizers),
//            defaultRandomConfig = defaultRandomConfig,
//        )
//    }
//
//    val clzzData = RDClassData.from<T>()
//    return randomizer.random(clzzData) as T
//}

/**
 * Make a random instance of an inner class [T] within [enclosingObject].
 */
inline fun <reified T : Any> randomInnerClass(
    enclosingObject: Any,
    random: Random = Random,
    randomizers: Collection<ClassRandomizer<*>> = emptyList(),
    paramRandomizers: Collection<ParameterRandomizer<*>> = emptyList(),
    defaultRandomConfig: RandomizerConfig = RandomizerConfig.default
): T {
    val comp = DaggerRandomizerComponent.builder()
        .setRandom(random)
        .build()

    val randomizer = comp.randomizer().let {
        it.copy(
            lv1RandomizerCollection = it.lv1RandomizerCollection
                .addParamRandomizer(paramRandomizers)
                .addRandomizers(randomizers),
            defaultRandomConfig = defaultRandomConfig,
        )
    }
    val clzzData = RDClassData.from<T>()
    return randomizer.randomInnerClass(clzzData, enclosingObject) as T
}

