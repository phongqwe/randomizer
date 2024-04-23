package com.x12q.randomizer.annotation_processor.param

import com.x12q.randomizer.randomizer.parameter.ParameterRandomizer
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

sealed class InvalidParamRandomizerReason {
    abstract val randomizerKClass: KClass<out ParameterRandomizer<*>>
    abstract val parentClass: KClass<*>
    abstract val targetKParam: KParameter

    /**
     * When the checked class is an abstract class
     */
    data class IsAbstract(
        override val randomizerKClass: KClass<out ParameterRandomizer<*>>,
        override val targetKParam: KParameter,
        override val parentClass: KClass<*>
    ) : InvalidParamRandomizerReason()

    /**
     * When the checked class is a [ClassRandomizer] but cannot generate random instance of [expectedType]
     */
    data class WrongTargetType(
        override val randomizerKClass: KClass<out ParameterRandomizer<*>>,
        override val targetKParam: KParameter,
        override val parentClass: KClass<*>,
        val actualTypes: KClass<*>?,
        val expectedType: KClass<*>,
    ) : InvalidParamRandomizerReason()

    /**
     * When the checked class is not of type [ClassRandomizer]
     */
    data class IllegalClass(
        override val randomizerKClass: KClass<out ParameterRandomizer<*>>,
        override val targetKParam: KParameter,
        override val parentClass: KClass<*>,
    ) : InvalidParamRandomizerReason()
}