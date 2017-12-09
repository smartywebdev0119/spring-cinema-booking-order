package compgc01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;

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
 * @since 09.12.2017
 * 
 * References:
 * JSON library by https://code.google.com/archive/p/json-simple/,
 * JavaFX icons made by http://www.jensd.de/wordpress/ for the buttons,
 * dummy film posters and text from http://www.imdb.com/,
 * a .png icon showing a .csv file made by https://thenounproject.com/term/csv-file/56841/,
 * UCLlywood sign inspired by the original Hollywood one at http://www.clker.com/cliparts/A/z/5/z/y/H/hollywood-sign-md.png,
 * default film poster by http://comicbook.com/,
 * default user icon by https://www.whatsapp.com/,
 * and all other images adapted from originals at https://www.freepik.com/.
 */
public class Main extends Application {

    private static Parent root;
    private static Stage primaryStage;
    static Main m = null;
    static User currentUser;
    private static Boolean employeeMode = false;
    private static String selectedFilmTitle = "";

    // arrayLists to be populated with the information from the text files
    static HashSet<Employee> employees = new HashSet<Employee>();
    static HashSet<Customer> customers = new HashSet<Customer>();
    static HashSet<Film> films = new HashSet<Film>();
    static HashSet<BookingHistoryItem> bookings = new HashSet<BookingHistoryItem>();

    public static void main(String[] args) {

        m = new Main();

        // playMusic("wonderful_world.mp3");

        // if files do not exist, create them using default values
        try {
            if (!(new File(URLDecoder.decode(getPath() + "res/employeesJSON.txt", "UTF-8")).exists()))
                createJSONFile("employees");
            if (!(new File(URLDecoder.decode(getPath() + "res/customersJSON.txt", "UTF-8")).exists()))
                createJSONFile("customers");
            if (!(new File(URLDecoder.decode(getPath() + "res/filmsJSON.txt", "UTF-8")).exists()))
                createJSONFile("films");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        launch(args);
    }

    static HashSet<Employee> getEmployeeList() {

        return employees;
    }

    static HashSet<Customer> getCustomerList() {

        return customers;
    }

    static HashSet<Film> getFilmList() {

        return films;
    }

    static HashSet<BookingHistoryItem> getBookingList() {

        return bookings;
    }

    static void resetEmployeeList() {

        employees.clear();
    }

    static void resetCustomerList() {

        customers.clear();
    }

    static void resetFilmList() {

        films.clear();
    }

    static void resetBookingList() {

        bookings.clear();
    }

    static Main getMainApplication() {

        return m;
    }

    static JSONObject readJSONFile(String file) {

        JSONObject items = null;

        try {
            String path = getPath();

            path = URLDecoder.decode(path + "res/" + file, "UTF-8");

            JSONParser parser = new JSONParser();
            items =  (JSONObject) parser.parse(new FileReader(path));
            for (Object s : items.keySet()) {
                // System.out.println((String) s);
                JSONObject item = (JSONObject) items.get(s);

                if (file.contains("employees"))
                    employees.add( new Employee ((String) item.get("firstName"), (String) item.get("lastName"), (String) item.get("username"), (String) item.get("password"), (String) item.get("email")));
                else if (file.contains("customers"))
                    customers.add( new Customer ((String) item.get("firstName"), (String) item.get("lastName"), (String) item.get("username"), (String) item.get("password"), (String) item.get("email"), Double.parseDouble(String.valueOf(item.get("accountBalance")))));
                else if (file.contains("films")) {
                    String[] times = {(String) item.get("time1"), (String) item.get("time2"), (String) item.get("time3")};
                    films.add( new Film ((String) s, (String) item.get("description"), (String) item.get("startDate"), (String) item.get("endDate"), times));
                }
                else if (file.contains("bookings"))
                    bookings.add(new BookingHistoryItem((String) item.get("status"), (String) item.get("username"),
                            getCustomerByUsername((String) item.get("username")).getFirstName(), getCustomerByUsername((String) item.get("username")).getLastName(),
                            (String) item.get("film"), (String) item.get("date"),
                            (String) item.get("time"), (String) item.get("seat"), (String) (s)));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    static void modifyJSONFile(String file, String identifier, String attribute, String newValue) {

        try {
            JSONObject items = readJSONFile(file);

            if (newValue.equals("delete")) {
                items.remove(identifier);
                // System.out.println(items.toJSONString());
            }
            else {
                JSONObject itemToEdit = null;

                if (items.get(identifier) == null) {
                    itemToEdit = new JSONObject();
                    items.put(identifier, itemToEdit);
                }
                else
                    itemToEdit = (JSONObject) items.get(identifier);

                itemToEdit.put(attribute, newValue);
            }

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
    static void createJSONFile(String type) {

        try {
            // creating JSON files
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

    static void playMusic(String musicFile) {

        try {
            File file = new File(URLDecoder.decode(getPath() + "res/sounds/" + musicFile, "UTF-8"));
            Media backgroundMusic = new Media(file.toPath().toUri().toString());
            MediaPlayer player = new MediaPlayer(backgroundMusic);
            player.play();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    static String getPath() {

        String path = ClassLoader.getSystemClassLoader().getResource(".").getPath();

        // leave this here to run in Eclipse... if proper deployment then
        // remove code to only run from jar file
        if (path.contains("zcinema/bin"))
            path = path.split("zcinema")[0];

        return path;
    }

    static User getCurrentUser() {

        return currentUser;
    }

    static void setCurrentUser(User currentUser) {

        Main.currentUser = currentUser;
    }

    static boolean isEmployee() {

        return employeeMode;
    }

    static void setEmployeeMode(boolean employeeMode) {

        Main.employeeMode = employeeMode;
    }

    static Customer getCustomerByUsername(String username) {
        for (Customer c : customers)
            if (c.getUsername().equals(username))
                return c;

        return null;
    }

    static Film getFilmByTitle (String title) {

        for (Film film : Main.getFilmList()) {
            if (film.getTitle().equals(title))
                return film;
        }

        return null;
    }

    static Parent getRoot() {

        return root;
    }

    static void setRoot(Parent root) {

        Main.root = root;
    }

    static Stage getStage() {

        return primaryStage;
    }

    static void setStage(Stage stage) {

        Main.primaryStage = stage;
    }

    static void setSelectedFilmTitle (String selectedFilmTitle) {

        Main.selectedFilmTitle = selectedFilmTitle;
    }

    static String getSelectedFilmTitle () {

        return selectedFilmTitle;
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