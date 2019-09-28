package connectfour;

/**
 * @File	ConnectFour.java
 * @Course	JAC 444 SAB
 * @Author	Vincent Terpstra
 * @ID     	140665175
 * @Date   	March 14, 2019
 * @Purpose Workshop 5 Task 3
 * @Class ConnectFour.java 
 * 		User interface for an Connect four game
 * @Declaration This assignment represents my own work in accordance with Seneca Academic Policy
 */

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ConnectFour extends Application {
	public static void main(String[] args){ launch(args); }
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		MyScene scene = new MyScene(600, 500);
			(indicator = scene.add(new MyCircle(){
				@Override
				void player(int player){
					if(player == Board.AI && indicator.isVisible()){
						int col = board.BackTrackAI(Board.AI);
						int row = board.addPiece(col, Board.AI);
						playPiece(col, row, Board.AI);
						
					}
					super.player(Board.PLAYER);
				}
			}, 300, 40)).player(Board.PLAYER);
			reset = scene.add(new Button(), 200, 10);
			reset.setOnAction(event ->resetGame());
			reset.setMinWidth(200);
				
		Rectangle rect = scene.add( new Rectangle(490, 420), 55, 60);
		rect.setFill(Color.LIGHTBLUE);
		rect.setOnMouseMoved(
			event->{//align the player indicator at the top of the screen with the mouse
					indicatorIdx = ((int)event.getSceneX() - 55) / 70;
					indicator.setLayoutX( indicatorIdx * 70 + 90);
				}
			);
		scene.setOnMouseClicked(
			event->{
				//add a piece to the column where the player clicked
				int row = board.addPiece(indicatorIdx, indicator.type);
				if(indicator.isVisible() && row != -1){
					playPiece(indicatorIdx, row, indicator.type);
					indicator.player((indicator.type+1) % 2);
				}
			}
		);
		
		board = new Board();
		
		circles = new MyCircle[7][6];
		for(int column = 0; column < 7; ++column){
			for(int row = 0; row < 6; ++ row){
					circles[column][5 - row] = scene.add(new MyCircle(), 90 + 70 * column, 95 + 70 * row );
			}
		}
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Connect Four");
		primaryStage.show();
		primaryStage.setResizable(false);
		resetGame();
		
		/**
		 * TEST AIvAI
		 *
		 */
		int start = 0;
		while(indicator.isVisible()){
			int plr = start ++ % 2;
			int col = board.BackTrackAI(plr);
			int row = board.addPiece(col, plr);
			playPiece(col, row, plr);
		}
		/**/
	}
	
	
	void playPiece(int col, int row,int type){
		circles[col][row].player(type);
		boolean check = board.checkWin(col, row, type);
		if(check)
			reset.setText((type == Board.PLAYER ? "Player" : "Computer") + " wins!");
			
		if(check || board.endGame()){
			indicator.setVisible(false);
			reset.setVisible(true);
		}
	}
	
	MyCircle indicator;
	MyCircle[][] circles;
	
	Button reset;
	Board board;
	
	private int indicatorIdx;
	
	/**
	 * Reset the Game back to it's starting conditions
	 */
	int StartingPlayer = 0;
	private void resetGame() {
		indicator.setVisible(true);
		reset.setVisible(false);
		reset.setText("Game Over!");
		board.reset();
		for(int column = 0; column < 7; ++ column){
			for(int row = 0; row < 6; ++ row){
				circles[column][row].reset();
			}
		}
		indicator.player(StartingPlayer++ % 2);
	}
	
	
	class MyScene extends Scene {
		public MyScene(double x, double y){
			this(new Group(), x, y);
		}
		public MyScene(Group root, double x, double y) {
			super(root, x, y);
			this.group = root;
		}
		final Group group;
		
		public Text addText(String input, double x, double y){
			Text text = new Text(input);
			return add(text, x, y);
		}
		<T extends Node>
		T add(T node, double x, double y){
			node.setLayoutX(x);
			node.setLayoutY(y);
			group.getChildren().add(node);
			return node;
		}
	}
}
