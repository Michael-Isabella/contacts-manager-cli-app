import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ContactApp {

    public String directory = "./src/";
    public String filename = "contacts.txt";

    public TreeMap<String, String> myContact;

    public ContactApp() {
        this.myContact = new TreeMap<>();
    }

    public static void main(String[] args) {
        ContactApp App = new ContactApp();

        Path contactsDirectory = Paths.get(App.directory);
        Path contactsFileName = Paths.get(App.directory, App.filename);

        // this loads the readme
        try {
            ArrayList<String> contactBook = (ArrayList<String>) Files.readAllLines(contactsFileName);
            for (String contact : contactBook) {
                String [] temp = contact.split(",");

                App.myContact.put(temp[0], temp[1]);
            }
        } catch (IOException e) {
            System.out.println("cant read contactBook");
        }

        // this views contacts
        for(Map.Entry<String, String> contact : App.myContact.entrySet()) {
            System.out.println(contact.getKey());
            System.out.println(contact.getValue());
        }

        // adding a contact
        App.myContact.put("John Doe", "98765432");
        App.myContact.put("Marie Rose", "987654321");

        // search by contact name
        String searchName = "John Doe";
        if(App.myContact.containsKey(searchName)) {
            System.out.println(searchName + "|" + App.myContact.get(searchName));
        }

        // delete contact
        App.myContact.remove(searchName);

        // exit


        try {
            if (Files.notExists(contactsDirectory)) {
                Files.createDirectories(contactsDirectory);
            } else {
                System.out.println("Directory already exists. Overwriting");
            }
        } catch (IOException e) {
            System.out.println("Directory could not be created.");
        }

        try {
            if (Files.notExists(contactsFileName)) {
                Files.createFile(contactsFileName);
            } else {
                System.out.println("File already exists. Overwriting file.");
            }
        } catch (IOException e) {
            System.out.println("File could not be created.");
        }

        ArrayList<String> templist = new ArrayList<>();

        for(Map.Entry<String, String> contact : App.myContact.entrySet()) {
            templist.add(contact.getKey() + "," + contact.getValue());
        }

        try{
            Files.write(contactsFileName, templist);
        } catch (IOException e) {
            System.out.println("could not write file");
        }



    }
}
