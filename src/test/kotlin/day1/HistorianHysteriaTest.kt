package day1

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HistorianHysteriaTest {

    @Test
    fun `solves day 11`() {
        val result = solveDay11("/day1.txt")
        val expected = 11
        assertEquals(expected, result)
    }

    @Test
    fun `solves day 12`() {
        val result = solveDay12("/day1.txt")
        val expected = 31
        assertEquals(expected, result)
    }
}
