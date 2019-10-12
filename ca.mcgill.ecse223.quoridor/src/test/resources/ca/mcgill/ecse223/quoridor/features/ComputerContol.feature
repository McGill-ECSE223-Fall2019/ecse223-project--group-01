Feature: Computer Control
  As a player who selected a computer opponent, I would like the computer to automatically make moves when it is its turn
  Background:
  Given The game is running

    Scenario: Computer plays a move
      Given It is my turn to move
      When I complete my move
      Then The computer computes a move
      And The move is valid
      And The computer completes the move
      And It shall be my turn to move
