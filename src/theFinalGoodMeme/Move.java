package theFinalGoodMeme;

import javafx.scene.image.Image;

public class Move {

	private int moveNumber;
	private String piece;
	private int[] initialPos;
	private int[] finalPos;
	private ChessPiece taken;
	private String specialMove;
	
	public Move (int moveNumber, String piece, int[] initialPos, int[] finalPos, ChessPiece taken) {
		this.moveNumber = moveNumber;
		this.piece = piece;
		this.initialPos = initialPos;
		this.finalPos = finalPos;
		this.taken = taken;
		specialMove = "NONE";
	}
	
	public Move (int moveNumber, String piece, int[] initialPos, int[] finalPos, ChessPiece taken, String specialMove) {
		this.moveNumber = moveNumber;
		this.piece = piece;
		this.initialPos = initialPos;
		this.finalPos = finalPos;
		this.taken = taken;
		this.specialMove = specialMove;
	}
	
	public int getMoveNumber() {
		return moveNumber;
	}
	
	public String getPiece() {
		return piece;
	}
	
	public ChessPiece getTakenPiece() {
		return taken;
	}
	
	public int[] getInitialArrayPos() {
		return initialPos;
	}
	
	public int[] getFinalArrayPos() {
		return finalPos;
	}
	
	public String getSpecialMove() {
		return specialMove;
	}
	
	public String getInitialPos() {
		
		String columnLetter = "";
		
		if (initialPos[0] == 1) {columnLetter = "A";}
		else if (initialPos[0]==2) {columnLetter = "B";}
		else if (initialPos[0]==3) {columnLetter = "C";}
		else if (initialPos[0]==4) {columnLetter = "D";}
		else if (initialPos[0]==5) {columnLetter = "E";}
		else if (initialPos[0]==6) {columnLetter = "F";}
		else if (initialPos[0]==7) {columnLetter = "G";}
		else if (initialPos[0]==8) {columnLetter = "H";}
		
		return (columnLetter + initialPos[1]);
		
	}
	
	public String getFinalPos() {
		
		String columnLetter = "";
		
		if (finalPos[1] == 1) {columnLetter = "A";}
		else if (finalPos[1]==2) {columnLetter = "B";}
		else if (finalPos[1]==3) {columnLetter = "C";}
		else if (finalPos[1]==4) {columnLetter = "D";}
		else if (finalPos[1]==5) {columnLetter = "E";}
		else if (finalPos[1]==6) {columnLetter = "F";}
		else if (finalPos[1]==7) {columnLetter = "G";}
		else if (finalPos[1]==8) {columnLetter = "H";}
		
		return (columnLetter + finalPos[0]);
		
	}
	
	public Image getMiniImage(String movePiece) {
		
		if (movePiece.equals("PIECE")) { movePiece = piece; }
		else if (movePiece.equals("TAKEN")) { movePiece = taken.getPiece(); }
		
		if (movePiece.contains("WHITEPAWN")) { return new Image("ChessPieces/White/WhitePawn.png", 20, 20, false, true); }
		else if (movePiece.contains("WHITEBISHOP")) { return new Image("ChessPieces/White/WhiteBishop.png", 20, 20, false, true); }
		else if (movePiece.contains("WHITEKNIGHT")) { return new Image("ChessPieces/White/WhiteKnight.png", 20, 20, false, true); }
		else if (movePiece.contains("WHITEROOK")) { return new Image("ChessPieces/White/WhiteRook.png", 20, 20, false, true); }
		else if (movePiece.contains("WHITEKING")) { return new Image("ChessPieces/White/WhiteKing.png", 20, 20, false, true); }
		else if (movePiece.contains("WHITEQUEEN")) { return new Image("ChessPieces/White/WhiteQueen.png", 20, 20, false, true); }
		
		else if (movePiece.contains("BLACKPAWN")) { return new Image("ChessPieces/Black/BlackPawn.png", 20, 20, false, true); }
		else if (movePiece.contains("BLACKBISHOP")) { return new Image("ChessPieces/Black/BlackBishop.png", 20, 20, false, true); }
		else if (movePiece.contains("BLACKKNIGHT")) { return new Image("ChessPieces/Black/BlackKnight.png", 20, 20, false, true); }
		else if (movePiece.contains("BLACKROOK")) { return new Image("ChessPieces/Black/BlackRook.png", 20, 20, false, true); }
		else if (movePiece.contains("BLACKKING")) { return new Image("ChessPieces/Black/BlackKing.png", 20, 20, false, true); }
		else if (movePiece.contains("BLACKQUEEN")) { return new Image("ChessPieces/Black/BlackQueen.png", 20, 20, false, true); }
		
		else { return null; }
		
	}
	
}
