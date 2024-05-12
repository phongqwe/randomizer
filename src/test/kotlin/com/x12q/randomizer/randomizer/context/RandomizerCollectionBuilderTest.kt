package com.x12q.randomizer.randomizer.context

import com.x12q.randomizer.randomizer.builder.RandomizerListBuilder
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.test.Test

class RandomizerCollectionBuilderTest {

    @Test
    fun zz() {
        val builder = RandomizerCollectionBuilder()
        runBlocking {

            repeat(100){
                builder.addClassRandomizersBuilder(
                    RandomizerListBuilder()
                        .int(10)
                )
            }

            repeat(100){
                builder.addClassRandomizersBuilder(
                    RandomizerListBuilder().float(2f)
                )
            }

            repeat(100){
                builder.addClassRandomizersBuilder(
                    RandomizerListBuilder().string { "abc" }
                )
            }

            val l = builder.awaitComplete()
            val l2 = builder.awaitComplete()
            l.classRandomizers.size shouldBe 3
            l2.classRandomizers.size shouldBe 3
        }

    }


}
