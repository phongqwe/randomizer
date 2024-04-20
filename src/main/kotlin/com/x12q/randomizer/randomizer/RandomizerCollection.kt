package com.x12q.randomizer.randomizer

import com.x12q.randomizer.randomizer.class_randomizer.ClassRandomizer
import com.x12q.randomizer.randomizer.parameter.ParameterRandomizer

/**
 * Simply a collection of [ClassRandomizer] and [ParameterRandomizer]
 */
data class RandomizerCollection(
    val parameterRandomizers: Map<RDClassData, List<ParameterRandomizer<*>>>,
    val randomizers: Map<RDClassData, ClassRandomizer<*>>
) {


    fun addParamRandomizer(randomizer: Collection<ParameterRandomizer<*>>): RandomizerCollection {
        TODO("Not yet implemented")
    }

    fun getParamRandomizer(key: RDClassData): List<ParameterRandomizer<*>> {
        TODO("Not yet implemented")
    }


    fun addRandomizers(randomizers: Collection<ClassRandomizer<*>>): RandomizerCollection {
        TODO("Not yet implemented")
    }

    fun getRandomizer(key: RDClassData): ClassRandomizer<*>? {
        TODO("Not yet implemented")
    }

}
