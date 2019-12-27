package impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apfloat.Apfloat;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Gui extends Application{
	
	static boolean debug = false;
	
	public void start(Stage s) {
		FlowPane pane = new FlowPane();
		s.setTitle("Non-linear integer factorizer");
		//1392255237301
		//1559649500291
		//3049587890245871
		//937214189897372839
		//942389286244982743
		//139225523730145978202
		//924967921263797227
		//1304817946291

		Apfloat test1 = new Apfloat(5.035987356983756987250897);
		Apfloat test2 = new Apfloat(5.035987356983756987250898);
		TextField n = new TextField("1559649500291,d");
		Label text = new Label("Type number to be factorized: ");
		TextArea output = new TextArea();
		StringBuilder consoleText = new StringBuilder();
		Button calculate = new Button("factorize");
		output.setText(consoleText.toString());
		output.setEditable(false);
		output.setPrefSize(400, 300);
		FlowPane.setMargin(output,new Insets(10));
		pane.setPadding(new Insets(50));
		
		pane.getChildren().addAll(text,n,calculate,output);
		
		calculate.setOnAction(e -> init(n,output));
		
		Scene main = new Scene(pane,500,470);
		s.setScene(main);
		s.show();
	}

	private static void init(TextField t, TextArea a) {

		
		try {			
			if(t.getText().contains(".")) {
				throw new NumberFormatException();
			}
			if(t.getText().substring(t.getText().length()-2).equals(",d")) {
				debug = true;
				a.appendText("factors of n = " + t.getText().substring(0,t.getText().length()-2) + ": " + "\n" + NlfCore.start(t.getText().substring(0,t.getText().length()-2),debug)+"\n");
			}
			if(!debug) {
				a.appendText("factors of n = " + t.getText() + ": " + "\n" + NlfCore.start(t.getText(),debug)+"\n");
			}
			//a.appendText(NlfCore.factorize(t.getText())+"\n");
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
			a.appendText("illegal input!");
			double s = 23049829402956829409529458924563948578789087903452567246746775674678678359829458.0;
			//BigDecimal d = new BigDecimal(String.valueOf(s));
			BigDecimal d = new BigDecimal(0.2304);//9829402956829409529458924563948578789087903452567246746775674678678359829458);
			d.setScale(100,RoundingMode.FLOOR);
			System.out.println(d.toString());
			String myString = String.valueOf(s);//NumberFormat.getInstance().format(s);
			System.out.println(String.format("%.70f", s));
			System.out.println(s);
			//a.appendText("\n"+String.valueOf(s==8*Math.pow(10, -50)));
			
		}
	}
	
	public static void main(String[] args) {
		launch(args);

	}

}
