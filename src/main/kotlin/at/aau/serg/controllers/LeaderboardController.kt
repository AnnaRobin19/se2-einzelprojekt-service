package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
//Aufgabe 2.2.2 Imports ergänzt
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(@RequestParam(required = false) rank: Int?): List<GameResult> {
        //Aufgabe 2.2.1 zuerst nach Score abssteigend sortieren und bei gleichem Score nach Zeit aufsteigend sortieren
        val sortedResults = gameResultService.getGameResults().sortedWith(compareBy<GameResult>({ -it.score }, { it.timeInSeconds }))

//Aufgabe 2.2.2 Rank Parameter
        if (rank == null) { //wenn Rank nicht angegeben ist, soll der Endpunkt weiterhin das gesamte Leaderboard zurückgeben
            return sortedResults
        }
        if (rank <= 0 || rank > sortedResults.size) { //Wenn rank ungültig ist (zu groß oder negativ), soll die Methode mit HTTP 400 antworten
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid")
        }
        val index = rank - 1 //rank beginnt bei 1, Listenindex bei 0
        val fromIndex = maxOf(0, index - 3) //Startindex berechnen, max 3 Spieler davor
        val toIndex = minOf(sortedResults.size, index + 4) //Endindex berechnen, max 3 Spieler danach

        return sortedResults.subList(fromIndex, toIndex) //Teil der Liste zurückgeben
    }
}
