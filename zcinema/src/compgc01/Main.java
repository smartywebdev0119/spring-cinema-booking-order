package compgc01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The main class for our cinema booking management application.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 02.12.2017
 * Uses open-source icons made by http://www.jensd.de/wordpress/.
 */
public class Main extends Application {

    private static Parent root;
    private static Stage primaryStage;
    static Main m = null;
    static User currentUser;
    private static Boolean employeeMode = false;

    // arrayLists to be populated with the information from the text files
    private static ArrayList<Employee> employees = new ArrayList<Employee>();
    private static ArrayList<Customer> customers = new ArrayList<Customer>();
    private static ArrayList<Film> films = new ArrayList<Film>();
    private static ArrayList<Booking> bookings = new ArrayList<Booking>();

    public static void main(String[] args) {

        m = new Main();

        //        playMusic("wonderful_world.mp3");

        // if files do not exist, create them using default values
        try {
            if (!(new File(URLDecoder.decode(getPath() + "res/employeesJSON.txt", "UTF-8")).exists()))
                createJSONUserFile("employees");
            if (!(new File(URLDecoder.decode(getPath() + "res/customersJSON.txt", "UTF-8")).exists()))
                createJSONUserFile("customers");
            if (!(new File(URLDecoder.decode(getPath() + "res/filmsJSON.txt", "UTF-8")).exists()))
                createJSONUserFile("films");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        launch(args);
    }

    public static ArrayList<Employee> getEmployeeList() {

        return employees;
    }

    public static ArrayList<Customer> getCustomerList() {

        return customers;
    }

    public static ArrayList<Film> getFilmList() {

        return films;
    }

    public static void resetEmployeeList() {

        employees.clear();
    }

    public static void resetCustomerList() {

        customers.clear();
    }

    public static void resetFilmList() {

        films.clear();;
    }

    public static void resetBookingList() {

        bookings.clear();;
    }
    
    public static Main getMainApplication() {

        return m;
    }

    public static JSONObject readJSONUserFile(String file) {

        JSONObject items = null;

        try {
            String path = getPath();

            path = URLDecoder.decode(path + "res/" + file, "UTF-8");

            JSONParser parser = new JSONParser();
            items =  (JSONObject) parser.parse(new FileReader(path));
            for (Object s : items.keySet()) {
                // System.out.println((String) s);
                JSONObject user = (JSONObject) items.get(s);

                if (file.contains("employees"))
                    employees.add( new Employee ((String) user.get("firstName"), (String) user.get("lastName"), (String) user.get("username"), (String) user.get("password"), (String) user.get("email")));
                else if (file.contains("customers"))
                    customers.add( new Customer ((String) user.get("firstName"), (String) user.get("lastName"), (String) user.get("username"), (String) user.get("password"), (String) user.get("email"), Double.parseDouble(String.valueOf(user.get("accountBalance")))));
                else if (file.contains("films"))
                    films.add( new Film ((String) user.get("title"), (String) user.get("description"), (String) user.get("startDate"), (String) user.get("endDate"), (String) user.get("time")));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    public static void modifyJSONFile(String file, String identifier, String attribute, String newValue) {

        try {
            JSONObject items = readJSONUserFile(file);
            JSONObject itemToEdit = (JSONObject) items.get(identifier);
            itemToEdit.put(attribute, newValue);

            String path = URLDecoder.decode(getPath() + "res/" + file, "UTF-8");
            // System.out.println(path);

            File jsonFile = new File(path);
            PrintWriter writer = new PrintWriter(jsonFile);
            writer.print(items.toJSONString());
            writer.close();

            // System.out.println("-- updated file successfully --");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void createJSONUserFile(String type) {

        try {
            // Creating JSON files
            JSONObject items = new JSONObject();

            if (type.equals("films")) {
                JSONObject titanic = new JSONObject();
                titanic.put("title", "Titanic");
                titanic.put("description", "A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, ill-fated R.M.S. Titanic.");
                titanic.put("startDate", "2017-12-08");
                titanic.put("endDate", "2017-12-14");
                titanic.put("time", "20:00");
                items.put("Titanic", titanic);
            }
            else {
                JSONObject filip = new JSONObject();
                filip.put("username", "filip");
                filip.put("firstName", "Filippos");
                filip.put("lastName", "Zofakis");
                filip.put("email", "filippos.zofakis.17@ucl.ac.uk");
                if (type.equals("employees"))
                    filip.put("password", "e");
                else {
                    filip.put("password", "c");
                    filip.put("accountBalance", 1000);
                }
                items.put("filip", filip);

                JSONObject lucio = new JSONObject();
                lucio.put("username", "lucio");
                lucio.put("firstName", "Lucio");
                lucio.put("lastName", "D'Alessandro");
                lucio.put("email", "lucio.d'alessandro.17@ucl.ac.uk");
                if (type.equals("employees"))
                    lucio.put("password", "e");
                else {
                    lucio.put("password", "c");
                    lucio.put("accountBalance", 1000);
                }
                items.put("lucio", lucio);

                JSONObject ghita = new JSONObject();
                ghita.put("username", "ghita");
                ghita.put("firstName", "Ghita");
                ghita.put("lastName", "K Mostefaoui");
                ghita.put("email", "g.kouadri@ucl.ac.uk");
                if (type.equals("employees"))
                    ghita.put("password", "e");
                else {
                    ghita.put("password", "c");
                    ghita.put("accountBalance", 1000000);
                }
                items.put("ghita", ghita);            
            }

            // System.out.println(items.toJSONString());

            String path = getPath();

            if (type.equals("employees"))
                path = URLDecoder.decode(path + "res/employeesJSON.txt", "UTF-8");
            else if (type.equals("customers"))
                path = URLDecoder.decode(path + "res/customersJSON.txt", "UTF-8");
            else if (type.equals("films"))
                // System.out.println(path);
                path = URLDecoder.decode(path + "res/filmsJSON.txt", "UTF-8");

            File file = new File(path);
            PrintWriter writer = new PrintWriter(file);
            writer.print(items.toJSONString());
            writer.close();
        }
        catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void playMusic(String musicFile) {

        try {
            File file = new File(URLDecoder.decode(getPath() + "res/sounds/" + musicFile, "UTF-8"));
            Media backgroundMusic = new Media(file.toPath().toUri().toString());
            MediaPlayer player = new MediaPlayer(backgroundMusic);
            player.play();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String getPath() {

        String path = ClassLoader.getSystemClassLoader().getResource(".").getPath();

        // leave this here to run in Eclipse... if proper deployment then
        // remove code to only run from jar file
        if (path.contains("zcinema/bin"))
            path = path.split("zcinema")[0];

        return path;
    }

    public static User getCurrentUser() {

        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {

        Main.currentUser = currentUser;
    }

    public static boolean isEmployee() {
        return employeeMode;
    }

    public static void setEmployeeMode(boolean employeeMode) {
        Main.employeeMode = employeeMode;
    }

    public static Parent getRoot() {
        return root;
    }

    public static void setRoot(Parent root) {
        Main.root = root;
    }

    public static Stage getStage() {
        return primaryStage;
    }
    
    public static void setStage(Stage stage) {
        Main.primaryStage = stage;
    }
    
    @Override
    public void start(Stage primaryStage) {

        try {
            // setting up the login scene
            root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
            Main.primaryStage = primaryStage;
            primaryStage.setTitle("Cinema Booking Management System");
            primaryStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 700, 400);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}