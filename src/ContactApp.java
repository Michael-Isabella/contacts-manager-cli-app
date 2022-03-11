import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactApp {

    public String directory = "./src/";
    public String filename = "contacts.txt";
    Path contactsDirectory;
    Path contactsFileName;

    DefaultTableModel tableModel;
    JTable table;

    public TreeMap<String, String> myContact;

    public ContactApp() {
        this.myContact = new TreeMap<>();
        this.contactsDirectory =  Paths.get(this.directory);
        this.contactsFileName = Paths.get(this.directory, this.filename);
    }

    private void LoadMyContacts() {
        try {
            ArrayList<String> contactBook = (ArrayList<String>) Files.readAllLines(contactsFileName);
            for (String contact : contactBook) {
                String [] temp = contact.split(",");

                this.myContact.put(temp[0], temp[1]);
            }
        } catch (IOException e) {
            System.out.println("cant read contactBook");
        }
    }

    public void ViewContacts() {
        System.out.println(
                "Name            | Phone number    |\n" +
                "-----------------------------------");
        for(Map.Entry<String, String> contact : this.myContact.entrySet()) {
            System.out.printf("%-15s | %-15s |%n",contact.getKey(),contact.getValue());
        }
        System.out.println();
    }

    public void AddContact() {
        // adding a contact
        String fullname;
        String tempString;
        Scanner get = new Scanner(System.in);
        do {
            System.out.print("Enter your fullname: ");
            fullname = get.nextLine().trim();

            if (this.myContact.containsKey(fullname)) {
                System.out.print("There's already a contact named " + fullname + " Do you want to overwrite it? (Yes/No) ");
                String answer = get.nextLine();
                if (answer.equalsIgnoreCase("yes")) {
                    break;
                }
            } else {
                break;
            }
        } while(true);

        do {
            System.out.print("Enter your phone number: ");
            String phoneNumber = get.nextLine().trim();

            Pattern pattern = Pattern.compile("^\\d{10}$");
            Matcher matcher = pattern.matcher(phoneNumber);

            if(matcher.matches()) {
                tempString = phoneNumber.substring(0,3) + "-"
                        + phoneNumber.substring(3,6) + "-"
                        + phoneNumber.substring(6,10);
                break;
            } else {
                System.out.println("Invaild phone number input.");
                System.out.println();
            }
        } while(true);

        this.myContact.put(fullname, tempString);
        System.out.println();
    }

    public String SearchByName(String searchName) {
        if(this.myContact.containsKey(searchName)) {
           return (searchName + "|" + this.myContact.get(searchName));
        }
        return "Name not found.";
    }

    public void DeleteContact(String name) {
        this.myContact.remove(name);
    }

    public void WriteMyContacts() {
        try {
            if (Files.notExists(this.contactsDirectory)) {
                Files.createDirectories(this.contactsDirectory);
            } else {
                System.out.println("Directory already exists. Overwriting");
            }
        } catch (IOException e) {
            System.out.println("Directory could not be created.");
        }

        try {
            if (Files.notExists(this.contactsFileName)) {
                Files.createFile(this.contactsFileName);
            } else {
                System.out.println("File already exists. Overwriting file.");
            }
        } catch (IOException e) {
            System.out.println("File could not be created.");
        }

        ArrayList<String> templist = new ArrayList<>();

        for(Map.Entry<String, String> contact : this.myContact.entrySet()) {
            templist.add(contact.getKey() + "," + contact.getValue());
        }

        try{
            Files.write(this.contactsFileName, templist);
        } catch (IOException e) {
            System.out.println("could not write file");
        }
    }

    public void Run() {
        // this loads the readme
        LoadMyContacts();

        System.out.println("*-----| Welcome to our Contact Book Application |-----*");
        System.out.println();
        System.out.println("Please Choose from the below list:");
        System.out.println();

        Scanner get = new Scanner(System.in);
        String input;
        do {

            System.out.println("" +
                    "1. View contacts.\n" +
                    "2. Add a new contact.\n" +
                    "3. Search a contact by name.\n" +
                    "4. Delete an existing contact.\n" +
                    "5. Exit.\n" +
                    "Enter an option (1, 2, 3, 4 or 5):");
            input = get.next();
            get.nextLine();

            switch(input) {
                case "1": {
                    // this views contacts
                    ViewContacts();
                }break;
                case "2": {
                    AddContact();
                }break;
                case "3": {
                    System.out.print("Enter fullname to search for: ");
                    String searchName = get.nextLine();
                    System.out.println(SearchByName(searchName));
                    System.out.println();
                }break;
                case "4": {
                    System.out.print("Enter fullname to delete from contact book: ");
                    String searchName = get.nextLine();
                    DeleteContact(searchName);
                }break;
            }

        } while (!input.equals("5"));

        System.out.println("Application Closing.");

        WriteMyContacts();
    }

    public void Start() {
        LoadMyContacts();
        JFrame frame = new JFrame();
        frame.setLayout(null);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setShowGrid(true);
        table.setShowVerticalLines(true);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.GRAY);
        table.setGridColor(Color.RED);

        tableModel.addColumn("Name");
        tableModel.addColumn("Phone Number");

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0,0,250,300);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel fullNameLabel = new JLabel("Full Name");
        fullNameLabel.setBounds(0,320,100, 25);

        JTextField fullNameBox = new JTextField();
        fullNameBox.setBounds(0,340,250,30);

        JLabel phoneNumberLabel = new JLabel("Phone Number");
        phoneNumberLabel.setBounds(0,370,100, 25);

        JTextField phoneNumberBox = new JTextField();
        phoneNumberBox.setBounds(0,400,250,30);

        JButton viewButton = new JButton("View Contacts");
        viewButton.setBounds(300,25,150,30);

        JButton searchButton = new JButton("Search Contacts");
        searchButton.setBounds(300,75,150,30);

        JButton deleteButton = new JButton("Delete Contact");
        deleteButton.setBounds(300,125,150,30);

        JButton addButton = new JButton("Add Contact");
        addButton.setBounds(300,175,150,30);

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);

                for(Map.Entry<String, String> contact : myContact.entrySet()) {
                    tableModel.insertRow(tableModel.getRowCount(), new Object[]{contact.getKey(), contact.getValue()});
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);

                String fullname = fullNameBox.getText();
                if(fullname.length() == 0) { JOptionPane.showMessageDialog(frame, "Input a name!"); return; }
                String phoneNumber = phoneNumberBox.getText();
                if(phoneNumber.length() == 0) { JOptionPane.showMessageDialog(frame, "Input a phone number!"); return; }

                if (myContact.containsKey(fullname)) {
                    int selectedOption = JOptionPane.showConfirmDialog(frame, "Add anyway?", "Contact already exist!", JOptionPane.YES_NO_OPTION);
                    if (selectedOption != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                Pattern pattern = Pattern.compile("^\\d{10}$");
                Matcher matcher = pattern.matcher(phoneNumber);
                String tempString;
                if(matcher.matches()) {
                    tempString = phoneNumber.substring(0,3) + "-"
                            + phoneNumber.substring(3,6) + "-"
                            + phoneNumber.substring(6,10);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid phone number!");
                    return;
                }

                myContact.put(fullname, tempString);
                fullNameBox.setText("");
                phoneNumberBox.setText("");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);

                String fullname = fullNameBox.getText();
                if(fullname.length() == 0) { JOptionPane.showMessageDialog(frame, "Input a name!"); return; }

                myContact.remove(fullname);
                fullNameBox.setText("");
                phoneNumberBox.setText("");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);

                String searchName = fullNameBox.getText();
                if(searchName.length() == 0) { JOptionPane.showMessageDialog(frame, "Input a name!"); return; }

                if(myContact.containsKey(searchName)) {
                    tableModel.insertRow(0, new Object[]{searchName, myContact.get(searchName)});
                } else {
                    JOptionPane.showMessageDialog(frame, "Name not found.");
                }
                fullNameBox.setText("");
                phoneNumberBox.setText("");
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                WriteMyContacts();
            }
        });


        String[] countryList = {"US", "MX", "FR", "DE", "AU"};

//Create the combo box, select item at index 4.
//Indices start at 0, so 4 specifies the pig.
        JComboBox countryBox = new JComboBox(countryList);
        countryBox.setSelectedIndex(0);
        countryBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        frame.add(scrollPane);
        frame.add(fullNameBox);
        frame.add(fullNameLabel);
        frame.add(phoneNumberBox);
        frame.add(phoneNumberLabel);
        frame.add(addButton);
        frame.add(viewButton);
        frame.add(searchButton);
        frame.add(deleteButton);

        frame.setResizable(false);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        ContactApp App = new ContactApp();
        App.Start();
    }
}
