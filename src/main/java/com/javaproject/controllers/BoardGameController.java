package com.javaproject.controllers;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.javaproject.beans.BoardGame;
import com.javaproject.beans.ErrorMessage;
import com.javaproject.database.DatabaseAccess;

@RestController
@RequestMapping("/boardgames")
public class BoardGameController {

    private final DatabaseAccess da;

    public BoardGameController(DatabaseAccess da) {
        this.da = da;
    }

    /**
     * Retrieve all boardgames
     * 
     * @return List of BoardGame
     */
    @GetMapping
    public List<BoardGame> getBoardGames() {
        return da.getBoardGames();
    }

    /**
     * Handles requests for specific boardgame
     * 
     * @param id the ID of the board game
     * @return the ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardGame(@PathVariable Long id) {
        BoardGame boardGame = da.getBoardGame(id);
        if (boardGame != null) {
            return ResponseEntity.ok(boardGame);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No such record"));
        }
    }

    /**
     * Adds a new board game
     * 
     * @param boardGame the board game to add
     * @return the ResponseEntity
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> postBoardGame(@RequestBody BoardGame boardGame) {
        try {
            Long id = da.addBoardGame(boardGame);
            boardGame.setId(id);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
            return ResponseEntity.created(location).body(boardGame);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage("Name already exists."));
        }
    }
}
