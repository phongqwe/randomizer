package com.x12q.randomizer.randomizer.context

import com.x12q.randomizer.randomizer.ClassRandomizer
import com.x12q.randomizer.randomizer.ParameterRandomizer
import com.x12q.randomizer.randomizer.RandomizerCollection
import com.x12q.randomizer.randomizer.builder.RandomizerListBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RandomizerCollectionBuilder {

    private var _lv1RandomizerCollection: RandomizerCollection = RandomizerCollection()
    private val mutex = Mutex()

    private val topJob = Job()
    private val coroutineScope = CoroutineScope(topJob + Dispatchers.Default)

    /**
     * Schedule a job to modify
     */
    fun addClassRandomizersBuilder(builder: RandomizerListBuilder): RandomizerCollectionBuilder {
        scheduleUpdate {
            _lv1RandomizerCollection.addRandomizers(builder.build())
        }
        return this
    }

    fun addClassRandomizers(randomizers: Collection<ClassRandomizer<*>>): RandomizerCollectionBuilder {
        scheduleUpdate {
            _lv1RandomizerCollection.addRandomizers(randomizers)
        }
        return this
    }

    fun addParamRandomizer(paramRandomizers: Collection<ParameterRandomizer<*>>):RandomizerCollectionBuilder{
        scheduleUpdate {
            _lv1RandomizerCollection.addParamRandomizer(paramRandomizers)
        }
        return this
    }

    fun mergeWith(another:RandomizerCollectionBuilder):RandomizerCollectionBuilder{
        scheduleUpdate{
            _lv1RandomizerCollection.mergeWith(another._lv1RandomizerCollection)
        }
        return this
    }

    private fun scheduleUpdate(newCollection:()->RandomizerCollection){
        coroutineScope.launch {
            // use mutex here is to ensure thread-safety for the collection.
            mutex.withLock {
                _lv1RandomizerCollection = newCollection()
            }
        }
    }

    suspend fun awaitComplete(): RandomizerCollection {
        // wait for all the job in the coroutine scope to complete before returning the collection
        if(topJob.isActive){
            topJob.complete()
            topJob.join()
        }
        return _lv1RandomizerCollection
    }
}
