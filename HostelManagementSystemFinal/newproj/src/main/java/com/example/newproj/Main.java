    package com.example.newproj;

    import com.example.newproj.controllers.*;

    import javafx.application.Application;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.stage.Stage;

    import java.io.IOException;

    public class Main extends Application {

        private static Stage primaryStage;

        @Override
        public void start(Stage primaryStage) throws IOException {
            this.primaryStage = primaryStage;
            loadLoginScene();  // Load the hosteller scene on application start
        }

        // Method to load the Hosteller Info scene
        public static void loadHostellerScene() {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("hostellerinfo.fxml"));
                Parent root = loader.load();
                primaryStage.setTitle("Hosteller Management System");
                Scene scene = new Scene(root, 1200, 800);
                primaryStage.setScene(scene);
                primaryStage.show();

                // Optional: Access the HostellerInfoController if you need to call methods directly
                HostellerInfoController controller = loader.getController();
                // Perform any controller setup here if needed

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static void loadDashboardScene() {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("dashboard.fxml"));
                Parent root = loader.load();
                primaryStage.setTitle("Hosteller Management System");
                Scene scene = new Scene(root, 1200, 800);
                primaryStage.setScene(scene);
                primaryStage.show();

                // Optional: Access the HostellerInfoController if you need to call methods directly
                DashController controller = loader.getController();
                // Perform any controller setup here if needed

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static void loadLoginScene() {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("login.fxml"));
                Parent root = loader.load();
                primaryStage.setTitle("Hosteller Management System");
                Scene scene = new Scene(root, 1200, 800);
                primaryStage.setScene(scene);
                primaryStage.show();

                // Access the LoginController instead of HostellerInfoController
                LoginController controller = loader.getController();
                // Perform any controller setup here if needed

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Method to load the Staff Info scene
        public static void loadStaffScene() {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("staffinfo.fxml"));
                Parent root = loader.load();
                primaryStage.setTitle("Hosteller Management System");
                Scene scene = new Scene(root, 1200, 800);
                primaryStage.setScene(scene);
                primaryStage.show();

                // Access the LoginController instead of HostellerInfoController
                StaffInfoController controller = loader.getController();
                // Perform any controller setup here if needed

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static void loadFeeScene() {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("fee.fxml"));
                Parent root = loader.load();
                primaryStage.setTitle("Hosteller Management System");
                Scene scene = new Scene(root, 1200, 800);
                primaryStage.setScene(scene);
                primaryStage.show();

                // Access the LoginController instead of HostellerInfoController
                feeController controller = loader.getController();
                // Perform any controller setup here if needed

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static void loadAttendanceScene() {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("attendance.fxml"));
                Parent root = loader.load();
                primaryStage.setTitle("Hosteller Management System");
                Scene scene = new Scene(root, 1200, 800);
                primaryStage.setScene(scene);
                primaryStage.show();

                // Access the LoginController instead of HostellerInfoController
                AttendanceController controller = loader.getController();
                // Perform any controller setup here if needed

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        public static void loadAllotRoomsScene() {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("allotRooms.fxml"));
                Parent root = loader.load();
                primaryStage.setTitle("Hosteller Management System");
                Scene scene = new Scene(root, 1200, 800);
                primaryStage.setScene(scene);
                primaryStage.show();

                // Access the LoginController instead of HostellerInfoController
                AllotRoomsController controller = loader.getController();
                // Perform any controller setup here if needed

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static void loadComplaintsScene() {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("complaints.fxml"));
                Parent root = loader.load();
                primaryStage.setTitle("Hosteller Management System");
                Scene scene = new Scene(root, 1200, 800);
                primaryStage.setScene(scene);
                primaryStage.show();

                // Access the LoginController instead of HostellerInfoController
                ComplaintsController controller = loader.getController();
                // Perform any controller setup here if needed

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static void main(String[] args) {
            launch(args);
        }
    }
