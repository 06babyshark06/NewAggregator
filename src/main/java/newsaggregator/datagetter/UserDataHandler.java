package newsaggregator.datagetter;

import com.google.gson.Gson;
import newsaggregator.notification.ErrorNotification;
import newsaggregator.data.User;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class UserDataHandler {
    public static List<User> users = new ArrayList<User>();

    public void writeObjectToFile() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        try {
            FileWriter fileWriter=new FileWriter(s + "/src/main/resources/newsaggregator/users.txt",false);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            Gson gson = new Gson();
            for (User user : users) {
                String userProperty = gson.toJson(user);
                writer.write(userProperty);
                writer.newLine();
            }
            writer.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            new ErrorNotification().showMessage("Something went wrong with loading the file please try again");
        }

    }

    public List<User> readObjectFromFile() {
        users=new ArrayList<>();
        try {
            File myJsonFile = new File("src/main/resources/newsaggregator/users.txt");
            Scanner myScanner = new Scanner(myJsonFile);
            while (myScanner.hasNextLine()) {
                String jsonData = myScanner.nextLine();
                User user = new Gson().fromJson(jsonData, User.class);
                users.add(user);
            }
            if (users.isEmpty()) {
                users.add(new User("admin", "1", new ArrayList<>()));
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error");
            new ErrorNotification().showMessage("Could not find users.txt file");
        }
        return users;
    }
    public void updateUserState() {
        if (users.isEmpty()) return;
        for (User user :users) {
            if (user.getUsername().equals(users.get(users.size() - 1).getUsername())) {
                users.remove(user);
                return;
            }
        }
    }
}
