import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ContactInfo implements Serializable {
    private String fullName;
    private String phone;
    private String email;

    public ContactInfo(String fullName, String phone, String email) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name: " + fullName + ", Phone: " + phone + ", Email: " + email;
    }
}

class ContactDirectory {
    private List<ContactInfo> contactList;
    private static final String DATA_FILE = "contacts.dat";

    public ContactDirectory() {
        if (!loadDataFromFile()) {
            contactList = new ArrayList<>();
        }
    }

    public void addContact(ContactInfo contact) {
        contactList.add(contact);
        saveDataToFile();
    }

    public boolean removeContact(String fullName) {
        ContactInfo contactToRemove = null;
        for (ContactInfo contact : contactList) {
            if (contact.getFullName().equalsIgnoreCase(fullName)) {
                contactToRemove = contact;
                break;
            }
        }
        if (contactToRemove != null) {
            contactList.remove(contactToRemove);
            saveDataToFile();
            return true;
        }
        return false;
    }

    public ContactInfo searchContact(String fullName) {
        for (ContactInfo contact : contactList) {
            if (contact.getFullName().equalsIgnoreCase(fullName)) {
                return contact;
            }
        }
        return null;
    }

    public void editContact(String fullName, String newPhone, String newEmail) {
        for (ContactInfo contact : contactList) {
            if (contact.getFullName().equalsIgnoreCase(fullName)) {
                contact.setPhone(newPhone);
                contact.setEmail(newEmail);
                saveDataToFile();
                break;
            }
        }
    }

    public List<ContactInfo> getAllContacts() {
        return contactList;
    }

    public void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(contactList);
            System.out.println("Contact data saved to file: " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Error while saving contact data: " + e.getMessage());
        }
    }

    public boolean loadDataFromFile() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                contactList = (List<ContactInfo>) ois.readObject();
                System.out.println("Contact data loaded from file: " + DATA_FILE);
                return true;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error while loading contact data: " + e.getMessage());
            }
        }
        return false;
    }
}

public class CustomAddressBook {
    private static Scanner scanner = new Scanner(System.in);
    private static ContactDirectory directory = new ContactDirectory();

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("=== Custom Address Book ===");
            System.out.println("1. Add Contact");
            System.out.println("2. Remove Contact");
            System.out.println("3. Search Contact");
            System.out.println("4. Edit Contact");
            System.out.println("5. Display All Contacts");
            System.out.println("6. Save Data to File");
            System.out.println("7. Exit");
            System.out.print("Enter your choice (1-7): ");

            int choice = validateIntInput(1, 7);

            switch (choice) {
                case 1:
                    addContact();
                    break;
                case 2:
                    removeContact();
                    break;
                case 3:
                    searchContact();
                    break;
                case 4:
                    editContact();
                    break;
                case 5:
                    displayAllContacts();
                    break;
                case 6:
                    directory.saveDataToFile();
                    break;
                case 7:
                    directory.saveDataToFile();
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addContact() {
        System.out.print("Enter the full name: ");
        String fullName = validateStringInput();

        System.out.print("Enter the phone number: ");
        String phone = validatePhoneNumber();

        System.out.print("Enter the email address: ");
        String email = validateEmailAddress();

        ContactInfo contact = new ContactInfo(fullName, phone, email);
        directory.addContact(contact);
        System.out.println("Contact added successfully!");
    }

    private static void removeContact() {
        System.out.print("Enter the full name of the contact to remove: ");
        String fullName = validateStringInput();

        if (directory.removeContact(fullName)) {
            System.out.println("Contact removed successfully!");
        } else {
            System.out.println("Contact not found!");
        }
    }

    private static void searchContact() {
        System.out.print("Enter the full name of the contact to search: ");
        String fullName = validateStringInput();

        ContactInfo contact = directory.searchContact(fullName);
        if (contact != null) {
            System.out.println("Contact found:");
            System.out.println(contact);
        } else {
            System.out.println("Contact not found!");
        }
    }

    private static void editContact() {
        System.out.print("Enter the full name of the contact to edit: ");
        String fullName = validateStringInput();

        ContactInfo contact = directory.searchContact(fullName);
        if (contact != null) {
            System.out.print("Enter the new phone number: ");
            String newPhone = validatePhoneNumber();

            System.out.print("Enter the new email address: ");
            String newEmail = validateEmailAddress();

            directory.editContact(fullName, newPhone, newEmail);
            System.out.println("Contact details updated successfully!");
        } else {
            System.out.println("Contact not found!");
        }
    }

    private static void displayAllContacts() {
        List<ContactInfo> contacts = directory.getAllContacts();
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
        } else {
            System.out.println("=== All Contacts ===");
            for (ContactInfo contact : contacts) {
                System.out.println(contact);
            }
        }
    }

    private static String validateStringInput() {
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.println("This field cannot be left empty. Please try again: ");
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private static String validatePhoneNumber() {
        String phoneNumber = scanner.nextLine().trim();
        while (!phoneNumber.matches("\\d{10}")) {
            System.out.println("Invalid phone number. Please enter a 10-digit number: ");
            phoneNumber = scanner.nextLine().trim();
        }
        return phoneNumber;
    }

    private static String validateEmailAddress() {
        String emailAddress = scanner.nextLine().trim();
        while (!emailAddress.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            System.out.println("Invalid email address. Please enter a valid email: ");
            emailAddress = scanner.nextLine().trim();
        }
        return emailAddress;
    }

    private static int validateIntInput(int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value < min || value > max) {
                    throw new NumberFormatException();
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer between " + min + " and " + max + ": ");
            }
        }
    }
}
