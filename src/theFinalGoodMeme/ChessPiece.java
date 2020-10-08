package theFinalGoodMeme;

import javafx.scene.image.Image;

public class ChessPiece {
	private Image whitePawn = new Image("ChessPieces/White/WhitePawn.png", 75, 75, false, true);
	private Image whiteRook = new Image("ChessPieces/White/WhiteRook.png", 75, 75, false, true);
	private Image whiteKnight = new Image("ChessPieces/White/WhiteKnight.png", 75, 75, false, true);
	private Image whiteBishop = new Image("ChessPieces/White/WhiteBishop.png", 75, 75, false, true);
	private Image whiteKing = new Image("ChessPieces/White/WhiteKing.png", 75, 75, false, true);
	private Image whiteQueen = new Image("ChessPieces/White/WhiteQueen.png", 75, 75, false, true);
	
	private Image blackPawn = new Image("ChessPieces/Black/BlackPawn.png", 75, 75, false, true);
	private Image blackRook = new Image("ChessPieces/Black/BlackRook.png", 75, 75, false, true);
	private Image blackKnight = new Image("ChessPieces/Black/BlackKnight.png", 75, 75, false, true);
	private Image blackBishop = new Image("ChessPieces/Black/BlackBishop.png", 75, 75, false, true);
	private Image blackKing = new Image("ChessPieces/Black/BlackKing.png", 75, 75, false, true);
	private Image blackQueen = new Image("ChessPieces/Black/BlackQueen.png", 75, 75, false, true);
	
	private String piece;
	private int row;
	private int column;
	
	public ChessPiece(String piece) {
		this.piece = piece;
		this.row = 0;
		this.column = 0;
	}
	
	public ChessPiece(String piece, int row, int column) {
		this.piece = piece;
		this.row = row;
		this.column = column;
	}
	
	public int getRow() { return row; }
	
	public int getColumn() { return column; }
	
	public double getX() {
		return ((column-1) * 75);
	}
	
	public double getY() {
		return ((row-1) * 75);
	}
	
	public String getPiece() { return piece; }
	
	public void setPiece(String piece) { this.piece = piece; }
	
	public void setPos(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public Image getImage() {
		if (piece.contains("WHITEPAWN")) { return whitePawn; }
		else if (piece.contains("WHITEROOK")) { return whiteRook; }
		else if (piece.contains("WHITEKNIGHT")) { return whiteKnight; }
		else if (piece.contains("WHITEBISHOP")) { return whiteBishop; }
		else if (piece.contains("WHITEKING")) { return whiteKing; }
		else if (piece.contains("WHITEQUEEN")) { return whiteQueen; }
		else if (piece.contains("BLACKPAWN")) { return blackPawn; }
		else if (piece.contains("BLACKROOK")) { return blackRook; }
		else if (piece.contains("BLACKKNIGHT")) { return blackKnight; }
		else if (piece.contains("BLACKBISHOP")) { return blackBishop; }
		else if (piece.contains("BLACKKING")) { return blackKing; }
		else if (piece.contains("BLACKQUEEN")) { return blackQueen; }
		else { return null; }
	}
	
	public Image getMiniImage() {
		
		if (piece.contains("WHITEPAWN")) { return new Image("ChessPieces/White/WhitePawn.png", 20, 20, false, true); }
		else if (piece.contains("WHITEBISHOP")) { return new Image("ChessPieces/White/WhiteBishop.png", 20, 20, false, true); }
		else if (piece.contains("WHITEKNIGHT")) { return new Image("ChessPieces/White/WhiteKnight.png", 20, 20, false, true); }
		else if (piece.contains("WHITEROOK")) { return new Image("ChessPieces/White/WhiteRook.png", 20, 20, false, true); }
		else if (piece.contains("WHITEKING")) { return new Image("ChessPieces/White/WhiteKing.png", 20, 20, false, true); }
		else if (piece.contains("WHITEQUEEN")) { return new Image("ChessPieces/White/WhiteQueen.png", 20, 20, false, true); }
		
		else if (piece.contains("BLACKPAWN")) { return new Image("ChessPieces/Black/BlackPawn.png", 20, 20, false, true); }
		else if (piece.contains("BLACKBISHOP")) { return new Image("ChessPieces/Black/BlackBishop.png", 20, 20, false, true); }
		else if (piece.contains("BLACKKNIGHT")) { return new Image("ChessPieces/Black/BlackKnight.png", 20, 20, false, true); }
		else if (piece.contains("BLACKROOK")) { return new Image("ChessPieces/Black/BlackRook.png", 20, 20, false, true); }
		else if (piece.contains("BLACKKING")) { return new Image("ChessPieces/Black/BlackKing.png", 20, 20, false, true); }
		else if (piece.contains("BLACKQUEEN")) { return new Image("ChessPieces/Black/BlackQueen.png", 20, 20, false, true); }
		
		else { return null; }
		
	}
	
}
