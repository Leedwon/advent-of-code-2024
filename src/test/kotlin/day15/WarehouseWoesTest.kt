package day15

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class WarehouseWoesTest {

    @Test
    fun `solves day 151`() {
        val result = solveDay151("/day15.txt")
        val expected = 10092
        assertEquals(expected, result)
    }

    @Test
    fun `solves day 152`() {
        val result = solveDay152("/day15.txt")
        val expected = 9021
        assertEquals(expected, result)
    }
}