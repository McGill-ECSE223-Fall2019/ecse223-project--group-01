package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.statemachine.defaultsm.DefaultSMStatemachine;
import ca.mcgill.ecse223.quoridor.statemachine.defaultsm.IDefaultSMStatemachine;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PawnController {
    public static  boolean movePawn(String side) throws NotImplementedException {
        DefaultSMStatemachine statemachine = new DefaultSMStatemachine();

        IDefaultSMStatemachine.SCIPawnOperationCallback callback = new IDefaultSMStatemachine.SCIPawnOperationCallback() {
            @Override
            public void movePawn(String i, long j) {

            }
        };

        IDefaultSMStatemachine.InternalOperationCallback internalCallback = new IDefaultSMStatemachine.InternalOperationCallback() {
            @Override
            public boolean isValidMove() {
                return false;
            }

            @Override
            public boolean isValidJump() {
                return false;
            }

            @Override
            public long pawnGetRow() {
                return 0;
            }

            @Override
            public long pawnGetCol() {
                return 0;
            }
        };

        statemachine.setInternalOperationCallback(internalCallback);
        statemachine.getSCIPawn().setSCIPawnOperationCallback(callback);

        throw new NotImplementedException();
    }

    public static void getPossibleMoves() throws  NotImplementedException{
        throw new NotImplementedException();
    }
}
