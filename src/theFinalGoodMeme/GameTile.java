package theFinalGoodMeme;

import java.util.ArrayList;

import javafx.scene.paint.Color;

public class GameTile {
	
	private String texture; //BLACK OR WHITE TILE
	private int row;
	private int column;
	private double xPos; //X COORD
	private double yPos; //Y COORD
	
	public GameTile(String texture, int row, int column) {
		this.texture = texture;
		this.row = row;
		this.column = column;
		xPos = (row-1)*75;
		yPos = (column-1)*75;
	}
	
	public double getRow() { return row; }
	
	public double getColumn() { return column; }
	
	public double getXPos() { return xPos; }
	
	public double getYPos() { return yPos; }
	
	public Color getRectColor() {
		
		if (texture.equals("LIGHT")) {
			return Color.BURLYWOOD;
		}
		else if (texture.equals("DARK")) {
			return Color.BROWN;
		}
		else {
			return Color.WHITE;
		}
		
	}
	
	public boolean tileRolledOver(double[] pos) {
		
		if (pos[0] > xPos && pos[0] < xPos + 75 && pos[1] > yPos && pos[1] < yPos + 75) {
			return true;
		}
		else { return false; }
		
	}
	
	public String getPiece(ArrayList<ChessPiece> chessPieces) {
		for (int i = 0; i < chessPieces.size(); i++) {
			if (chessPieces.get(i).getRow() == row && chessPieces.get(i).getColumn() == column) {
				return chessPieces.get(i).getPiece();
			}
		}
		return "BLANK";
	}

}
