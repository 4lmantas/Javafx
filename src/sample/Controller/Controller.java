package sample.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.Model.Football;
import sample.Model.FootballDAO;
import sample.Model.User;
import sample.Model.UserDAO;
import sample.Utils.Validation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {

    @FXML
    private TextField password;
    @FXML
    private TextField username;
    @FXML
    private Label error;
    @FXML
    private Button close;
    @FXML
    private TextField regUser;
    @FXML
    private PasswordField regPassw;
    @FXML
    private PasswordField regConfPassw;
    @FXML
    private TextField regEmail;
    @FXML
    private Label regError;
    @FXML
    private CheckBox SecondDivision;
    @FXML
    private CheckBox LastDivision;
    @FXML
    private CheckBox FirstDivision;
    @FXML
    private RadioButton Real;
    @FXML
    private RadioButton fake;
    @FXML
    private RadioButton futsal;
    @FXML
    private ComboBox comboNum;
    @FXML
    private TextField TeamNameField;
    @FXML
    private TextField ManagerField;
    @FXML
    private Button create;
    @FXML
    private Label warning;
    @FXML
    private TableView table;
    @FXML
    private TextField id;
    @FXML
    private CheckBox admin;
    @FXML
    private Button update;
    @FXML
    private Button delete;
    @FXML
    private Label logname;
    @FXML
    private Label role;

    ResultSet rsAllEntries;
    ObservableList<ObservableList> data = FXCollections.observableArrayList();

    public void login(ActionEvent event) {
        if (Validation.isValidUsername(username.getText()) && Validation.isValidPassword(password.getText())) {
            UserDAO userDAO = new UserDAO();
            String msg = userDAO.login(username.getText(), password.getText());
            if (msg.contains("Successful")) {
                User user = userDAO.getUser(username.getText());
                dashboard(event, user);
            } else {
                error.setText(msg);
            }

        } else {
            error.setText("Wrong user name or password!");
        }
        error.setVisible(true);
    }

    public void logOut(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root, 450, 350));
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeWindow(ActionEvent event) {
        if (event.getSource() == close) {
            System.exit(0);
        }
    }

    public void register(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../view/register.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Register");
            stage.setScene(new Scene(root, 450, 350));
            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerLogin(ActionEvent event) {
        boolean isRegistered = true;

        regError.setText("");
        if (!Validation.isValidUsername(regUser.getText())) {
            regError.setText("Username is incorrect (letters and numbers only, at least 5 char)");
            isRegistered = false;
        } else if (!Validation.isValidPassword(regPassw.getText())) {
            regError.setText("Password is incorrect (letters and numbers only, at least 5 char)");
            isRegistered = false;
        } else if (!regConfPassw.getText().equals(regPassw.getText())) {
            regError.setText("Password doesn't match");
            isRegistered = false;
        } else if (!Validation.isValidEmail(regEmail.getText())) {
            regError.setText("Email is not correct, pattern- football@gmail.com");
            isRegistered = false;
        }

        if (isRegistered) {
            User user = new User(regUser.getText(), regPassw.getText(), regEmail.getText(),admin.isSelected());
            UserDAO userDAO = new UserDAO();
            String msg = userDAO.register(user);
            if (msg.contains("successfully")) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Login");
                    stage.setScene(new Scene(root, 450, 350));
                    stage.show();
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                regError.setText(msg);
            }
        }
    }

    public void dashboard(ActionEvent event, User user) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../view/dashboard.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(root, 1274, 850));

            Label lblLoginName = (Label) root.lookup("#logname");
            Label lblLoginRole = (Label) root.lookup("#role");
            if (lblLoginName != null) lblLoginName.setText(user.getUsername());
            if (lblLoginRole != null) lblLoginRole.setText(user.isAdmin() ? "Admin" : "User");

            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create() {
        String title = TeamNameField.getText();
        String Manager = ManagerField.getText();

        String genre = "";
        if (SecondDivision.isSelected()) {
            genre += SecondDivision.getText() + ",";
        }
        if (LastDivision.isSelected()) {
            genre += LastDivision.getText() + ",";
        }
        if (FirstDivision.isSelected()) {
            genre += FirstDivision.getText() + ",";
        }

        String platform = "";
        if (fake.isSelected()) {
            platform += fake.getText();
        } else if (futsal.isSelected()) {
            platform += futsal.getText();
        } else if (Real.isSelected()) {
            platform += Real.getText();
        }
        String rating = "";
        if (!comboNum.getSelectionModel().isEmpty()) {
            rating = comboNum.getSelectionModel().getSelectedItem().toString();
        } else {
            warning.setText("Please check rating");
        }

        if (!Validation.isValidTitle(title)) {
            warning.setText("TeamName Required");
        } else if (!Validation.Manager(Manager)) {
            warning.setText("Manager Required");
        } else {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUser(logname.getText());
            Football football = new Football(title, Manager, Integer.parseInt(rating), platform, genre, user.getId());
            FootballDAO footballDAO = new FootballDAO();
            String msg = footballDAO.add(football);
            warning.setText(msg);

            updateTableFromDB("");
        }
    }

    public void search() {
        updateTableFromDB(TeamNameField.getText());
    }

    public void updateTableFromDB(String title) {
        FootballDAO footballDAO = new FootballDAO();
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUser(logname.getText());
        try {
            rsAllEntries = footballDAO.searchByTitle(title, user);
        } catch (NullPointerException e) {
            warning.setText("No rows to display");
        }
        fetchColumnList();
        fetchRowList();
    }

    public void update() {
        if(role.getText().equals("Admin")) {
            String userId = id.getText();
            int TeamNameid = (Integer.parseInt(id.getText()));
            String TeamName = TeamNameField.getText();
            String Manager = ManagerField.getText();

            String genre = "";
            if (SecondDivision.isSelected()) {
                genre += SecondDivision.getText() + ",";
            }
            if (LastDivision.isSelected()) {
                genre += LastDivision.getText() + ",";
            }
            if (FirstDivision.isSelected()) {
                genre += FirstDivision.getText() + ",";
            }

            String platform = "";
            if (fake.isSelected()) {
                platform += fake.getText();
            } else if (futsal.isSelected()) {
                platform += futsal.getText();
            } else if (Real.isSelected()) {
                platform += Real.getText();
            }
            String rating = "";
            if (!comboNum.getSelectionModel().isEmpty()) {
                rating = comboNum.getSelectionModel().getSelectedItem().toString();
            } else {
                warning.setText("Please check number of players");
            }

            if (!Validation.isValidTitle(TeamName)) {
                warning.setText("Team Name Required");
            } else if (!Validation.Manager(Manager)) {
                warning.setText("Manager Required");
            } else {
                Football football = new Football(TeamNameid, TeamName, Manager, Integer.parseInt(rating), platform, genre, Integer.parseInt(userId));
                FootballDAO footballDAO = new FootballDAO();
                footballDAO.editById(football);

                updateTableFromDB("");
            }
        } else {
            warning.setText("Update feature is only for admins");
        }
    }

    public void delete() {
        if (role.getText().equals("Admin")) {
            FootballDAO footballDAO = new FootballDAO();
            footballDAO.deleteById(Integer.parseInt((String) (id.getText())));

            updateTableFromDB(""); // get all entries after entry delete
        } else {
            warning.setText("Delete feature is only for admins");
        }
    }

    private void fetchColumnList() {
        try {
            table.getColumns().clear();

            if (rsAllEntries != null) {
                for (int i = 0; i < rsAllEntries.getMetaData().getColumnCount(); i++) {
                    final int j = i;
                    TableColumn col = new TableColumn(rsAllEntries.getMetaData().getColumnName(i + 1).toUpperCase());
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });

                    table.getColumns().removeAll(col);
                    table.getColumns().addAll(col);
                }
            } else {
                warning.setText("No columns to display");
            }
        } catch (SQLException e) {
            warning.setText("Failure in getting all entries");
        }
    }

    private void fetchRowList() {
        try {
            data.clear();
            if (rsAllEntries != null) {
                while (rsAllEntries.next()) {
                    ObservableList row = FXCollections.observableArrayList();
                    for (int i = 1; i <= rsAllEntries.getMetaData().getColumnCount(); i++) {
                        row.add(rsAllEntries.getString(i));
                    }
                    data.add(row);
                }
                table.setItems(data);
            } else {
                warning.setText("No rows to display");
            }
        } catch (SQLException ex) {
            warning.setText("Failure in getting all entries");
        }
    }


}
