package day9

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DiskFragmenterTest {

    @Test
    fun `solves day 91`() {
        val result = solveDay91("/day9.txt")
        val expected = 1928L
        assertEquals(expected, result)
    }

    @Test
    fun `solves day 92`() {
        val result = solveDay92("/day9.txt")
        val expected = 2858L
        assertEquals(expected, result)
    }
}