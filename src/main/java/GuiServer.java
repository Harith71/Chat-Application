
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application{

	
	TextField s1,s2,s3,s4, c1;
	Button serverChoice,clientChoice,b1;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox;
	Scene startScene;
	BorderPane startPane;
	Server serverConnection;
	Client clientConnection;
	
	ListView<String> listItems, listItems2, clientList;
	ArrayList<Integer> selectedClients;
	ArrayList<String> clientPos = new ArrayList<>();
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("The Networked Client/Server GUI Example");
		this.serverChoice = new Button("Server");
		this.serverChoice.setStyle("-fx-pref-width: 300px");
		this.serverChoice.setStyle("-fx-pref-height: 300px");
		ObservableList<String> list = FXCollections.observableArrayList();
		listItems = new ListView<String>();
		listItems2 = new ListView<String>();
		clientList = new ListView<String>();

		clientList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
											primaryStage.setTitle("This is the Server");
				serverConnection = new Server(data -> {
					Platform.runLater(()->{
						listItems.getItems().add(data.toString());
					});

				});
											
		});
		
		
		this.clientChoice = new Button("Client");
		this.clientChoice.setStyle("-fx-pref-width: 300px");
		this.clientChoice.setStyle("-fx-pref-height: 300px");
		
		this.clientChoice.setOnAction(e-> {primaryStage.setScene(sceneMap.get("client"));
											primaryStage.setTitle("This is a client");
											clientConnection = new Client(
													data->{Platform.runLater(()->{listItems2.getItems().add(data.toString());});},
													data->{
														Platform.runLater(()->{
															clientList.getItems().add(data.toString());
															clientPos.add(data.toString());
													});},
													data->{
														Platform.runLater(()->{
															String tmp = data.toString().substring(0, 9);;
															int pos = clientPos.indexOf(tmp);
															clientPos.remove(pos);
															clientList.getItems().remove(pos);
														});}
											);
							
											clientConnection.start();
		});


		this.clientList.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				selectedClients = new ArrayList<>();
				ObservableList<String> selectedItems =  clientList.getSelectionModel().getSelectedItems();

				for(String s : selectedItems){
					String cNum = s.substring(s.length()-1, s.length());
					int cN = Integer.parseInt(cNum, 10);
					selectedClients.add(cN);
				}

			}

		});



		
		this.buttonBox = new HBox(400, serverChoice, clientChoice);
		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setCenter(buttonBox);
		
		startScene = new Scene(startPane, 800,800);
		

		
		c1 = new TextField();
		b1 = new Button("Send");
		b1.setOnAction(e->{
			messageObject msg = new messageObject();
			msg.message = c1.getText();
			msg.clients = selectedClients;
			clientConnection.send(msg);
			c1.clear();
		});
		
		sceneMap = new HashMap<String, Scene>();
		
		sceneMap.put("server",  createServerGui());
		sceneMap.put("client",  createClientGui());
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		
		 
		
		primaryStage.setScene(startScene);
		primaryStage.show();
		
	}
	
	public Scene createServerGui() {
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: coral");
		
		pane.setCenter(listItems);
	
		return new Scene(pane, 500, 400);
		
		
	}
	
	public Scene createClientGui() {
		Label slInfo = new Label("Select Client to send msg to by clicking on client");
		Label slInfo2 = new Label("or Shift clicking to select multiple.");
		slInfo2.setStyle("-fx-text-fill: White");
		slInfo.setStyle("-fx-text-fill: White");
		clientBox = new VBox(10, c1,b1,listItems2, slInfo, slInfo2,  clientList);
		clientBox.setStyle("-fx-background-color: blue");
		return new Scene(clientBox, 400, 700);
		
	}

}
