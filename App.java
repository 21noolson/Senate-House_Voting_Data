import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

  // Backend Interface
  private static DataProcessor backend = new DataProcessor();
  private static ObservableList<Member> members;
  private static ObservableList<String> memberNames = FXCollections.observableArrayList();

  @Override
  public void start(Stage primaryStage) throws FileNotFoundException {
    // Loads the data on the backend
    DataProcessor.loadData();
  
    primaryStage.setTitle("Congressional Data Viewer");

    VBox root = new VBox();
    root.setPadding(new Insets(15));
    root.setSpacing(10);

    // Welcoming text
    Label welcomeText = new Label("Welcome to the Congressional Data Viewer!");

    // Search bar and button
    TextField searchField = new TextField();
    searchField.setPromptText("Search for a politician...");
    Button searchButton = new Button("Search");
    
    searchButton.setOnMouseReleased(new SearchEvent());

    HBox searchBox = new HBox();
    searchBox.setSpacing(10);
    searchBox.getChildren().addAll(searchField, searchButton);

    // Filters for party and government body
    ComboBox<String> partyFilter = new ComboBox<>();
    partyFilter.setPromptText("Select Party");
    ObservableList<String> parties = FXCollections.observableArrayList("Democrat", "Republican", "Independent");
    partyFilter.setItems(parties);

    ComboBox<String> bodyFilter = new ComboBox<>();
    bodyFilter.setPromptText("Select Government Body");
    ObservableList<String> bodies = FXCollections.observableArrayList("House of Representatives", "Senate");
    bodyFilter.setItems(bodies);

    HBox filtersBox = new HBox();
    filtersBox.setSpacing(10);
    filtersBox.getChildren().addAll(partyFilter, bodyFilter);

    // List of members
    ListView<String> membersList = new ListView<>();
    membersList.setItems(memberNames);

    // Add all components to the root
    root.getChildren().addAll(welcomeText, searchBox, filtersBox, membersList);

    Scene scene = new Scene(root, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
  
  private class SearchEvent implements EventHandler<MouseEvent> {
    @Override
    public void handle(MouseEvent arg0) {
      members = FXCollections.observableArrayList(DataProcessor.getMembers());
      System.out.println("Event Handler; members returned = " + members.size());
      memberNames.clear(); // Clear existing member names
      for (Member member : members) {
        memberNames.add(member.getName());
        System.out.println(member.getName());
      }
    }
  }
}
