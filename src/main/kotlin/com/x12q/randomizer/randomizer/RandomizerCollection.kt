package com.x12q.randomizer.randomizer

import com.x12q.randomizer.RDClassData
import javax.inject.Inject
import kotlin.random.Random
import kotlin.reflect.full.isSubclassOf

/**
 * A collection of [ClassRandomizer] and [ParameterRandomizer]
 */
data class RandomizerCollection(
    val parameterRandomizers: Map<RDClassData, List<ParameterRandomizer<*>>>,
    val classRandomizers: Map<RDClassData, List<ClassRandomizer<*>>>,
) {

    @Inject
    constructor() : this(emptyMap(), emptyMap())

    fun addParamRandomizer(newRandomizers: Collection<ParameterRandomizer<*>>): RandomizerCollection {
        val newMap = newRandomizers.groupBy { it.paramClassData }
        return this.copy(
            parameterRandomizers = parameterRandomizers + newMap
        )
    }

    fun addParamRandomizer(vararg newRandomizers: ParameterRandomizer<*>): RandomizerCollection {
        return this.addParamRandomizer(newRandomizers.toList())
    }

    fun getParamRandomizer(key: RDClassData): List<ParameterRandomizer<*>>? {
        return parameterRandomizers[key]
    }

    fun addRandomizers(newRandomizers: Collection<ClassRandomizer<*>>): RandomizerCollection {

        val newMap: MutableMap<RDClassData, List<ClassRandomizer<*>>> = classRandomizers.toMutableMap()
        val newRandomizersMap = newRandomizers.groupBy { it.returnedInstanceData }

        for (newRdm in newRandomizersMap) {
            val lst = newMap[newRdm.key]
            val newLst = if (lst != null) {
                lst + newRdm.value
            } else {
                newRdm.value
            }
            newMap[newRdm.key] = newLst
        }

        return this.copy(
            classRandomizers = newMap.toMap()
        )
    }

    fun addRandomizers(vararg newRandomizers: ClassRandomizer<*>): RandomizerCollection {
        return this.addRandomizers(newRandomizers.toList())
    }

    fun getRandomizer(key: RDClassData): ClassRandomizer<*>? {
        val rt = this.classRandomizers.filter { it.key.kClass.isSubclassOf(key.kClass) }.values.flatten().randomOrNull()
        return rt
    }

    fun mergeWith(another: RandomizerCollection): RandomizerCollection {
        return RandomizerCollection(
            parameterRandomizers = this.parameterRandomizers + another.parameterRandomizers,
            classRandomizers = this.classRandomizers + another.classRandomizers,
        )
    }

}
