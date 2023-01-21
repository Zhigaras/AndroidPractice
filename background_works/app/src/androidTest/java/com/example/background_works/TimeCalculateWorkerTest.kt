package com.example.background_works

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.testing.TestWorkerBuilder
import androidx.work.workDataOf
import com.google.common.truth.Truth.assertThat
import androidx.work.ListenableWorker.Result
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class TimeCalculateWorkerTest {
    
    lateinit var context: Context
    lateinit var executor: Executor
    lateinit var worker: TimeCalculateWorker
    
    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        executor = Executors.newSingleThreadExecutor()
        worker = TestWorkerBuilder<TimeCalculateWorker>(
            context,
            executor
        ).build()
    }
    
    @Test
    fun doWork_returnSuccess1() {
        worker = TestWorkerBuilder<TimeCalculateWorker>(
            context = context,
            executor = executor,
            inputData = workDataOf(
                KEY_LAT to 90,
                KEY_LAT to 90,
                KEY_DATE to 100L,
                KEY_TIME to 100L
            )
        ).build()
        assertThat(worker.doWork()).isInstanceOf(Result.success()::class.java)
    }
    
    @Test
    fun doWork_returnSuccess2() {
        worker = TestWorkerBuilder<TimeCalculateWorker>(
            context = context,
            executor = executor,
            inputData = workDataOf(
                KEY_LAT to 1,
                KEY_LAT to 1,
                KEY_DATE to 10000000L,
                KEY_TIME to 10000000L
            )
        ).build()
        assertThat(worker.doWork()).isInstanceOf(Result.success()::class.java)
    }
    
    @Test
    fun doWork_returnSuccess3() {
        worker = TestWorkerBuilder<TimeCalculateWorker>(
            context = context,
            executor = executor,
            inputData = workDataOf(
                KEY_LAT to 40.234,
                KEY_LAT to 30.234234,
                KEY_DATE to 100000000000L,
                KEY_TIME to 100000000000L
            )
        ).build()
        assertThat(worker.doWork()).isInstanceOf(Result.success()::class.java)
    }
}