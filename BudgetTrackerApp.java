import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Optional;

public class BudgetTrackerApp extends Application {
    private ArrayList<User> users = new ArrayList<>();
    private User currentUser;
    private UIInterface ui;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Budget Tracker");

        // Create a layout for the main screen
        BorderPane mainLayout = new BorderPane();

        // Create a menu bar
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(event -> exit());
        fileMenu.getItems().add(exitMenuItem);
        menuBar.getMenus().add(fileMenu);

        // Create a tab pane for different functionalities
        TabPane tabPane = new TabPane();

        // Create an "Add Income" tab
        Tab addIncomeTab = new Tab("Add Income");
        VBox addIncomeLayout = createAddIncomeLayout();
        addIncomeTab.setContent(addIncomeLayout);

        // Create an "Add Expense" tab
        Tab addExpenseTab = new Tab("Add Expense");
        VBox addExpenseLayout = createAddExpenseLayout();
        addExpenseTab.setContent(addExpenseLayout);

        // Create a "View Budget" tab
        Tab viewBudgetTab = new Tab("View Budget");
        VBox viewBudgetLayout = createViewBudgetLayout();
        viewBudgetTab.setContent(viewBudgetLayout);

        tabPane.getTabs().addAll(addIncomeTab, addExpenseTab, viewBudgetTab);

        mainLayout.setTop(menuBar);
        mainLayout.setCenter(tabPane);

        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createAddIncomeLayout() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label incomeLabel = new Label("Income Description:");
        TextField incomeDescriptionField = new TextField();

        Label incomeAmountLabel = new Label("Income Amount:");
        TextField incomeAmountField = new TextField();

        Button addIncomeButton = new Button("Add Income");
        addIncomeButton.setOnAction(event -> {
            String description = incomeDescriptionField.getText();
            String amountStr = incomeAmountField.getText();
            if (!description.isEmpty() && isNumeric(amountStr)) {
                double amount = Double.parseDouble(amountStr);
                currentUser.addIncome(new Transaction(description, amount));
                showAlert("Income Added", "Income has been added successfully.");
                incomeDescriptionField.clear();
                incomeAmountField.clear();
            } else {
                showAlert("Error", "Invalid input. Please enter a valid amount.");
            }
        });

        layout.getChildren().addAll(incomeLabel, incomeDescriptionField, incomeAmountLabel, incomeAmountField,
                addIncomeButton);

        return layout;
    }

    private VBox createAddExpenseLayout() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label expenseLabel = new Label("Expense Description:");
        TextField expenseDescriptionField = new TextField();

        Label expenseAmountLabel = new Label("Expense Amount:");
        TextField expenseAmountField = new TextField();

        Button addExpenseButton = new Button("Add Expense");
        addExpenseButton.setOnAction(event -> {
            String description = expenseDescriptionField.getText();
            String amountStr = expenseAmountField.getText();
            if (!description.isEmpty() && isNumeric(amountStr)) {
                double amount = Double.parseDouble(amountStr);
                currentUser.addExpense(new Transaction(description, amount));
                showAlert("Expense Added", "Expense has been added successfully.");
                expenseDescriptionField.clear();
                expenseAmountField.clear();
            } else {
                showAlert("Error", "Invalid input. Please enter a valid amount.");
            }
        });

        layout.getChildren().addAll(expenseLabel, expenseDescriptionField, expenseAmountLabel, expenseAmountField,
                addExpenseButton);

        return layout;
    }

    private VBox createViewBudgetLayout() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        // Display income sources and amounts
        ListView<String> incomeListView = new ListView<>();
        ObservableList<String> incomeItems = FXCollections.observableArrayList(currentUser.getIncomeDescriptions());
        incomeListView.setItems(incomeItems);

        // Display expenses and amounts
        ListView<String> expenseListView = new ListView<>();
        ObservableList<String> expenseItems = FXCollections.observableArrayList(currentUser.getExpenseDescriptions());
        expenseListView.setItems(expenseItems);

        Label totalIncomeLabel = new Label("Total Income: $" + currentUser.calculateTotalIncome());
        Label totalExpenseLabel = new Label("Total Expenses: $" + currentUser.calculateTotalExpenses());
        Label remainingBudgetLabel = new Label("Remaining Budget: $" + currentUser.calculateRemainingBudget());

        layout.getChildren().addAll(new Label("Income Sources:"), incomeListView,
                new Label("Expenses:"), expenseListView,
                totalIncomeLabel, totalExpenseLabel, remainingBudgetLabel);

        return layout;
    }

    private void exit() {
        // Handle any necessary cleanup before exiting the program
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
