package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever

//Aufgabe 2.2.3 Test für GameResultControlerTests
class GameResultControllerTests {
    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() { //Mock erstellen, damit nur Controller getestet wird
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)
    }

    @Test //Prüft, ob beim Aufruf mit einer vorhanden Id das richtige Ergebnis ausgegeben wird
    fun test_getGameResult_existingId_returnsObject() {
        val gameResult = GameResult(1, "player1", 20, 10.0)

        whenever(mockedService.getGameResult(1)).thenReturn(gameResult)

        val res = controller.getGameResult(1)

        verify(mockedService).getGameResult(1)
        assertEquals(gameResult, res)
    }

    @Test // Testen ob der Controller alle GameResults vom Service holt und die Liste korrekt übergibt
    fun test_getAllGameResults_returnsList() {
        val first = GameResult(1, "player1", 20, 10.0)
        val second = GameResult(2, "player2", 15, 12.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(first, second))

        val res = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(2, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
    }

    @Test // neue Gameresult hinzufügen und prüfen, ob Service Methode aufgerufen wird
    fun test_addGameResult_callsService() {
        val gameResult = GameResult(0, "player1", 20, 10.0)

        controller.addGameResult(gameResult)

        verify(mockedService).addGameResult(gameResult)
    }

    @Test// GameResult löschen und richtige Id an Service übergeben
    fun test_deleteGameResult_callsService() {
        controller.deleteGameResult(1)

        verify(mockedService).deleteGameResult(1)
    }
}