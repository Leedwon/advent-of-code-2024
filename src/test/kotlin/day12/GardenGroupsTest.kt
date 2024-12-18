package day12

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GardenGroupsTest {

    @Test
    fun `solves day 121`() {
        val result = solveDay121("/day12.txt")
        val expected = 1930
        assertEquals(expected, result)
    }

    @Test
    fun `solves day 122`() {
        val result = solveDay122("/day12.txt")
        val expected = 1206
        assertEquals(expected, result)
    }
}
