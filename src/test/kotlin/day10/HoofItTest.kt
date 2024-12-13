package day10

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HoofItTest {

    @Test
    fun `solves day 101`() {
        val result = solveDay101("/day10.txt")
        val expected = 36
        assertEquals(expected, result)
    }

    @Test
    fun `solves day 102`() {
        val result = solveDay102("/day10.txt")
        val expected = 81
        assertEquals(expected, result)
    }
}
