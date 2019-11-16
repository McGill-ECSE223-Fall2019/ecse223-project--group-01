/** Generated by YAKINDU Statechart Tools code generator. */
package ca.mcgill.ecse223.quoridor.statemachine.defaultsm;

import ca.mcgill.ecse223.quoridor.statemachine.IStatemachine;

public interface IDefaultSMStatemachine extends IStatemachine {
	public interface InternalOperationCallback {
	
		public boolean isValidMove();
		
		public boolean isValidJump();
		
		public long pawnGetRow();
		
		public long pawnGetCol();
		
	}
	
	public void setInternalOperationCallback(InternalOperationCallback operationCallback);
	
	public interface SCIPawn {
	
		public void raiseSetup();
		
		public void raiseUp();
		
		public void raiseUpLeft();
		
		public void raiseUpRight();
		
		public void raiseDown();
		
		public void raiseDownLeft();
		
		public void raiseDownRight();
		
		public void raiseLeft();
		
		public void raiseRight();
		
		public void setSCIPawnOperationCallback(SCIPawnOperationCallback operationCallback);
	
	}
	
	public interface SCIPawnOperationCallback {
	
		public void movePawn(String i, long j);
		
	}
	
	public SCIPawn getSCIPawn();
	
}
