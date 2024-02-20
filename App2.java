import java.util.*;
import java.util.concurrent.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.geometry.*;

public class App2 extends Application {
  
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    /* 
     * Creates a timer for printing to Console for debugging
     * Creates a handler to shut down the timer when the scene is closed.
     * Adds the handler to the stage and shows the stage
     */
    Timer timer = new Timer();
    javafx.event.EventHandler<WindowEvent> handler = new javafx.event.EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent event) {
        if(WindowEvent.WINDOW_HIDDEN.equals(event.getEventType())) {
          timer.cancel();
          event.consume();
        } 
      }
    };
    primaryStage.setOnHidden(handler);
    primaryStage.show();
    
    // Sets the stage title
    primaryStage.setTitle("United States Voting Data");
    
    // Creates Pane's for Stage
    StackPane stackPane = new StackPane();
    AnchorPane anchorPane = new AnchorPane();
    BorderPane borderPane = new BorderPane(anchorPane);
    
    // Adds a VBox to the left side of the screen
    VBox vBox = new VBox(20.0);
    anchorPane.getChildren().add(vBox);
    AnchorPane.setLeftAnchor(vBox, 0.0);
    
    // Adds the Menu Text to top of Screen
    Text text = new Text("Welcome to the United States Voting Data Search Application");
    anchorPane.getChildren().add(text);
    AnchorPane.setTopAnchor(text, 0.0);
    
    // Adds Search Box to the top of the screen
    TextField textField = new TextField("Search");
    anchorPane.getChildren().add(textField);
    AnchorPane.setBottomAnchor(textField, 15.0+text.getLayoutY());
    
    // Adds the Button to the right of the screen
    Button searchBtn = new Button();
    anchorPane.getChildren().add(searchBtn);
    AnchorPane.setRightAnchor(searchBtn, 0.0);
    
    // Adds the Button to the Left of the screen
    Button extraBtn = new Button();
    anchorPane.getChildren().add(extraBtn);
    AnchorPane.setLeftAnchor(extraBtn, 0.0);
    
    // Creates a scene to connect the Panes and the stage.
    Scene primaryScene = new Scene(borderPane);
    primaryStage.setScene(primaryScene);
    
    // Prints info about onscreen objects
    TimerTask timerTask = new TimerTask() {
      @Override
      public void run() {
        System.out.println("Stage\t\tX:" + primaryStage.getX() + "\tY:" + primaryStage.getY());
        System.out.println("AnchorPane\tX:" + anchorPane.getLayoutX() + "\tY:" + anchorPane.getLayoutY());
        System.out.println("Text\t\tX:" + text.getLayoutX() + "\tY:" + text.getLayoutY());
        System.out.println("TextField\tX:" + textField.getLayoutX() + "\tY:" + textField.getLayoutY());
        System.out.println("BorderPane\tX:" + borderPane.getLayoutX() + "\tY:" + borderPane.getLayoutY());
        System.out.println("VBox\t\tX:" + vBox.getLayoutX() + "\tY:" + vBox.getLayoutY());
      }
    };
    timer.scheduleAtFixedRate(timerTask, 0, 5000);
  }

}
