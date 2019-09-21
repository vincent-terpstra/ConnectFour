package connectfour;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MyCircle extends Circle {
	int type;
	public MyCircle(){
		super(30);
		setStroke(Color.BLACK);
	}
	
	void reset(){
		type = Board.EMPTY;
		setFill(Color.GHOSTWHITE);
	}
	
	void player(int type){
		this.type = type;
		setFill(type == Board.PLAYER ? Color.GOLDENROD : Color.CRIMSON);
	}
}