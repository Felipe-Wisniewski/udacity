package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** integration test - localDataSource with taskDao */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TasksLocalDataSourceTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase
    private lateinit var localDataSource: TasksLocalDataSource

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                ToDoDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        localDataSource = TasksLocalDataSource(database.taskDao(), Dispatchers.Main)
    }

    @After
    fun cleanUp() = database.close()

    // runBlocking is used here because of https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    // Replace with runBlockingTest once issue is resolved
    @Test
    fun saveTask_retrievesTask() = runBlocking {
        // GIVEN - a new task saved in the database
        val newTask = Task("title", "description", false)
        localDataSource.saveTask(newTask)

        // WHEN - task retrieved by ID
        val result = localDataSource.getTask(newTask.id)

        // THEN - same task is returned
        assertThat(result.succeeded, `is`(true))
        result as Result.Success
        assertThat(result.data.title, `is`("title"))
        assertThat(result.data.description, `is`("description"))
        assertThat(result.data.isCompleted, `is`(false))
    }

    @Test
    fun completeTask_retrivedTaskIsComplete() = runBlocking {
        // GIVEN - save a new active task in the local data source
        val newTask = Task("title", "description", false)
        localDataSource.saveTask(newTask)

        // WHEN - mark it as complete
        localDataSource.completeTask(newTask.id)

        // THEN - check that the task can be retrieved from the local data source and is complete
        val completeTask = localDataSource.getTask(newTask.id)

        assertThat(completeTask, notNullValue())
        completeTask as Result.Success
        assertThat(completeTask.data.isCompleted, `is`(true))
    }
}