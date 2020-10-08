package theFinalGoodMeme;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class Main extends Application {

	private static int[] selectedTile = new int[2]; //ARRAY OF COORDS OF SELECTED TILE (COLUMN, ROW)
	private static GameTile[][] tiles; //ROW, COLUMN
	private static ArrayList<Move> moveHistory = new ArrayList<Move>();
	private static ArrayList<Move> undidMoves = new ArrayList<Move>();
	private static ArrayList<ChessPiece> chessPieces = new ArrayList<ChessPiece>();
	private static ArrayList<ChessPiece> takenPieces = new ArrayList<ChessPiece>();
	private static ArrayList<int[]> moves = new ArrayList<int[]>();
	private static TextFlow dialogueBox = new TextFlow();
	private static VBox movesList = new VBox();
	private static HBox header = new HBox();
	private static GridPane blackTakenPieces = new GridPane();
	private static GridPane whiteTakenPieces = new GridPane();
	private static int moveCounter = 0;
	private static boolean click = false;
	private static boolean blackCheck = false;
	private static boolean whiteCheck = false;
	private static boolean blackCheckmate = false;
	private static boolean whiteCheckmate = false;
	private static boolean whiteTurn = true;
	private static boolean movedEnPassant = false;
	private static int[] enPassantMove = {};
	private static boolean whiteVictory = false;
	private static boolean blackVictory = false;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 815, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("SamTech Chess");
			primaryStage.getIcons().add(new Image("chessicon.png"));
			//primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			Canvas canvas = new Canvas(600, 600);
			VBox menu = new VBox();
			HBox undoButtons = new HBox();
			HBox saveButtons = new HBox();
			ScrollPane moveHistoryScroll = new ScrollPane();
			
			menu.setAlignment(Pos.CENTER);
			menu.setSpacing(10);
			
			Button saveButton = new Button("SAVE");
			saveButton.setPrefSize(60, 20);
			Button loadButton = new Button("LOAD");
			loadButton.setPrefSize(60, 20);
			saveButtons.getChildren().addAll(saveButton, loadButton);
			saveButtons.setPadding(new Insets(10, 12, 0, 30));
			saveButtons.setSpacing(30);
			saveButtons.setAlignment(Pos.CENTER_LEFT);
			menu.getChildren().add(saveButtons);
			
			//SETS UP DIALOGUE BOX
			dialogueBox.setPrefSize(200, 100);
		    dialogueBox.setTextAlignment(TextAlignment.CENTER);
		    dialogueBox.setLineSpacing(5.0);
		    dialogueBox.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		    updateDialogueBox();
			menu.getChildren().add(dialogueBox);
			
			//SETS UP MOVE HISTORY SCROLL BAR
			Text move = new Text("MOVE");
			move.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
			Text piece = new Text("PIECE");
			piece.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
			Text from = new Text("FROM");
			from.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
			Text to = new Text("TO");
			to.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
			Text taken = new Text("TAKEN");
			taken.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
			
			header.getChildren().addAll(move, piece, from, to, taken);
		    header.setSpacing(10);
		    header.setAlignment(Pos.CENTER_LEFT);
			movesList.getChildren().add(header);
			
			Button blackSurrender = new Button("BLACK SURRENDER!");
			blackSurrender.setPrefSize(150, 20);
			menu.getChildren().add(blackSurrender);

			whiteTakenPieces.setPadding(new Insets(3, 3, 3, 3));
			menu.getChildren().add(whiteTakenPieces);
			
			Button reset = new Button("RESET GAME");
			reset.setPrefSize(100, 20);
			menu.getChildren().add(reset);

			blackTakenPieces.setPadding(new Insets(3, 3, 3, 3));
			menu.getChildren().add(blackTakenPieces);
			
			Button whiteSurrender = new Button("WHITE SURRENDER!");
			whiteSurrender.setPrefSize(150, 20);
			menu.getChildren().add(whiteSurrender);
			
			moveHistoryScroll.setContent(movesList);
			moveHistoryScroll.setPrefViewportHeight(150);
			moveHistoryScroll.setPrefViewportWidth(200);
			menu.getChildren().add(moveHistoryScroll);
			
			//SETS UP UNDO AND REDO BUTTONS
			Button undoButton = new Button("UNDO");
			undoButton.setPrefSize(60, 20);
			Button redoButton = new Button("REDO");
			redoButton.setPrefSize(60, 20);
			undoButtons.getChildren().addAll(undoButton, redoButton);
			undoButtons.setPadding(new Insets(0, 12, 0, 30));
			undoButtons.setSpacing(30);
			undoButtons.setAlignment(Pos.CENTER_LEFT);
			menu.getChildren().add(undoButtons);

			Text samTech = new Text("             SamTech™ 2019");
			samTech.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 15));
			menu.getChildren().add(samTech);
			
			root.getChildren().addAll(canvas);
			root.setRight(menu);
			
			ArrayList<String> input = new ArrayList<String>();
			
			//GIVES FUNCTION TO SAVE BUTTON
			saveButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
				    try {
						saveGame();
					} catch (IOException e) { }
				}
				
			});
			
			//GIVES FUNCTION TO LOAD BUTTON
			loadButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					try {
						loadGame();
					} catch (FileNotFoundException e) { }
				}
				
			});
			
			//GIVES FUNCTION TO RESET BUTTON
			reset.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					try {
						chessPieces = startGame();
					} catch (FileNotFoundException e) { }
				}
				
			});
			
			//GIVES FUNCTION TO BLACK SURRENDER BUTTON
			whiteSurrender.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					selectedTile = new int[] {0,0};
					blackVictory = true;
					updateDialogueBox();
				}
				
			});
			
			//GIVES FUNCTION TO WHITE SURRENDER BUTTON
			blackSurrender.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					selectedTile = new int[] {0,0};
					whiteVictory = true;
					updateDialogueBox();
				}
				
			});
			
			//GIVES FUNCTION TO UNDO BUTTON
	        undoButton.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	undoButtonMethod();
	            }
	        });
			
	        //GIVES FUNCTION TO REDO BUTTON
	        redoButton.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent event) {
	            	redoButtonMethod();
	            }
	        });
	        
			//HANDLES INPUT OF PRESSING DOWN KEYBOARD KEYS
			scene.setOnKeyPressed(
				new EventHandler<KeyEvent>() {
					public void handle (KeyEvent e) {
						String event = e.getCode().toString();
						if (!input.contains(event)) {
							input.add(event);
						}
					}
				}
			);
			
			//HANDLES INPUT OF RELEASING KEYBOARD KEYS
			scene.setOnKeyReleased(
				new EventHandler<KeyEvent>() {
					public void handle (KeyEvent e) {
						String event = e.getCode().toString();
						input.remove(event);
					}
				}
			);
			
			//GETS NEW COORDS EVERYTIME MOUSE MOVES
			double[] mouseCoords = new double[2];
			scene.setOnMouseMoved(
				new EventHandler<MouseEvent>() {
					public void handle (MouseEvent event) {
						mouseCoords[0] = event.getX();
						mouseCoords[1] = event.getY();
					}
				}
			);
			
			//DETECTS A CLICK
			scene.setOnMouseReleased(
				new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						click = true;
					}
				}
			);
			
			GraphicsContext gc = canvas.getGraphicsContext2D();
			
			tiles = new GameTile[8][8]; //ROW, COLUMN
			
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (i % 2 == 1 && j % 2 == 1 || i % 2 == 0 && j % 2 == 0) {
						tiles[i][j] = new GameTile("LIGHT", i+1, j+1);
					}
					else {
						tiles[i][j] = new GameTile("DARK", i+1, j+1);
					}
				}
			}
			
			chessPieces = startGame();
			
			//MAIN GAME LOOP
			new AnimationTimer() {

				@Override
				public void handle(long currentTime) {
					
					//DRAWS CHESSBOARD
					for (int i = 0; i < 8; i++) {
						for (int j = 0; j < 8; j++) {
							gc.setFill(tiles[i][j].getRectColor());
							gc.fillRect(tiles[i][j].getXPos(), tiles[i][j].getYPos(), 75, 75);
							gc.setStroke(Color.BLACK);
							gc.setLineWidth(2);
							gc.strokeRect(tiles[i][j].getXPos(), tiles[i][j].getYPos(), 75, 75);
							
							//DRAWS BLUE SQUARE ON SELECTED TILE
							if (i == selectedTile[0]-1 && j == selectedTile[1]-1) {
								gc.setGlobalAlpha(0.5);
								gc.setFill(Color.DARKBLUE);
								gc.fillRect((selectedTile[0]-1)*75, (selectedTile[1]-1)*75, 75, 75);
								gc.setGlobalAlpha(1);
							}
						}
					}

					//DRAWS ORANGE OUTLINE OF TILE THAT MOUSE IS OVER
					if (!whiteVictory && !blackVictory) {
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								if (tiles[i][j].tileRolledOver(mouseCoords)) {
									gc.setStroke(Color.ORANGE);
									gc.setLineWidth(4);
									gc.strokeRoundRect(tiles[i][j].getXPos(), tiles[i][j].getYPos(), 75, 75, 3, 3);
								}
							}
						}
					}
					
					//DRAWS GAMEPIECES
					for (int i = 0; i < chessPieces.size(); i++) {
						gc.drawImage(chessPieces.get(i).getImage(), chessPieces.get(i).getX(), chessPieces.get(i).getY());
					}
					
					if (!whiteVictory && !blackVictory) {
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								
								//CHECKS IF TILE IS CLICKED
								if (tiles[i][j].tileRolledOver(mouseCoords) && click) {
									
									//IF CLICKED TILE IS A POSSIBLE MOVE, MOVE PIECE TO SAID TILE
									int[] clickedTile = {(int)tiles[i][j].getColumn(), (int)tiles[i][j].getRow()};
									
									//ESTABLISHES TURN SYSTEM
									if (tiles[j][i].getPiece(chessPieces).contains("WHITE") == whiteTurn && !tiles[j][i].getPiece(chessPieces).contains("BLANK")|| possibleMoveSelected(moves, clickedTile)) {
									
										if (possibleMoveSelected(moves, clickedTile)) {
											//UPDATES MOVE COUNTER
											moveCounter++;
											
											int[] originalTile = selectedTile;
											
											//MOVES PIECE
											movePiece(originalTile, clickedTile);
											gameOver();
											
											undidMoves.clear();
											
											whiteTurn = !whiteTurn;
											
											//CHECKS IF KINGS ARE IN CHECK IF TILE WAS MOVED
						        			blackCheck = false;
						        			whiteCheck = false;
						        			blackCheckmate = false;
						        			whiteCheckmate = false;
											if (getCheck("BLACK")) {
												if (getCheckmate("BLACK")) { blackCheckmate = true; }
												else { blackCheck = true; }
											}
											else {
												blackCheck = false;
												blackCheckmate = false;
											}
											if (getCheck("WHITE")) {
												if (getCheckmate("WHITE")) { whiteCheckmate = true; }
												else { whiteCheck = true; }
											}
											else {
												whiteCheck = false;
												whiteCheckmate = false;
											}
										}
										//IF THE SELECTED TILE WAS CLICKED, GET RID OF THE SELECTED TILE
										else if (selectedTile[0] == i+1 && selectedTile[1] == j+1) {
											selectedTile = new int[] {0,0};
										}
										//IF NOT, CHANGE SELECTED TILE TO CLICKED TILE
										else {
											selectedTile = new int[] {i+1,j+1};
										}
										
										//CHECKS IF GAME IS OVER
									    updateDialogueBox();
									}
								}
							}
						}
					}
					
					//UPDATES LIST OF POSSIBLE MOVES
					for (int i = 0; i < 8; i++) {
						for (int j = 0; j < 8; j++) {
							if (tiles[i][j].tileRolledOver(mouseCoords) && click) {
								moves = showPossibleMoves(selectedTile, chessPieces);
							}
						}
					}
					
					//DRAWS POSSIBLE MOVES
					for (int i = 0; i < moves.size(); i++) {
						gc.setGlobalAlpha(0.5);
						gc.setFill(Color.DARKGREEN);
						gc.fillRect((moves.get(i)[0]-1)*75, (moves.get(i)[1]-1)*75, 75, 75);
						gc.setGlobalAlpha(1);
					}
					
					//CHECKS FOR CHECK OR MATE CONDITIONS, THEN DRAWS RED FOR MATE OR YELLOW FOR CHECK ON KING IF SO
					if (blackCheckmate) {
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								if (tiles[i][j].getPiece(chessPieces).contains("BLACKKING")) {
									gc.setGlobalAlpha(0.5);
									gc.setFill(Color.DARKRED);
									gc.fillRect(tiles[i][j].getYPos(), tiles[i][j].getXPos(), 75, 75);
									gc.setGlobalAlpha(1);
								}
							}
						}
					}
					else if (blackCheck) {
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								if (tiles[i][j].getPiece(chessPieces).contains("BLACKKING")) {
									gc.setGlobalAlpha(0.5);
									gc.setFill(Color.YELLOW);
									gc.fillRect(tiles[i][j].getYPos(), tiles[i][j].getXPos(), 75, 75);
									gc.setGlobalAlpha(1);
								}
							}
						}
					}
					if (whiteCheckmate) {
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								if (tiles[i][j].getPiece(chessPieces).contains("WHITEKING")) {
									gc.setGlobalAlpha(0.5);
									gc.setFill(Color.DARKRED);
									gc.fillRect(tiles[i][j].getYPos(), tiles[i][j].getXPos(), 75, 75);
									gc.setGlobalAlpha(1);
								}
							}
						}
					}
					else if (whiteCheck) {
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								if (tiles[i][j].getPiece(chessPieces).contains("WHITEKING")) {
									gc.setGlobalAlpha(0.5);
									gc.setFill(Color.YELLOW);
									gc.fillRect(tiles[i][j].getYPos(), tiles[i][j].getXPos(), 75, 75);
									gc.setGlobalAlpha(1);
								}
							}
						}
					}
					
				click = false;
				}
			}.start();
			
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private static void setTakenGrids() {
		
		blackTakenPieces.setVgap(1); 
		blackTakenPieces.setHgap(1);
		blackTakenPieces.setPadding(new Insets(10, 10, 10, 10)); 
		blackTakenPieces.setMinSize(200, 50); 
		blackTakenPieces.setAlignment(Pos.CENTER); 
		
		whiteTakenPieces.setVgap(1); 
		whiteTakenPieces.setHgap(1);  
		whiteTakenPieces.setPadding(new Insets(10, 10, 10, 10));
		whiteTakenPieces.setMinSize(200, 50);
		whiteTakenPieces.setAlignment(Pos.CENTER);
	      
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 8; j++) {
				blackTakenPieces.add(new ImageView(new Image(("TransperantImage.png"))), j, i);
				whiteTakenPieces.add(new ImageView(new Image(("TransperantImage.png"))), j, i);
			}
		}
		
	}

	private static void updateTakenPiecesGrid() {
		
	    blackTakenPieces.getChildren().clear();
	    whiteTakenPieces.getChildren().clear();
	    setTakenGrids();

		for (int i = 0; i < takenPieces.size(); i++) {
			if (takenPieces.get(i).getPiece().contains("BLACKROOKLEFT")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 0, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKKNIGHTLEFT")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 1, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKBISHOPLEFT")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 2, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKQUEEN")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 3, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKKING")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 4, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKBISHOPRIGHT")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 5, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKKNIGHTRIGHT")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 6, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKROOKRIGHT")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 7, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKPAWN1")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 0, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKPAWN2")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 1, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKPAWN3")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 2, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKPAWN4")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 3, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKPAWN5")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 4, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKPAWN6")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 5, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKPAWN7")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 6, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("BLACKPAWN8")) {
				blackTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 7, 1);
			}
			
			else if (takenPieces.get(i).getPiece().contains("WHITEROOKLEFT")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 0, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEKNIGHTLEFT")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 1, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEBISHOPLEFT")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 2, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEQUEEN")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 3, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEKING")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 4, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEBISHOPRIGHT")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 5, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEKNIGHTRIGHT")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 6, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEROOKRIGHT")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 7, 1);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEPAWN1")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 0, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEPAWN2")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 1, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEPAWN3")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 2, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEPAWN4")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 3, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEPAWN5")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 4, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEPAWN6")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 5, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEPAWN7")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 6, 0);
			}
			else if (takenPieces.get(i).getPiece().contains("WHITEPAWN8")) {
				whiteTakenPieces.add(new ImageView(takenPieces.get(i).getMiniImage()), 7, 0);
			}
		}
		
	}

	private static ArrayList<ChessPiece> startGame() throws FileNotFoundException {
		
		moveHistory.clear();
		undidMoves.clear();
		takenPieces.clear();
		moves.clear();
		moveCounter = 0;
		blackCheck = false;
		whiteCheck = false;
		blackCheckmate = false;
		whiteCheckmate = false;
		whiteTurn = true;
		whiteVictory = false;
		blackVictory = false;
		
		ArrayList<ChessPiece> chessPieces = new ArrayList<ChessPiece>();
		
		chessPieces.add(new ChessPiece("WHITEROOKLEFT", 8, 1));
		chessPieces.add(new ChessPiece("WHITEROOKRIGHT", 8, 8));
		chessPieces.add(new ChessPiece("WHITEKNIGHTLEFT", 8, 2));
		chessPieces.add(new ChessPiece("WHITEKNIGHTRIGHT", 8, 7));
		chessPieces.add(new ChessPiece("WHITEBISHOPLEFT", 8, 3));
		chessPieces.add(new ChessPiece("WHITEBISHOPRIGHT", 8, 6));
		chessPieces.add(new ChessPiece("WHITEQUEEN", 8, 4));
		
		for (int i = 0; i < 8; i++) {
			chessPieces.add(new ChessPiece("WHITEPAWN" + (i+1), 7, (i+1)));
		}
		
		chessPieces.add(new ChessPiece("BLACKROOKLEFT", 1, 1));
		chessPieces.add(new ChessPiece("BLACKROOKRIGHT", 1, 8));
		chessPieces.add(new ChessPiece("BLACKKNIGHTLEFT", 1, 2));
		chessPieces.add(new ChessPiece("BLACKKNIGHTRIGHT", 1, 7));
		chessPieces.add(new ChessPiece("BLACKBISHOPLEFT", 1, 3));
		chessPieces.add(new ChessPiece("BLACKBISHOPRIGHT", 1, 6));
		chessPieces.add(new ChessPiece("BLACKQUEEN", 1, 4));
		
		for (int i = 0; i < 8; i++) {
			chessPieces.add(new ChessPiece("BLACKPAWN" + (i+1), 2, (i+1)));
		}

		chessPieces.add(new ChessPiece("BLACKKING", 1, 5));
		chessPieces.add(new ChessPiece("WHITEKING", 8, 5));
		
		updateDialogueBox();
		updateTakenPiecesGrid();
		updateHistoryList(moveHistory);
		
		return chessPieces;
		
	}
	
	private static ArrayList<int[]> showPossibleMoves(int[] selectedTile, ArrayList<ChessPiece> chessPieces) {
		
		if (selectedTile[0] == 0) {
			return new ArrayList<int[]>();
		}
		else if (tiles[selectedTile[1]-1][selectedTile[0]-1].getPiece(chessPieces).contains("BLANK")) {
			return new ArrayList<int[]>();
		}
		else {
			
			ChessPiece piece = new ChessPiece("WHITEROOK");
			ArrayList<int[]> moves = new ArrayList<int[]>();
			
			for (int i = 0; i < chessPieces.size(); i++) {
				if (chessPieces.get(i).getColumn() == selectedTile[0] && chessPieces.get(i).getRow() == selectedTile[1]) {
					piece = chessPieces.get(i);
				}
			}
			
			if (piece.getPiece().contains("WHITEPAWN") || piece.getPiece().contains("BLACKPAWN") ) {
				moves = getPawnMoves(piece);
			}
			else if (piece.getPiece().contains("WHITEKNIGHT") || piece.getPiece().contains("BLACKKNIGHT") ) {
				moves = getKnightMoves(piece);
			}
			else if (piece.getPiece().contains("WHITEROOK") || piece.getPiece().contains("BLACKROOK") ) {
				moves = getRookMoves(piece);
			}
			else if (piece.getPiece().contains("WHITEBISHOP") || piece.getPiece().contains("BLACKBISHOP") ) {
				moves = getBishopMoves(piece);
			}
			else if (piece.getPiece().contains("WHITEQUEEN") || piece.getPiece().contains("BLACKQUEEN") ) {
				moves = getQueenMoves(piece);
			}
			else if (piece.getPiece().contains("WHITEKING") || piece.getPiece().contains("BLACKKING") ) {
				moves = getKingMoves(piece);
			}
			
			//REMOVES ALL POSSIBLE INVALID MOVES WITH ROWS AND COLUMNS >8 OR <1
			for (int i = 0; i < moves.size(); i++) {
				if (moves.get(i)[0] < 1 || moves.get(i)[0] > 8 || moves.get(i)[1] < 1 || moves.get(i)[1] > 8) {
					moves.remove(i);
					i--;
				}
			}
			
			//REMOVES ALL POSSIBLE MOVES WITH PIECES OF THE SAME COLOUR
			for (int i = 0; i < moves.size(); i++) {
				if (tiles[moves.get(i)[1]-1][moves.get(i)[0]-1].getPiece(chessPieces).contains("WHITE") && piece.getPiece().contains("WHITE")) {
					moves.remove(i);
					i--;
				}
				else if (tiles[moves.get(i)[1]-1][moves.get(i)[0]-1].getPiece(chessPieces).contains("BLACK") && piece.getPiece().contains("BLACK")) {
					moves.remove(i);
					i--;
				}
			}
			
			return moves;
		}
	}
	
	private static ArrayList<int[]> getPawnMoves(ChessPiece piece) {
		
		ArrayList<int[]> moves = new ArrayList<int[]>();

		if (piece.getPiece().contains("WHITEPAWN")) {
			
			if (piece.getRow() != 1) {
				if (tiles[piece.getRow()-2][piece.getColumn()-1].getPiece(chessPieces).contains("BLANK")) {
					moves.add(new int[]{piece.getColumn(), piece.getRow()-1});
					
					if (piece.getRow() == 7 && tiles[piece.getRow()-3][piece.getColumn()-1].getPiece(chessPieces).contains("BLANK")) {
						moves.add(new int[]{piece.getColumn(), piece.getRow()-2});
					}
				}
			}
			
			if (piece.getColumn() != 1) {
				if (tiles[piece.getRow()-2][piece.getColumn()-2].getPiece(chessPieces).contains("BLACK")) {
					moves.add(new int[]{piece.getColumn()-1, piece.getRow()-1});
				}
			}
			if (piece.getColumn() != 8) {
				if (tiles[piece.getRow()-2][piece.getColumn()].getPiece(chessPieces).contains("BLACK")) {
					moves.add(new int[]{piece.getColumn()+1, piece.getRow()-1});
				}
			}
			
			//EN PASSANT
			if (piece.getRow() == 4) {
				for (int i = 0; i < chessPieces.size(); i++) {
					if (chessPieces.get(i).getPiece().contains("BLACKPAWN") && chessPieces.get(i).getRow() == 4 && chessPieces.get(i).getColumn() == piece.getColumn()-1) {
						if (moveHistory.get(moveHistory.size()-1).getPiece().equals(chessPieces.get(i).getPiece()) && moveHistory.get(moveHistory.size()-1).getInitialArrayPos()[1] == 2) {
							moves.add(new int[]{piece.getColumn()-1, piece.getRow()-1});
							movedEnPassant = true;
							enPassantMove = new int[]{piece.getColumn()-1, piece.getRow()-1};
						}
					}
					if (chessPieces.get(i).getPiece().contains("BLACKPAWN") && chessPieces.get(i).getRow() == 4 && chessPieces.get(i).getColumn() == piece.getColumn()+1) {
						if (moveHistory.get(moveHistory.size()-1).getPiece().equals(chessPieces.get(i).getPiece()) && moveHistory.get(moveHistory.size()-1).getInitialArrayPos()[1] == 2) {
							moves.add(new int[]{piece.getColumn()+1, piece.getRow()-1});
							movedEnPassant = true;
							enPassantMove = new int[]{piece.getColumn()+1, piece.getRow()-1};
						}
					}
				}
			}
			
		}
		
		else if (piece.getPiece().contains("BLACKPAWN")) {
			
			if (piece.getRow() != 8) {
				if (tiles[piece.getRow()][piece.getColumn()-1].getPiece(chessPieces).contains("BLANK")) {
					moves.add(new int[]{piece.getColumn(), piece.getRow()+1});
					
					if (piece.getRow() == 2 && tiles[piece.getRow()+1][piece.getColumn()-1].getPiece(chessPieces).contains("BLANK")) {
						moves.add(new int[]{piece.getColumn(), piece.getRow()+2});
					}
				}
			}
			
			if (piece.getColumn() != 1) {
				if (tiles[piece.getRow()][piece.getColumn()-2].getPiece(chessPieces).contains("WHITE")) {
					moves.add(new int[]{piece.getColumn()-1, piece.getRow()+1});
				}
			}
			if (piece.getColumn() != 8) {
				if (tiles[piece.getRow()][piece.getColumn()].getPiece(chessPieces).contains("WHITE")) {
					moves.add(new int[]{piece.getColumn()+1, piece.getRow()+1});
				}
			}
			
			if (piece.getRow() == 5) {
				for (int i = 0; i < chessPieces.size(); i++) {
					if (chessPieces.get(i).getPiece().contains("WHITEPAWN") && chessPieces.get(i).getRow() == 5 && chessPieces.get(i).getColumn() == piece.getColumn()-1) {
						if (moveHistory.get(moveHistory.size()-1).getPiece().equals(chessPieces.get(i).getPiece()) && moveHistory.get(moveHistory.size()-1).getInitialArrayPos()[1] == 7) {
							moves.add(new int[]{piece.getColumn()-1, piece.getRow()+1});
							movedEnPassant = true;
							enPassantMove = new int[]{piece.getColumn()-1, piece.getRow()+1};
						}
					}
					if (chessPieces.get(i).getPiece().contains("WHITEPAWN") && chessPieces.get(i).getRow() == 5 && chessPieces.get(i).getColumn() == piece.getColumn()+1) {
						if (moveHistory.get(moveHistory.size()-1).getPiece().equals(chessPieces.get(i).getPiece()) && moveHistory.get(moveHistory.size()-1).getInitialArrayPos()[1] == 7) {
							moves.add(new int[]{piece.getColumn()+1, piece.getRow()+1});
							movedEnPassant = true;
							enPassantMove = new int[]{piece.getColumn()+1, piece.getRow()+1};
						}
					}
				}
			}
			
		}
		return moves;
	}

	private static ArrayList<int[]> getKnightMoves(ChessPiece piece) {
		
		ArrayList<int[]> moves = new ArrayList<int[]>();

		moves.add(new int[]{piece.getColumn()+2, piece.getRow()+1});
		moves.add(new int[]{piece.getColumn()+2, piece.getRow()-1});
		moves.add(new int[]{piece.getColumn()-2, piece.getRow()+1});
		moves.add(new int[]{piece.getColumn()-2, piece.getRow()-1});
		moves.add(new int[]{piece.getColumn()+1, piece.getRow()+2});
		moves.add(new int[]{piece.getColumn()+1, piece.getRow()-2});
		moves.add(new int[]{piece.getColumn()-1, piece.getRow()+2});
		moves.add(new int[]{piece.getColumn()-1, piece.getRow()-2});
		
		//REMOVES ALL POSSIBLE INVALID MOVES WITH ROWS AND COLUMNS >8 OR <1
		for (int i = 0; i < moves.size(); i++) {
			if (moves.get(i)[0] < 1 || moves.get(i)[0] > 8 || moves.get(i)[1] < 1 || moves.get(i)[1] > 8) {
				moves.remove(i);
				i--;
			}
		}
		
		return moves;
		
	}
	
	private static ArrayList<int[]> getRookMoves(ChessPiece piece) {
		
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		int column = piece.getColumn()-1;
		int row = piece.getRow();
		boolean blocked = false;
		
		//CHECK SPACES TO LEFT OF ROOK
		while (column > 0 && blocked == false) {
			if (tiles[row-1][column-1].getPiece(chessPieces).contains("BLANK")) {
				moves.add(new int[]{column, row});
			}
			else {
				if (piece.getPiece().contains("WHITE") && tiles[row-1][column-1].getPiece(chessPieces).contains("BLACK") ||
					piece.getPiece().contains("BLACK") && tiles[row-1][column-1].getPiece(chessPieces).contains("WHITE")) {
					moves.add(new int[]{column, row});
				}
				blocked = true;
			}
			column--;
		}
		
		column = piece.getColumn()+1;
		row = piece.getRow();
		blocked = false;
		
		//CHECK SPACES TO RIGHT OF ROOK
		while (column < 9 && blocked == false) {
			if (tiles[row-1][column-1].getPiece(chessPieces).contains("BLANK")) {
				moves.add(new int[]{column, row});
			}
			else {
				if (piece.getPiece().contains("WHITE") && tiles[row-1][column-1].getPiece(chessPieces).contains("BLACK") ||
					piece.getPiece().contains("BLACK") && tiles[row-1][column-1].getPiece(chessPieces).contains("WHITE")) {
					moves.add(new int[]{column, row});
				}
				blocked = true;
			}
			column++;
		}
		
		column = piece.getColumn();
		row = piece.getRow()+1;
		blocked = false;
		
		//CHECK SPACES ABOVE ROOK
		while (row < 9 && blocked == false) {
			if (tiles[row-1][column-1].getPiece(chessPieces).contains("BLANK")) {
				moves.add(new int[]{column, row});
			}
			else {
				if (piece.getPiece().contains("WHITE") && tiles[row-1][column-1].getPiece(chessPieces).contains("BLACK") ||
					piece.getPiece().contains("BLACK") && tiles[row-1][column-1].getPiece(chessPieces).contains("WHITE")) {
					moves.add(new int[]{column, row});
				}
				blocked = true;
			}
			row++;
		}
		
		column = piece.getColumn();
		row = piece.getRow()-1;
		blocked = false;
		
		//CHECK SPACES BELOW ROOK
		while (row > 0 && blocked == false) {
			if (tiles[row-1][column-1].getPiece(chessPieces).contains("BLANK")) {
				moves.add(new int[]{column, row});
			}
			else {
				if (piece.getPiece().contains("WHITE") && tiles[row-1][column-1].getPiece(chessPieces).contains("BLACK") ||
					piece.getPiece().contains("BLACK") && tiles[row-1][column-1].getPiece(chessPieces).contains("WHITE")) {
					moves.add(new int[]{column, row});
				}
				blocked = true;
			}
			row--;
		}
		
		return moves;
	}

	private static ArrayList<int[]> getBishopMoves(ChessPiece piece) {
		
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		int column = piece.getColumn()-1;
		int row = piece.getRow()-1;
		boolean blocked = false;
		
		//CHECK SPACES TO TOP LEFT OF ROOK
		while (column > 0 && row > 0 && blocked == false) {
			if (tiles[row-1][column-1].getPiece(chessPieces).contains("BLANK")) {
				moves.add(new int[]{column, row});
			}
			else {
				if (piece.getPiece().contains("WHITE") && tiles[row-1][column-1].getPiece(chessPieces).contains("BLACK") ||
					piece.getPiece().contains("BLACK") && tiles[row-1][column-1].getPiece(chessPieces).contains("WHITE")) {
					moves.add(new int[]{column, row});
				}
				blocked = true;
			}
			column--;
			row--;
		}
		
		column = piece.getColumn()+1;
		row = piece.getRow()-1;
		blocked = false;
		
		//CHECK SPACES TO TOP RIGHT OF ROOK
		while (column < 9 && row > 0 && blocked == false) {
			if (tiles[row-1][column-1].getPiece(chessPieces).contains("BLANK")) {
				moves.add(new int[]{column, row});
			}
			else {
				if (piece.getPiece().contains("WHITE") && tiles[row-1][column-1].getPiece(chessPieces).contains("BLACK") ||
					piece.getPiece().contains("BLACK") && tiles[row-1][column-1].getPiece(chessPieces).contains("WHITE")) {
					moves.add(new int[]{column, row});
				}
				blocked = true;
			}
			column++;
			row--;
		}
		
		column = piece.getColumn()+1;
		row = piece.getRow()+1;
		blocked = false;
		
		//CHECK SPACES TO BOTTOM RIGHT OF BISHOP
		while (row < 9 && column < 9 && blocked == false) {
			if (tiles[row-1][column-1].getPiece(chessPieces).contains("BLANK")) {
				moves.add(new int[]{column, row});
			}
			else {
				if (piece.getPiece().contains("WHITE") && tiles[row-1][column-1].getPiece(chessPieces).contains("BLACK") ||
					piece.getPiece().contains("BLACK") && tiles[row-1][column-1].getPiece(chessPieces).contains("WHITE")) {
					moves.add(new int[]{column, row});
				}
				blocked = true;
			}
			row++;
			column++;
		}
		
		column = piece.getColumn()-1;
		row = piece.getRow()+1;
		blocked = false;
		
		//CHECK SPACES TO BOTTOM LEFT OF BISHOP
		while (row < 9 && column > 0 && blocked == false) {
			if (tiles[row-1][column-1].getPiece(chessPieces).contains("BLANK")) {
				moves.add(new int[]{column, row});
			}
			else {
				if (piece.getPiece().contains("WHITE") && tiles[row-1][column-1].getPiece(chessPieces).contains("BLACK") ||
					piece.getPiece().contains("BLACK") && tiles[row-1][column-1].getPiece(chessPieces).contains("WHITE")) {
					moves.add(new int[]{column, row});
				}
				blocked = true;
			}
			row++;
			column--;
		}
		
		return moves;
		
	}
	
	private static ArrayList<int[]> getQueenMoves(ChessPiece piece) {
		
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		ArrayList<int[]> rookMoves = getRookMoves(piece);
		ArrayList<int[]> bishopMoves = getBishopMoves(piece);
		
		for (int i = 0; i < rookMoves.size(); i++) {
			moves.add(rookMoves.get(i));
		}
		for (int i = 0; i < bishopMoves.size(); i++) {
			moves.add(bishopMoves.get(i));
		}
		
		return moves;
		
	}

	private static ArrayList<int[]> getKingMoves(ChessPiece piece) {
		
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		moves.add(new int[]{piece.getColumn()+1, piece.getRow()});
		moves.add(new int[]{piece.getColumn()-1, piece.getRow()});
		moves.add(new int[]{piece.getColumn(), piece.getRow()+1});
		moves.add(new int[]{piece.getColumn(), piece.getRow()-1});
		moves.add(new int[]{piece.getColumn()+1, piece.getRow()+1});
		moves.add(new int[]{piece.getColumn()+1, piece.getRow()-1});
		moves.add(new int[]{piece.getColumn()-1, piece.getRow()+1});
		moves.add(new int[]{piece.getColumn()-1, piece.getRow()-1});
		
		//CASTLING (ARE YOU HAPPY, ALDOWRTH AND SAVOY?)
		if (!whiteCheck && whiteTurn) {
			if (tiles[7][4].getPiece(chessPieces).contains("WHITEKING") && 
				tiles[7][7].getPiece(chessPieces).contains("WHITEROOKRIGHT") &&
				tiles[7][5].getPiece(chessPieces).contains("BLANK") &&
				tiles[7][6].getPiece(chessPieces).contains("BLANK")) {
				
				boolean moved = false;
				
				for (int i = 0; i < moveHistory.size(); i++) {
					if (moveHistory.get(i).getPiece().contains("WHITEKING") || moveHistory.get(i).getPiece().contains("WHITEROOKRIGHT")) {
						moved = true;
					}
				}
				
				if (!moved) { moves.add(new int[]{7, 8}); }
				
			}
			if (tiles[7][4].getPiece(chessPieces).contains("WHITEKING") && 
				tiles[7][0].getPiece(chessPieces).contains("WHITEROOKLEFT") &&
				tiles[7][1].getPiece(chessPieces).contains("BLANK") &&
				tiles[7][2].getPiece(chessPieces).contains("BLANK") &&
				tiles[7][3].getPiece(chessPieces).contains("BLANK")) {
				
				boolean moved = false;
				
				for (int i = 0; i < moveHistory.size(); i++) {
					if (moveHistory.get(i).getPiece().contains("WHITEKING") || moveHistory.get(i).getPiece().contains("WHITEROOKLEFT")) {
						moved = true;
					}
				}
				
				if (!moved) {moves.add(new int[]{3, 8}); }
			}
		}
		if (!blackCheck && !whiteTurn) {
			if (tiles[0][4].getPiece(chessPieces).contains("BLACKKING") && 
				tiles[0][7].getPiece(chessPieces).contains("BLACKROOKRIGHT") &&
				tiles[0][5].getPiece(chessPieces).contains("BLANK") &&
				tiles[0][6].getPiece(chessPieces).contains("BLANK")) {
				
				boolean moved = false;
				
				for (int i = 0; i < moveHistory.size(); i++) {
					if (moveHistory.get(i).getPiece().contains("BLACKKING") || moveHistory.get(i).getPiece().contains("BLACKROOKRIGHT")) {
						moved = true;
					}
				}
				
				if (!moved) { moves.add(new int[]{7, 1}); }
				
			}
			if (tiles[0][4].getPiece(chessPieces).contains("BLACKKING") && 
				tiles[0][0].getPiece(chessPieces).contains("BLACKROOKLEFT") &&
				tiles[0][1].getPiece(chessPieces).contains("BLANK") &&
				tiles[0][2].getPiece(chessPieces).contains("BLANK") &&
				tiles[0][3].getPiece(chessPieces).contains("BLANK")) {
				
				boolean moved = false;
				
				for (int i = 0; i < moveHistory.size(); i++) {
					if (moveHistory.get(i).getPiece().contains("BLACKKING") || moveHistory.get(i).getPiece().contains("BLACKROOKLEFT")) {
						moved = true;
					}
				}
				
				if (!moved) {moves.add(new int[]{3, 1}); }
			}
		}
		
		//REMOVES ALL POSSIBLE INVALID MOVES WITH ROWS AND COLUMNS >8 OR <1
		for (int i = 0; i < moves.size(); i++) {
			if (moves.get(i)[0] < 1 || moves.get(i)[0] > 8 || moves.get(i)[1] < 1 || moves.get(i)[1] > 8) {
				moves.remove(i);
				i--;
			}
		}
		
		for (int i = 0; i < moves.size(); i++) {
			if (tiles[moves.get(i)[1]-1][moves.get(i)[0]-1].getPiece(chessPieces).contains("WHITE") && piece.getPiece().contains("WHITE")) {
				moves.remove(i);
				i--;
			}
			else if (tiles[moves.get(i)[1]-1][moves.get(i)[0]-1].getPiece(chessPieces).contains("BLACK") && piece.getPiece().contains("BLACK")) {
				moves.remove(i);
				i--;
			}
		}
		
		return moves;
	}
	
	private static boolean possibleMoveSelected(ArrayList<int[]> moves, int[] clickedTile) {
		
		for (int i = 0; i < moves.size(); i++) {
			if (clickedTile[1] == moves.get(i)[0] && clickedTile[0] == moves.get(i)[1]) {
				return true;
			}
		}
		return false;
	}

	private static void movePiece(int[] originalPos, int[] finalPos) {
		
		ChessPiece takenPiece = new ChessPiece("---");
		
		//REMOVES PIECE ON TILE THAT PIECE IS MOVING TO
		if (movedEnPassant) {
			for (int i = 0; i < chessPieces.size(); i++) {
				if (chessPieces.get(i).getRow() == originalPos[1] && chessPieces.get(i).getColumn() == originalPos[0]) {
					if (chessPieces.get(i).getPiece().contains("BLACK")) {
						for (int j = 0; j < chessPieces.size(); j++) {
							if (chessPieces.get(j).getRow() == finalPos[0] - 1 && chessPieces.get(j).getColumn() == finalPos[1]) {
								takenPiece = chessPieces.get(j);
								takenPieces.add(chessPieces.get(j));
								updateTakenPiecesGrid();
								chessPieces.remove(j);
								break;
							}
						}
					}
					else if (chessPieces.get(i).getPiece().contains("WHITE")) {
						for (int j = 0; j < chessPieces.size(); j++) {
							if (chessPieces.get(j).getRow() == finalPos[0] + 1 && chessPieces.get(j).getColumn() == finalPos[1]) {
								takenPiece = chessPieces.get(j);
								takenPieces.add(chessPieces.get(j));
								updateTakenPiecesGrid();
								chessPieces.remove(j);
								break;
							}
						}
					}
				}
			}
		}
		else {
			for (int i = 0; i < chessPieces.size(); i++) {
				if (chessPieces.get(i).getRow() == finalPos[0] && chessPieces.get(i).getColumn() == finalPos[1]) {
					takenPiece = chessPieces.get(i);
					takenPieces.add(chessPieces.get(i));
					updateTakenPiecesGrid();
					chessPieces.remove(i);
					break;
				}
			}
		}
		
		//MOVES PIECE TO TILE
		for (int i = 0; i < chessPieces.size(); i++) {
			if (chessPieces.get(i).getColumn() == originalPos[0] && chessPieces.get(i).getRow() == originalPos[1]) {
				chessPieces.get(i).setPos(finalPos[0], finalPos[1]);
				
				//DEALS WITH CASTLING
				if (chessPieces.get(i).getPiece().contains("WHITEKING") && Math.abs(originalPos[0] - finalPos[1]) == 2) {
					moveHistory.add(new Move(moveCounter, chessPieces.get(i).getPiece(), originalPos, finalPos, takenPiece, "CASTLE"));
					if (originalPos[0] - finalPos[1] > 0) {
						movePiece(new int[] {1, 8}, new int[] {8, 4});
					}
					else {
						movePiece(new int[] {8, 8}, new int[] {8, 6});
					}
				}
				else if (chessPieces.get(i).getPiece().contains("BLACKKING") && Math.abs(originalPos[0] - finalPos[1]) == 2) {
					moveHistory.add(new Move(moveCounter, chessPieces.get(i).getPiece(), originalPos, finalPos, takenPiece, "CASTLE"));
					if (originalPos[0] - finalPos[1] > 0) {
						movePiece(new int[] {1, 1}, new int[] {1, 4});
					}
					else {
						movePiece(new int[] {8, 1}, new int[] {1, 6});
					}
				}
				
				//DEALS WITH EN PASSANT
				else if (movedEnPassant && enPassantMove[0] == finalPos[1] && enPassantMove[1] == finalPos[0]) {
					moveHistory.add(new Move(moveCounter, chessPieces.get(i).getPiece(), originalPos, finalPos, takenPiece, "ENPASSANT"));
				}
				
				//DEALS WITH PAWN PROMOTION
				else if (chessPieces.get(i).getPiece().contains("WHITEPAWN") && chessPieces.get(i).getRow() == 1) {
					moveHistory.add(new Move(moveCounter, chessPieces.get(i).getPiece(), originalPos, finalPos, takenPiece, (chessPieces.get(i).getPiece() + "PROMOTION")));
					chessPieces.get(i).setPiece("WHITEQUEEN" + chessPieces.get(i).getPiece().replace("WHITEPAWN", ""));
				}
				else if (chessPieces.get(i).getPiece().contains("BLACKPAWN") && chessPieces.get(i).getRow() == 8) {
					moveHistory.add(new Move(moveCounter, chessPieces.get(i).getPiece(), originalPos, finalPos, takenPiece, (chessPieces.get(i).getPiece() + "PROMOTION")));
					chessPieces.get(i).setPiece("BLACKQUEEN" + chessPieces.get(i).getPiece().replace("BLACKPAWN", ""));
				}
				else {
					moveHistory.add(new Move(moveCounter, chessPieces.get(i).getPiece(), originalPos, finalPos, takenPiece));
				}
				
				updateHistoryList(moveHistory);
				
				break;
			}
		}
		
		selectedTile = new int[] {0, 0};
		movedEnPassant = false;
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void updateDialogueBox() {

		String turnText = "";
		String warningText = "";
		Text turn = new Text();
		Text warning = new Text();
		
		if (whiteVictory || blackVictory) {
			if (whiteVictory) { 
				turnText = "WHITE\nWINS!!!";
				turn = new Text(turnText);
				turn.setStyle("-fx-font: 40px Tahoma;\r\n" +  
						"    -fx-fill: white;\r\n" + 
						"    -fx-stroke: black;\r\n" + 
						"    -fx-stroke-width: 1;");
			}
			else if (blackVictory) {
				turnText = "BLACK\nWINS!!!";
				turn = new Text(turnText);
				turn.setStyle("-fx-font: 40px Tahoma;\r\n" +  
						"    -fx-fill: black;\r\n" + 
						"    -fx-stroke: black;\r\n" + 
						"    -fx-stroke-width: 1;");
			}
		}
		else {
		
			if (whiteCheckmate) { warningText = "WHITE CHECKMATE"; }
			else if (blackCheckmate) { warningText = "BLACK CHECKMATE"; }
			else if (whiteCheck) { warningText = "WHITE CHECK"; }
			else if (blackCheck) { warningText = "BLACK CHECK"; }
			
			if (whiteTurn) { turnText = "WHITE'S TURN\n"; }
			else { turnText = "BLACK'S TURN\n"; }
			
			turn = new Text(turnText);
			if (turnText.contains("WHITE")) {
				turn.setStyle("-fx-font: 30px Tahoma;\r\n" +  
						"    -fx-fill: white;\r\n" + 
						"    -fx-stroke: black;\r\n" + 
						"    -fx-stroke-width: 1;");
			}
			else if (turnText.contains("BLACK")) {
				turn.setStyle("-fx-font: 30px Tahoma;\r\n" +  
						"    -fx-fill: black;\r\n" + 
						"    -fx-stroke: black;\r\n" + 
						"    -fx-stroke-width: 1;");
			}
			
			warning = new Text(warningText);
			if (warningText.contains("WHITE")) {
				warning.setStyle("-fx-font: 20px Tahoma;\r\n" +  
						"    -fx-fill: white;\r\n" + 
						"    -fx-stroke: black;\r\n" + 
						"    -fx-stroke-width: 1;");
			}
			else if (warningText.contains("BLACK")) {
				warning.setStyle("-fx-font: 20px Tahoma;\r\n" +  
						"    -fx-fill: black;\r\n" + 
						"    -fx-stroke: black;\r\n" + 
						"    -fx-stroke-width: 1;");
			}
		}
		
		dialogueBox.getChildren().clear();
		
		ObservableList list = dialogueBox.getChildren(); 
		list.addAll(turn, warning);        
		
	}
	
	private static void updateHistoryList(ArrayList<Move> moveHistory) {
		
		movesList.getChildren().clear();
		movesList.getChildren().add(header);
		
		for (int i = moveHistory.size()-1; i >= 0; i--) {

			HBox move = new HBox();
			if (moveHistory.get(i).getTakenPiece().getPiece().equals("---")) {
				move.getChildren().addAll(new Text("" + moveHistory.get(i).getMoveNumber()), new ImageView(moveHistory.get(i).getMiniImage("PIECE")), 
						new Text(moveHistory.get(i).getInitialPos()), new Text(moveHistory.get(i).getFinalPos()),
						new Text(moveHistory.get(i).getTakenPiece().getPiece()));
			}
			else {
				move.getChildren().addAll(new Text("" + moveHistory.get(i).getMoveNumber()), new ImageView(moveHistory.get(i).getMiniImage("PIECE")), 
						new Text(moveHistory.get(i).getInitialPos()), new Text(moveHistory.get(i).getFinalPos()),
						new ImageView(moveHistory.get(i).getMiniImage("TAKEN")));
			}

			move.setSpacing(25);
		    move.setAlignment(Pos.CENTER);
			
			movesList.getChildren().add(move);
			
		}
		
	}

	private static boolean getCheck(String colour) {
		
		String antiColour = "";
		ChessPiece king = new ChessPiece("");
		ArrayList<int[]> pieceMoves = new ArrayList<int[]>();
		
		if (colour.contains("WHITE")) {
			antiColour = "BLACK";
		}
		else {
			antiColour = "WHITE";
		}
			
		for (int i = 0; i < chessPieces.size(); i++) {
			if (chessPieces.get(i).getPiece().contains(colour+"KING")) {
				king = chessPieces.get(i);
				break;
			}
		}
		
		if (king.getPiece().equals("")) {
			return false;
		}
		
		try {
			if (tiles[king.getRow()][king.getColumn()-2].getPiece(chessPieces).contains(antiColour+"PAWN") && colour.contains("BLACK")) {
				return true;
			}
		} catch (Exception e) { }
		try {
			if (tiles[king.getRow()][king.getColumn()].getPiece(chessPieces).contains(antiColour+"PAWN") && colour.contains("BLACK")) {
				return true;
			}
		} catch (Exception e) { }
		try {
			if (tiles[king.getRow()-2][king.getColumn()-2].getPiece(chessPieces).contains(antiColour+"PAWN") && colour.contains("WHITE")) {
				return true;
			}
		} catch (Exception e) { }
		try {
			if (tiles[king.getRow()-2][king.getColumn()].getPiece(chessPieces).contains(antiColour+"PAWN") && colour.contains("WHITE")) {
				return true;
			}
		} catch (Exception e) { }
		
		pieceMoves = getKnightMoves(king);
		
		for (int i = 0; i < pieceMoves.size(); i++) {
			if (tiles[pieceMoves.get(i)[1]-1][pieceMoves.get(i)[0]-1].getPiece(chessPieces).contains(antiColour+"KNIGHT")) {
				return true;
			}
		}
		
		pieceMoves = getRookMoves(king);
		
		for (int i = 0; i < pieceMoves.size(); i++) {
			if (tiles[pieceMoves.get(i)[1]-1][pieceMoves.get(i)[0]-1].getPiece(chessPieces).contains(antiColour+"ROOK")) {
				return true;
			}
		}
		
		pieceMoves = getBishopMoves(king);
		
		for (int i = 0; i < pieceMoves.size(); i++) {
			if (tiles[pieceMoves.get(i)[1]-1][pieceMoves.get(i)[0]-1].getPiece(chessPieces).contains(antiColour+"BISHOP")) {
				return true;
			}
		}
		
		pieceMoves = getQueenMoves(king);
		
		for (int i = 0; i < pieceMoves.size(); i++) {
			if (tiles[pieceMoves.get(i)[1]-1][pieceMoves.get(i)[0]-1].getPiece(chessPieces).contains(antiColour+"QUEEN")) {
				return true;
			}
		}
		
		pieceMoves = getKingMoves(king);
		
		for (int i = 0; i < pieceMoves.size(); i++) {
			if (tiles[pieceMoves.get(i)[1]-1][pieceMoves.get(i)[0]-1].getPiece(chessPieces).contains(antiColour+"KING")) {
				return true;
			}
		}
		
		return false;
	}

	private static boolean getCheckmate(String colour) {
		
		ChessPiece king = new ChessPiece("");
		int kingRow = 0;
		int kingColumn = 0;
		
		ArrayList<int[]> kingMoves = new ArrayList<int[]>();
			
		for (int i = 0; i < chessPieces.size(); i++) {
			if (chessPieces.get(i).getPiece().contains(colour+"KING")) {
				king = chessPieces.get(i);
				kingRow = king.getRow();
				kingColumn = king.getColumn();
				break;
			}
		}
		
		if (king.getPiece().equals("")) {
			return false;
		}
		
		kingMoves = getKingMoves(king);
		
		for (int i = 0; i < kingMoves.size(); i++) {
			king.setPos(kingMoves.get(i)[1], kingMoves.get(i)[0]);
			
			if (getCheck(colour) == false) {
				king.setPos(kingRow, kingColumn);
				return false;
			}
		}
		
		king.setPos(kingRow, kingColumn);
		return true;
	}
	
	private static void gameOver() {
		
		boolean whiteKingTaken = true;
		boolean blackKingTaken = true;

		for (int i = 0; i < chessPieces.size(); i++) {
	
			if (chessPieces.get(i).getPiece().contains("WHITEKING")) {
				whiteKingTaken = false;
			}
			if (chessPieces.get(i).getPiece().contains("BLACKKING")) {
				blackKingTaken = false;
			}
		}
		
		whiteVictory = blackKingTaken;
		blackVictory = whiteKingTaken;
		
	}

	private static void saveGame() throws IOException {
		
		Writer output = null;
    	File file = new File("SavedGame.txt");
    	output = new BufferedWriter(new FileWriter(file));
    	
		for (int i = 0; i < moveHistory.size(); i++) {

			output.write(moveHistory.get(i).getMoveNumber() + "\n");
			output.write(moveHistory.get(i).getPiece() + "\n");
			output.write(moveHistory.get(i).getInitialArrayPos()[0] + "\n");
			output.write(moveHistory.get(i).getInitialArrayPos()[1] + "\n");
			output.write(moveHistory.get(i).getFinalArrayPos()[0] + "\n");
			output.write(moveHistory.get(i).getFinalArrayPos()[1] + "\n");
			output.write(moveHistory.get(i).getTakenPiece().getPiece() + "\n");
			output.write(moveHistory.get(i).getTakenPiece().getRow() + "\n");
			output.write(moveHistory.get(i).getTakenPiece().getColumn() + "\n");
			output.write(moveHistory.get(i).getSpecialMove() + "\n");
	    	
		}

    	output.close();
    	
	}
	
	private static void loadGame() throws FileNotFoundException {
		
		chessPieces = startGame();
		
		Scanner input = new Scanner(new File("SavedGame.txt"));
		
		while (input.hasNextLine()) {

			int moveNumber = input.nextInt();
			input.nextLine();
			String movedPiece = input.nextLine();
			int[] initialPos = {input.nextInt(), input.nextInt()};
			int[] finalPos = {input.nextInt(), input.nextInt()};
			input.nextLine();
			ChessPiece takenPiece = new ChessPiece(input.nextLine(), input.nextInt(), input.nextInt());
			String specialMove = input.nextLine();
			if (input.hasNextLine()) { input.nextLine(); }
			
			undidMoves.add(new Move(moveNumber, movedPiece, initialPos, finalPos, takenPiece, specialMove));
			redoButtonMethod();
			
		}
		input.close();
	}
	
    private static void redoButtonMethod() {
        
    	if (undidMoves.size() > 0) {
    		
			moveCounter++;
			whiteTurn = !whiteTurn;
			
			selectedTile = new int[] {0, 0};
			
    		moveHistory.add(undidMoves.get(undidMoves.size() -1));
        	Move lastMove = moveHistory.get(moveHistory.size()-1);
    		undidMoves.remove(undidMoves.size() -1);   
    	
        	for (int i = 0; i < chessPieces.size(); i++) {
        		if (chessPieces.get(i).getPiece().equals(lastMove.getPiece())) {
        			//PUTS PIECE BACK TO WHERE IT WAS
        			chessPieces.get(i).setPos(lastMove.getFinalArrayPos()[0], lastMove.getFinalArrayPos()[1]);
        			
        			//IF PIECE WAS TAKEN, REMOVE IT
        			if (!lastMove.getTakenPiece().getPiece().equals("---")) {
        				for (int j = 0; j < chessPieces.size(); j++) {
        					if (chessPieces.get(j).getPiece().equals(lastMove.getTakenPiece().getPiece())) {
        						takenPieces.add(chessPieces.get(j));
        						chessPieces.remove(j);
        						break;
        					}
        				}
        				
        				updateTakenPiecesGrid();
        			}
        			
        			updateHistoryList(moveHistory);
        			blackCheck = false;
        			whiteCheck = false;
        			blackCheckmate = false;
        			whiteCheckmate = false;
        			if (getCheck("BLACK")) {
						if (getCheckmate("BLACK")) { blackCheckmate = true; }
						else { blackCheck = true; }
					}
					else {
						blackCheck = false;
						blackCheckmate = false;
					}
					if (getCheck("WHITE")) {
						if (getCheckmate("WHITE")) { whiteCheckmate = true; }
						else { whiteCheck = true; }
					}
					else {
						whiteCheck = false;
						whiteCheckmate = false;
					}
					gameOver();
        			
        			if (undidMoves.size() > 0) {
            			if (lastMove.getMoveNumber() == undidMoves.get(undidMoves.size()-1).getMoveNumber()) {
    	        			whiteTurn = !whiteTurn;
            				redoButtonMethod();
            			}
        			}

        			updateDialogueBox();
        			break;
        		}
        	}

    		//DEALS WITH REDOING PAWN PROMOTIONS
			if (lastMove.getSpecialMove().contains("PROMOTION")) {
				
				String pawnNumber = lastMove.getPiece().replace("WHITEPAWN", "");
				pawnNumber = pawnNumber.replace("BLACKPAWN", "");
				
				for (int i = 0; i < chessPieces.size(); i++) {
					
					String currentMovePawnNumber = chessPieces.get(i).getPiece().replace("WHITEPAWN", "");
					currentMovePawnNumber = currentMovePawnNumber.replace("BLACKPAWN", "");
					
					if (pawnNumber.equals(currentMovePawnNumber) && chessPieces.get(i).getPiece().contains("BLACK") && lastMove.getFinalArrayPos()[0] == 8) {
						chessPieces.get(i).setPiece("BLACKQUEEN" + pawnNumber);
					}
					else if (pawnNumber.equals(currentMovePawnNumber) && chessPieces.get(i).getPiece().contains("WHITE")  && lastMove.getFinalArrayPos()[0] == 1) {
						chessPieces.get(i).setPiece("WHITEQUEEN" + pawnNumber);
					}
				}
			}
    	}
    }

    private static void undoButtonMethod() {
		if (moveHistory.size() > 0) {
            
        	Move recentMove = moveHistory.get(moveHistory.size()-1);
			
        	undidMoves.add(moveHistory.get(moveHistory.size()-1));
			moveHistory.remove(moveHistory.size()-1);
			
			moveCounter--;
			whiteTurn = !whiteTurn;
			
			selectedTile = new int[] {0, 0};
			
			//DEALS WITH UNDOING PAWN PROMOTIONS
			if (recentMove.getSpecialMove().contains("PROMOTION")) {
				
				String pawnNumber = recentMove.getPiece().replace("WHITEPAWN", "");
				pawnNumber = pawnNumber.replace("BLACKPAWN", "");
				
				for (int i = 0; i < chessPieces.size(); i++) {
					
					String currentMovePawnNumber = chessPieces.get(i).getPiece().replace("WHITEQUEEN", "");
					currentMovePawnNumber = currentMovePawnNumber.replace("BLACKQUEEN", "");
					
					if (pawnNumber.equals(currentMovePawnNumber) && chessPieces.get(i).getPiece().contains("BLACK")) {
						chessPieces.get(i).setPiece("BLACKPAWN" + pawnNumber);
					}
					else if (pawnNumber.equals(currentMovePawnNumber) && chessPieces.get(i).getPiece().contains("WHITE")) {
						chessPieces.get(i).setPiece("WHITEPAWN" + pawnNumber);
					}
				}
			}
        	
        	for (int i = 0; i < chessPieces.size(); i++) {
        		if (chessPieces.get(i).getPiece().equals(recentMove.getPiece())) {
        			//PUTS PIECE BACK TO WHERE IT WAS
        			chessPieces.get(i).setPos(recentMove.getInitialArrayPos()[1], recentMove.getInitialArrayPos()[0]);
        			
        			//IF PIECE WAS TAKEN, PUT IT BACK TO WHERE IT WAS
        			if (!recentMove.getTakenPiece().getPiece().equals("---")) {
        				chessPieces.add(new ChessPiece(recentMove.getTakenPiece().getPiece(), recentMove.getTakenPiece().getRow(), recentMove.getTakenPiece().getColumn()));
        				if (takenPieces.size() > 0) { takenPieces.remove(takenPieces.size() - 1); }
        				updateTakenPiecesGrid();
        			}
        			
        			updateHistoryList(moveHistory);
        			blackCheck = false;
        			whiteCheck = false;
        			blackCheckmate = false;
        			whiteCheckmate = false;
        			if (getCheck("BLACK")) {
						if (getCheckmate("BLACK")) { blackCheckmate = true; }
						else { blackCheck = true; }
					}
					else {
						blackCheck = false;
						blackCheckmate = false;
					}
					if (getCheck("WHITE")) {
						if (getCheckmate("WHITE")) { whiteCheckmate = true; }
						else { whiteCheck = true; }
					}
					else {
						whiteCheck = false;
						whiteCheckmate = false;
					}
        			gameOver();
        			
        			if (moveHistory.size() > 1) {
            			if (recentMove.getMoveNumber() == moveHistory.get(moveHistory.size()-1).getMoveNumber()) {
            				moveCounter++;
    	        			whiteTurn = !whiteTurn;
            				undoButtonMethod();
            			}
        			}
        			
        			updateDialogueBox();
        			break;
        		}
        	}
        	
        }
    }

}
