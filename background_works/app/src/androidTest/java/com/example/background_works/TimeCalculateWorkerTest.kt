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
    fun transformToMillis_returnLong1() {
        val str = "dsfsdT23:34:23+lkldskdl"
        val result = worker.transformToMillis(str)
        assertThat(result).isAtLeast(0L)
    }
    
    @Test
    fun transformToMillis_returnLong2() {
        val str = "a234fsT123:098:765+lkjlkh9632"
        val result = worker.transformToMillis(str)
        assertThat(result).isAtLeast(0L)
    }
    
    @Test
    fun transformToMillis_returnLong3() {
        val str = "a234fsT0987:093248:76675+lkjlkh9632"
        val result = worker.transformToMillis(str)
        assertThat(result).isAtLeast(0L)
    }
    
    @Test
    fun transformToMillis_returnLong4() {
        val str = "a234fsT234234:2342342:5867867+lkjlkh9632"
        val result = worker.transformToMillis(str)
        assertThat(result).isAtLeast(0L)
    }
    
    @Test
    fun doWork_returnSuccess1() {
        worker = TestWorkerBuilder<TimeCalculateWorker>(
            context = context,
            executor = executor,
            inputData = workDataOf(
                KEY_LAT to 90,
                KEY_LAT to 90,
                KEY_CALENDAR to 10000L
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
                KEY_CALENDAR to 10000000000L
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
                KEY_CALENDAR to 10000000000L
            )
        ).build()
        assertThat(worker.doWork()).isInstanceOf(Result.success()::class.java)
    }
}