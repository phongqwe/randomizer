package com.x12q.randomizer.ir_plugin.transformers.di

import com.x12q.randomizer.ir_plugin.transformers.randomizable.RandomizableTransformer
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import javax.inject.Singleton


@Singleton
@MergeComponent(
    scope = P7AnvilScope::class,
)
interface P7Component {

    fun randomizableTransformer(): RandomizableTransformer


    @Component.Builder
    interface Builder {
        fun setIRPluginContext( @BindsInstance pluginContext: IrPluginContext):Builder
        fun build(): P7Component
    }
}