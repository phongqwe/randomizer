package com.x12q.randomizer.randomizer.class_randomizer

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class BaseRandomizerFactory @Inject constructor(
    val random: Random
) {
    val intRandomizer: ClassRandomizer<Int> by lazy {
        randomizer {
            random.nextInt()
        }
    }


    val baseRandomizers:List<ClassRandomizer<*>> = emptyList()
}
