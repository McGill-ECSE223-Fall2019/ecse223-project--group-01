package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.statemachine.defaultsm.DefaultSMStatemachine;
import ca.mcgill.ecse223.quoridor.statemachine.defaultsm.IDefaultSMStatemachine;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class PawnController {
		public static  boolean movePawn(String side) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public static void initPawnSM(Player player, PlayerPosition position) {

        DefaultSMStatemachine statemachine = new DefaultSMStatemachine();

        IDefaultSMStatemachine.SCIPawnOperationCallback callback = (side, dist) -> {
            int travel = (int) dist;
            Tile source = position.getTile();
            Tile dest = source;
            switch (side) {
                case "up":
                    dest = ModelQuery.getTile(source.getRow() - travel, source.getColumn());
                    break;
                case "right":
                    dest = ModelQuery.getTile(source.getRow(), source.getColumn() + travel);
                    break;
                case "down":
                    dest = ModelQuery.getTile(source.getRow() + travel, source.getColumn());
                    break;
                case "left":
                    dest = ModelQuery.getTile(source.getRow(), source.getColumn() - travel);
                    break;
                default:
                    break;
            }
            position.setTile(dest);
        };

        IDefaultSMStatemachine.InternalOperationCallback internalCallback = new IDefaultSMStatemachine.InternalOperationCallback() {
            @Override
            public boolean isValidMove(String side) {
                Tile source = position.getTile();
                Tile dest;

                // gets 2 tiles, the starting tile, and the destination tile
                switch (side) {
                    case "up":
                        dest = ModelQuery.getTile(source.getRow() - 1, source.getColumn());
                        return !isWallBlocking(source, dest) && !isPawnBlocking(dest);
                    case "right":
                        dest = ModelQuery.getTile(source.getRow(), source.getColumn() + 1);
                        return !isWallBlocking(source, dest) && !isPawnBlocking(dest);
                    case "down":
                        dest = ModelQuery.getTile(source.getRow() + 1, source.getColumn());
                        return !isWallBlocking(source, dest) && !isPawnBlocking(dest);
                    case "left":
                        dest = ModelQuery.getTile(source.getRow(), source.getColumn() - 1);
                        return !isWallBlocking(source, dest) && !isPawnBlocking(dest);
                }
                return false;
            }


            // Checks if there is a wall obstructing the path between 2 tiles
            private boolean isWallBlocking(Tile t1, Tile t2) {
                List<Wall> placedWalls = ModelQuery.getAllWallsOnBoard();
                if (t1 == null || t2 == null) {
                    return true;
                }

                // Case for when they are on the same column
                if (t1.getColumn() == t2.getColumn()) {
                    Tile invalidTile2 = t1.getRow() < t2.getRow() ? t1 : t2;
                    Tile invalidTile1 = ModelQuery.getTile(invalidTile2.getRow(), invalidTile2.getColumn() - 1);
                    for (Wall wall : placedWalls) {
                        Tile wallTile = wall.getMove().getTargetTile();
                        if ((wallTile.equals(invalidTile2)) || wallTile.equals(invalidTile1)) {
                            return true;
                        }
                    }
                    return false;
                } else if (t1.getRow() == t2.getRow()) {
                    Tile invalidTile2 = t1.getColumn() < t2.getColumn() ? t1 : t2;
                    Tile invalidTile1 = ModelQuery.getTile(invalidTile2.getColumn(), invalidTile2.getColumn() + 1);
                    for (Wall wall : placedWalls) {
                        Tile wallTile = wall.getMove().getTargetTile();
                        if ((wallTile.equals(invalidTile2)) || wallTile.equals(invalidTile1)) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            }

            private boolean isPawnBlocking(Tile dest) {
                List<PlayerPosition> positions = ModelQuery.getAllPlayerPosition();
                for (PlayerPosition position : positions) {
                    if (position.getTile() == dest) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean isValidJump(String side) {
                Tile source = position.getTile();
                Tile dest;
                Tile enemyTile1;
                Tile enemyTile2;

                switch (side) {
                    case "up":
                        dest = ModelQuery.getTile(source.getRow(), source.getColumn() - 2);
                        enemyTile1 = ModelQuery.getTile(source.getRow() - 1, source.getColumn());
                        return isPathValidJump(source, dest, enemyTile1);
                    case "right":
                        dest = ModelQuery.getTile(source.getRow() + 2, source.getColumn());
                        enemyTile1 = ModelQuery.getTile(source.getRow(), source.getColumn() + 1);
                        return isPathValidJump(source, dest, enemyTile1);
                    case "down":
                        dest = ModelQuery.getTile(source.getRow(), source.getColumn() + 2);
                        enemyTile1 = ModelQuery.getTile(source.getRow() + 1, source.getColumn());
                        return isPathValidJump(source, dest, enemyTile1);
                    case "left":
                        dest = ModelQuery.getTile(source.getRow() - 2, source.getColumn());
                        enemyTile1 = ModelQuery.getTile(source.getRow(), source.getColumn() - 1);
                        return isPathValidJump(source, dest, enemyTile1);
                    case "upright":
                        dest = ModelQuery.getTile(source.getRow() - 1, source.getColumn() - 1);
                        enemyTile1 = ModelQuery.getTile(source.getRow(), source.getColumn() - 1);
                        enemyTile2 = ModelQuery.getTile(source.getRow() - 1, source.getColumn());
                        return isPathValidJump(source, dest, enemyTile1) || isPathValidJump(source, dest, enemyTile2);
                    case "upleft":
                        dest = ModelQuery.getTile(source.getRow() - 1, source.getColumn() + 1);
                        enemyTile1 = ModelQuery.getTile(source.getRow(), source.getColumn() + 1);
                        enemyTile2 = ModelQuery.getTile(source.getRow() - 1, source.getColumn());
                        return isPathValidJump(source, dest, enemyTile1) || isPathValidJump(source, dest, enemyTile2);
                    case "downright":
                        dest = ModelQuery.getTile(source.getRow() + 1, source.getColumn() + 1);
                        enemyTile1 = ModelQuery.getTile(source.getRow(), source.getColumn() + 1);
                        enemyTile2 = ModelQuery.getTile(source.getRow() + 1, source.getColumn());
                        return isPathValidJump(source, dest, enemyTile1) || isPathValidJump(source, dest, enemyTile2);
                    case "downleft":
                        dest = ModelQuery.getTile(source.getRow() + 1, source.getColumn() - 1);
                        enemyTile1 = ModelQuery.getTile(source.getRow(), source.getColumn() - 1);
                        enemyTile2 = ModelQuery.getTile(source.getRow() + 1, source.getColumn());
                        return isPathValidJump(source, dest, enemyTile1) || isPathValidJump(source, dest, enemyTile2);
                    default:
                        return false;
                }
            }

            private boolean isPathValidJump(Tile source, Tile dest, Tile enemyTile) {
                return isPawnBlocking(enemyTile) && !isWallBlocking(source, enemyTile) && !isWallBlocking(enemyTile, dest) && !isPawnBlocking(dest);
            }

            @Override
            public long pawnGetRow() {
                return position.getTile().getRow();
            }

            @Override
            public long pawnGetCol() {
                return position.getTile().getColumn();
            }
        };

        statemachine.setInternalOperationCallback(internalCallback);
        statemachine.getSCIPawn().setSCIPawnOperationCallback(callback);
        statemachine.init();
        statemachine.enter();
        player.setStatemachine(statemachine);
    }

    public static boolean movePawn(String side) throws NotImplementedException {
        DefaultSMStatemachine sm = ModelQuery.getPlayerToMove().getStatemachine();
        switch (side) {
            case "up":
                sm.getSCIPawn().raiseUp();
                break;
            case "upright":
                sm.getSCIPawn().raiseUpRight();
                break;
            case "right":
                sm.getSCIPawn().raiseRight();
                break;
            case "downright":
                sm.getSCIPawn().raiseDownRight();
                break;
            case "down":
                sm.getSCIPawn().raiseDown();
                break;
            case "downleft":
                sm.getSCIPawn().raiseDownLeft();
                break;
            case "left":
                sm.getSCIPawn().raiseLeft();
                break;
            case "upleft":
                sm.getSCIPawn().raiseUpLeft();
                break;
            default:
                return false;
        }

        if(sm.getSCIPawn().isRaisedMoveCompleted()){
            SwitchPlayerController.switchActivePlayer();
            return true;
        }
        else{
            return false;
        }
    }
}
