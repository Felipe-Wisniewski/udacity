package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
class TasksViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

// use Rule ^val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var tasksRepository: FakeTestRepository
    private lateinit var tasksViewModel: TasksViewModel

    @Before
    fun setup() {
//        Dispatchers.setMain(testDispatcher) // change Main Scope TO Test Scope

        tasksRepository = FakeTestRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        tasksRepository.addTasks(task1, task2, task3)

        tasksViewModel = TasksViewModel(tasksRepository)
    }

//    @After
//    fun tearDownDispatcher() {
//        Dispatchers.resetMain()
//        testDispatcher.cleanupTestCoroutines()
//    }

    @Test
    fun addNewTask_setNewTaskEvent() {
        // WHEN adding a new task
        tasksViewModel.addNewTask()

        // THEN the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), (not(nullValue())))
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        // WHEN adding a new task
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)
        tasksViewModel.getTasksAddViewVisible()

        // THEN the new task event is triggered
        val value = tasksViewModel.tasksAddViewVisible.getOrAwaitValue()
        assertThat(value, `is`(true))
    }

    @Test
    fun completeTask_dataAndSnackbarUpdated() {
        // Create an active task and add it to the repository.
        val task = Task("Title", "Description")
        tasksRepository.addTasks(task)

        // Mark the task as complete task.
        tasksViewModel.completeTask(task, true)

        // Verify the task is completed.
        assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))

        // Assert that the snackbar has been updated with the correct text.
        val snackbarText: Event<Int> =  tasksViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
    }
}