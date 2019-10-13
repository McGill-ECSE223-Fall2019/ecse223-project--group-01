Feature: Computer Control
  As a player who selected a computer opponent, I would like the computer to automatically make moves when it is its turn
  or suggest moves for me when it is my turn

  Background:
  Given The game is running

    Scenario: Computer plays a move
      Given It is not my turn to move
      When The computer computes a move
      Then The move is registered
      And It shall be my turn to move

    Scenario: Computer suggests a move for the player
      Given It is my turn to move
      When I ask for a move suggestion
      Then I am notified of a possible move
      And It is my turn to move
