package medfind;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import medfind.models.MedDetail;
import medfind.models.MedItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main extends Application {

    ObservableList<MedItem> entries = FXCollections.observableArrayList();
    ObservableList<MedDetail> transcripts = FXCollections.observableArrayList();
    ListView<MedItem> list = new ListView<>();
    TableView<MedDetail> table = new TableView<>();
    TextField txt = new TextField();
    TextField sum = new TextField();
    VBox root = new VBox();


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        primaryStage.setTitle("Simple Search");

        txt.setPromptText("Search");
        txt.textProperty().addListener((observable, oldVal, newVal) -> search(oldVal, newVal));
        readMeds();
        list.setMaxHeight(180);
        list.setOnMouseClicked(this::selectMed);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setSpacing(2);
        list.setVisible(false);
        root.getChildren().addAll(txt, list, table, sum);


        TableColumn name = new TableColumn("Medicine Name");
        name.setCellValueFactory(new PropertyValueFactory<>(""));
        TableColumn uPrice = new TableColumn("Unit Price");
        TableColumn amount = new TableColumn("Amount");
        TableColumn subTotal = new TableColumn("Sub Total");

        table.getColumns().addAll(name, uPrice, amount, subTotal    );


        table.setItems(transcripts);


        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public void search(String oldVal, String newVal) {

        if (oldVal != null && (oldVal.isEmpty() || newVal.length() < oldVal.length())) {
            list.setItems(entries);
        }

        String value = newVal.toUpperCase();
        ObservableList<MedItem> subentries = FXCollections.observableArrayList();
        for (MedItem entry : list.getItems()) {
            boolean match = true;
            if (entry.getName().contains(value) || entry.getAbbr().contains(value)) {
                subentries.add(entry);
            }
        }

        if (!subentries.isEmpty()) {
            list.setVisible(true);
        }
        list.setItems(subentries);

        sum.setText(calculateSum(transcripts));
    }

    public void selectMed(MouseEvent event) {

        MedItem item = list.getSelectionModel().getSelectedItem();
        boolean existedItem = false;
        for(MedDetail detail:transcripts){
            if(detail.getItem().equals(item)){
                detail.setAmount(detail.getAmount() + 1);
                existedItem = true;
                break;
            }
        }
        if(!existedItem) {
            transcripts.add(new MedDetail(item, 1));
        }
        list.getItems().clear();
        list.setVisible(false);
        txt.clear();
    }

    public String calculateSum(List<MedDetail> transcripts){
        BigDecimal sum = transcripts.stream().map(MedDetail::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.toString();
    }

    public void readMeds() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("data/meds.csv"));
        sc.useDelimiter("\n");
        while (sc.hasNext()) {
            String line = sc.next();
            String[] columns = line.split(","   );
            entries.add(new MedItem(columns[1], columns[0], new BigDecimal(columns[2])));

        }
        sc.close();
    }
}
