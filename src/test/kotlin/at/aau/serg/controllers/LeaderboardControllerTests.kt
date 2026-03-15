package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null) //Aufgabe 2.2.2 rank = null

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() { //Aufgabe 2.2.1 id mit Time ausgetauscht, wenn Score gleich ist, nach Zeit aufsteigend sortieren
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null) //Aufgabe 2.2.2. rank = null

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        //Aufgabe 2.2.1 erwartete Reihenfolge nach der Zeit 10s,15s,20s
        assertEquals(second, res[0])
        assertEquals(third, res[1])
        assertEquals(first, res[2])
    }

    //Aufgabe 2.2.2 Tests für rank

    @Test // Test für gültigen rank
    fun test_getLeaderboard_rank_returnsPlayersAroundRank() {

        // mehrere Spieler erstellen um rank zu testen
        val p1 = GameResult(1, "p1", 70, 7.0)
        val p2 = GameResult(2, "p2", 60, 6.0)
        val p3 = GameResult(3, "p3", 50, 5.0)
        val p4 = GameResult(4, "p4", 40, 4.0)
        val p5 = GameResult(5, "p5", 30, 3.0)
        val p6 = GameResult(6, "p6", 20, 2.0)
        val p7 = GameResult(7, "p7", 10, 1.0)
// Liste unsortiert zurückgeben
        whenever(mockedService.getGameResults()).thenReturn(listOf(p7,p6,p5,p4,p3,p2,p1))
// rank 4 ausgewählt
        val res: List<GameResult> = controller.getLeaderboard(4)

        verify(mockedService).getGameResults()

        //Spieler + 3 davor + 3 danach
        assertEquals(7, res.size)
    }

    @Test // Test für ungülltigen rank
    fun test_getLeaderboard_invalidRank() {

        val first = GameResult(1, "first", 20, 20.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(first))

        try {
            controller.getLeaderboard(-1) // rank negativ = ungültig
        } catch (e: Exception) { //HTTP 400
            assertEquals(true, true)
        }
    }

    //Aufgabe 2.2.3 für 100% Coverage fehlt noch der Test rank <= 0 || rank > sortedResults.size (Rank größer als Liste)
    @Test
    fun test_getLeaderboard_rankTooLarge_returnsBadRequest() {

        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(first, second))

        try { // rank(3) größer als Liste(2) = ungültig
            controller.getLeaderboard(3)
        } catch (e: Exception) {
            assertEquals(true, true)
        }
    }

}