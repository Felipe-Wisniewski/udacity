package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest {

    @Test
    fun getActiveAndCompletedStats_both_returnsZeroAndHundred() {
        //GIVEN a list of tasks with a single, active, task
        val tasks = listOf<Task>(Task("title", "descriptio", isCompleted = false))

        //WHEN you call getActiveAndCompletedStats
        val result = getActiveAndCompletedStats(tasks)

        //THEN there are 0% completed tasks and 100% active tasks
        assertThat(result.completedTasksPercent, `is`(0f))  //use hamcrest assertions
        assertEquals(100f, result.activeTasksPercent)   // use Junit assertions
    }

    //Se tenho 1 tarefa completa então retorne 100% completa e 0% ativa
    @Test
    fun getActiveAndCompletedStats_both_returnsHundredAndZero() {
        //Given
        val tasks = listOf<Task>(Task("title", "descriptio", isCompleted = true))

        //When
        val result = getActiveAndCompletedStats(tasks)

        //Then
        assertEquals(100f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }

    //Se tenho 1 tarefa incompleta e 1 completa então retorne 50% completa e 50% ativa
    @Test
    fun getActiveAndCompletedStats_both_returnsFifityAndFifity() {
        //Given
        val tasks = listOf<Task>(Task("title", "descriptio", isCompleted = false),
                Task("title", "descriptio", isCompleted = true))

        //When
        val result = getActiveAndCompletedStats(tasks)

        //Then
        assertEquals(50f, result.completedTasksPercent)
        assertEquals(50f, result.activeTasksPercent)
    }

    //se tenho 2 tarefas completas e 3 ativas então retorne 40% completa e 60% ativa
    @Test
    fun getActiveAndCompletedStats_both_returnsFortyAndSixty() {
        //Given
        val tasks = listOf<Task>(
                Task("title", "descriptio", isCompleted = true),
                Task("title", "descriptio", isCompleted = true),
                Task("title", "descriptio", isCompleted = false),
                Task("title", "descriptio", isCompleted = false),
                Task("title", "descriptio", isCompleted = false)
        )

        //When
        val result = getActiveAndCompletedStats(tasks)

        //Then
        assertEquals(40f, result.completedTasksPercent)
        assertEquals(60f, result.activeTasksPercent)
    }

    //Se tenho uma lista vazia então retorne 0% completa 0% ativa
    @Test
    fun getActiveAndCompletedStats_empty_returnsZeroAndZero() {
        //Given
        val tasks = emptyList<Task>()

        //When
        val result = getActiveAndCompletedStats(tasks)

        //Then
        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }

    //Se recebo null então retorne 0% completa 0% ativa
    @Test
    fun getActiveAndCompletedStats_null_returnsZeroAndZero() {
        //Given
        val tasks = null

        //When
        val result = getActiveAndCompletedStats(tasks)

        //THen
        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }
}