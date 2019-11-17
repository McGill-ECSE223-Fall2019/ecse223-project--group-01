package ca.mcgill.ecse223.quoridor.controllers;

import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Tile;

public class PawnController {

    public static  boolean movePawn(String side) throws NotImplementedException {
        throw new NotImplementedException();
    }

    public static void getPossibleMoves() throws  NotImplementedException{
        throw new NotImplementedException();
    }
    
    public static boolean jumpPawn(Player player, String side) {
    	//player is white
    	int row, col;
		if (player.equals(ModelQuery.getWhitePlayer())) {
			Tile curTile = ModelQuery.getCurrentPosition().getWhitePosition().getTile();
			row = curTile.getRow();
			col = curTile.getColumn();
		}
		//player is black
		else {
			Tile curTile = ModelQuery.getCurrentPosition().getBlackPosition().getTile();
			row = curTile.getRow();
			col = curTile.getColumn();
		}
		
		switch(side){
        	case "left":{
        		col-=2;
        		break;
        	}
        	case "right":{
        		col+=2;
        		break;
        	}
        	case "up": {
        		row-=2;
        		break;
        	}
        	case "down":{
        		row+=2;
        		break;
        	}
		}
		Tile targetTile = ModelQuery.getTile(row, col);
		if (player.equals(ModelQuery.getWhitePlayer())) {
			ModelQuery.getCurrentPosition().getWhitePosition().setTile(targetTile);
			return true;
		}
		if (player.equals(ModelQuery.getBlackPlayer())) {
			ModelQuery.getCurrentPosition().getBlackPosition().setTile(targetTile);
			return true;
		}
		
		return false;
    			
    }
}
