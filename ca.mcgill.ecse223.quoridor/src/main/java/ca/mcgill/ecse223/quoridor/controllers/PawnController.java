package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.statemachine.defaultsm.DefaultSMStatemachine;
import ca.mcgill.ecse223.quoridor.statemachine.defaultsm.IDefaultSMStatemachine;
import ca.mcgill.ecse223.quoridor.view.InitializeBoardController;

import java.util.List;

public class PawnController {

    static final String UP = "up";
    static final String RIGHT = "right";
    static final String DOWN = "down";
    static final String LEFT = "left";
    static final String UPRIGHT = "upright";
    static final String UPLEFT = "upleft";
    static final String DOWNRIGHT = "downright";
    static final String DOWNLEFT = "downleft";

    /**
     * @author Tritin Truong, Kevin Li, Jason Lau
     * @param player
     * @param position
     *
     * This method initializes the state machine to be used for each pawn
     * The state machine is tied to the player
     */
    public static void initPawnSM(Player player, PlayerPosition position) {

        DefaultSMStatemachine statemachine = new DefaultSMStatemachine();

        IDefaultSMStatemachine.SCIPawnOperationCallback callback = (side, dist) -> {
            int travel = (int) dist;
            Tile source = position.getTile();
            switch (side) {
                case UP:
                    statemachine.getSCIPawn().setTargetRow(source.getRow() - (long)travel);
                    break;
                case RIGHT:
                    statemachine.getSCIPawn().setTargetCol(source.getColumn() + (long)travel);
                    break;
                case DOWN:
                    statemachine.getSCIPawn().setTargetRow(source.getRow() + (long)travel);
                    break;
                case LEFT:
                    statemachine.getSCIPawn().setTargetCol(source.getColumn() - (long)travel);
                    break;
                default:
                    break;
            }
        };

        IDefaultSMStatemachine.InternalOperationCallback internalCallback = new IDefaultSMStatemachine.InternalOperationCallback() {
            @Override
            public boolean isValidMove(String side) {
                Tile source = position.getTile();
                Tile dest;

                // gets 2 tiles, the starting tile, and the destination tile
                switch (side) {
                    case UP:
                        dest = ModelQuery.getTile(source.getRow() - 1, source.getColumn());
                        return !isWallBlocking(source, dest) && !isPawnBlocking(dest);
                    case RIGHT:
                        dest = ModelQuery.getTile(source.getRow(), source.getColumn() + 1);
                        return !isWallBlocking(source, dest) && !isPawnBlocking(dest);
                    case DOWN:
                        dest = ModelQuery.getTile(source.getRow() + 1, source.getColumn());
                        return !isWallBlocking(source, dest) && !isPawnBlocking(dest);
                    case LEFT:
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
                        Direction direction = wall.getMove().getWallDirection();
                        if (((wallTile.equals(invalidTile2)) || wallTile.equals(invalidTile1)) && direction == Direction.Horizontal) {
                            return true;
                        }
                    }
                    return false;
                } else if (t1.getRow() == t2.getRow()) {
                    Tile invalidTile2 = t1.getColumn() < t2.getColumn() ? t1 : t2;
                    Tile invalidTile1 = ModelQuery.getTile(invalidTile2.getRow() - 1, invalidTile2.getColumn());
                    for (Wall wall : placedWalls) {
                        Tile wallTile = wall.getMove().getTargetTile();
                        Direction direction = wall.getMove().getWallDirection();
                        if (((wallTile.equals(invalidTile2)) || wallTile.equals(invalidTile1)) && direction == Direction.Vertical) {
                            return true;
                        }
                    }
                    return false;
                }
                return true;
            }

            // Checks if there is a pawn at the location
            private boolean isPawnBlocking(Tile dest) {
                List<PlayerPosition> positions = ModelQuery.getAllPlayerPosition();
                if(dest==null){
                    return true;
                }
                for (PlayerPosition pawn_position : positions) {
                    if (pawn_position.getTile().getColumn() == dest.getColumn() && pawn_position.getTile().getRow() == dest.getRow()) {
                        return true;
                    }
                }
                return false;
            }

            /**
             *
             * @param side
             * @return
             *
             * Returns true if there is a valid jump move at the target side
             *
             */
            @Override
            public boolean isValidJump(String side) {
                Tile source = position.getTile();
                Tile dest;
                Tile enemyTile1;
                Tile enemyTile2;

                switch (side) {
                    case UP:
                        dest = ModelQuery.getTile(source.getRow() - 2, source.getColumn());
                        enemyTile1 = ModelQuery.getTile(source.getRow() - 1, source.getColumn());
                        return isPathValidJump(source, dest, enemyTile1);
                    case RIGHT:
                        dest = ModelQuery.getTile(source.getRow(), source.getColumn() + 2);
                        enemyTile1 = ModelQuery.getTile(source.getRow(), source.getColumn() + 1);
                        return isPathValidJump(source, dest, enemyTile1);
                    case DOWN:
                        dest = ModelQuery.getTile(source.getRow() + 2, source.getColumn());
                        enemyTile1 = ModelQuery.getTile(source.getRow() + 1, source.getColumn());
                        return isPathValidJump(source, dest, enemyTile1);
                    case LEFT:
                        dest = ModelQuery.getTile(source.getRow(), source.getColumn() - 2);
                        enemyTile1 = ModelQuery.getTile(source.getRow(), source.getColumn() - 1);
                        return isPathValidJump(source, dest, enemyTile1);
                    case UPRIGHT:
                        dest = ModelQuery.getTile(source.getRow() - 1, source.getColumn() + 1);
                        enemyTile1 = ModelQuery.getTile(source.getRow(), source.getColumn() + 1);
                        enemyTile2 = ModelQuery.getTile(source.getRow() - 1, source.getColumn());
                        return isPathValidJump(source, dest, enemyTile1) || isPathValidJump(source, dest, enemyTile2);
                    case UPLEFT:
                        dest = ModelQuery.getTile(source.getRow() - 1, source.getColumn() - 1);
                        enemyTile1 = ModelQuery.getTile(source.getRow(), source.getColumn() - 1);
                        enemyTile2 = ModelQuery.getTile(source.getRow() - 1, source.getColumn());
                        return isPathValidJump(source, dest, enemyTile1) || isPathValidJump(source, dest, enemyTile2);
                    case DOWNRIGHT:
                        dest = ModelQuery.getTile(source.getRow() + 1, source.getColumn() + 1);
                        enemyTile1 = ModelQuery.getTile(source.getRow(), source.getColumn() + 1);
                        enemyTile2 = ModelQuery.getTile(source.getRow() + 1, source.getColumn());
                        return isPathValidJump(source, dest, enemyTile1) || isPathValidJump(source, dest, enemyTile2);
                    case DOWNLEFT:
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
        };

        statemachine.setInternalOperationCallback(internalCallback);
        statemachine.getSCIPawn().setSCIPawnOperationCallback(callback);
        statemachine.init();
        statemachine.enter();
        player.setStatemachine(statemachine);
    }


    /**
     * @author Tritin Truong, Jason Lau, Kevin Li
     * @param side, the side where you want to move the pawn
     * @return
     *
     * This method controls the pawn movement, if the move completes the turn also automatically ends
     */
    public static boolean movePawn(String side) {
        DefaultSMStatemachine sm = ModelQuery.getPlayerToMove().getStatemachine();
        PlayerPosition playerPosition = ModelQuery.getPlayerPositionOfPlayerToMove();

        sm.getSCIPawn().setTargetRow(playerPosition.getTile().getRow());
        sm.getSCIPawn().setTargetCol(playerPosition.getTile().getColumn());

        switch (side) {
            case UP:
                sm.getSCIPawn().raiseUp();
                break;
            case UPRIGHT:
                sm.getSCIPawn().raiseUpRight();
                break;
            case RIGHT:
                sm.getSCIPawn().raiseRight();
                break;
            case DOWNRIGHT:
                sm.getSCIPawn().raiseDownRight();
                break;
            case DOWN:
                sm.getSCIPawn().raiseDown();
                break;
            case DOWNLEFT:
                sm.getSCIPawn().raiseDownLeft();
                break;
            case LEFT:
                sm.getSCIPawn().raiseLeft();
                break;
            case UPLEFT:
                sm.getSCIPawn().raiseUpLeft();
                break;
            default:
                return false;
        }

        if (sm.getSCIPawn().isRaisedMoveCompleted()) {
            Tile target = ModelQuery.getTile((int) sm.getSCIPawn().getTargetRow(), (int) sm.getSCIPawn().getTargetCol());
            Tile source = playerPosition.getTile();

            // Creating the move
            Move newMove = createPawnMove(target,source);
            ModelQuery.getCurrentGame().addMove(newMove);

            // Update the player position
            playerPosition.setTile(target);
            SwitchPlayerController.switchActivePlayer();
            InitializeBoardController.isPawnMoved = true;

            return true;
        } else {
            return false;
        }
    }

    private static Move createPawnMove(Tile target,Tile source){
        int movesSize = ModelQuery.getMoves().size();
        int moveNum;
        int roundNum;
        if (movesSize>0) {
            roundNum = ModelQuery.getMoves().get(movesSize-1).getRoundNumber()+1;
            moveNum = roundNum/2;
        }
        else {
            moveNum = 0;
            roundNum = 0;
        }

        Move newMove = null;
        if( Math.abs(target.getRow()-source.getRow())<=1 && Math.abs(target.getColumn()-source.getColumn())<=1 ) {
            newMove = new StepMove(moveNum,roundNum,ModelQuery.getPlayerToMove(),target,ModelQuery.getCurrentGame());
        }else{
            newMove = new JumpMove(moveNum,roundNum,ModelQuery.getPlayerToMove(),target,ModelQuery.getCurrentGame());
        }
        return newMove;
    }
}
