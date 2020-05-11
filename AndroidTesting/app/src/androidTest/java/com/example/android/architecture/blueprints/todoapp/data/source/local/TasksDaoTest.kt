package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** unit test - TaskDao */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                ToDoDatabase::class.java).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTaskAndGetById() = runBlockingTest {
        // GIVEN - insert task
        val task = Task("title", "description")
        database.taskDao().insertTask(task)

        // WHEN - get task by id from database
        val loaded = database.taskDao().getTaskById(task.id)

        // THEN - the loaded data contains the expected values
        assertThat(loaded as Task, notNullValue())
        assertThat(loaded.id, `is`(task.id))
        assertThat(loaded.title, `is`(task.title))
        assertThat(loaded.description, `is`(task.description))
        assertThat(loaded.isCompleted, `is`(task.isCompleted))
    }

    @Test
    fun updateTaskAndGetById() = runBlockingTest {
        // GIVEN - insert task into the db
        val task = Task("title", "description")
        database.taskDao().insertTask(task)

        // WHEN - update the task by creating a new task with the same ID but different attributes
        val taskLoaded = database.taskDao().getTaskById(task.id)
        taskLoaded?.let {
            it.title = "new title"
            it.description = "new description"
            database.taskDao().insertTask(it)
        }

        // THEN - check that when you get the task by its ID, it has the updated values
        val taskUpdated = database.taskDao().getTaskById(task.id)

        assertThat(taskUpdated as Task, notNullValue())
        assertThat(taskUpdated.id, `is`(task.id))
        assertThat(taskUpdated.title, `is`("new title"))
        assertThat(taskUpdated.description, `is`("new description"))
    }
}