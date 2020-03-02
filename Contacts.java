import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class Contacts implements Serializable {

    private TreeMap<String,String> contacts = new TreeMap<>();

    boolean addContact(String name, String phoneNumber){
        if(name == null)
            return false;
        if (this.containName(name))
            return false;
        this.contacts.put(name, phoneNumber);
        return true;
    }
    private TreeMap<String,String> getContacts(){
        return this.contacts;
    }
    boolean updateName(String oldName, String newName){
        if (oldName.equals(newName))
            return true;
        if(newName != null) {
            if (containName(oldName)) {
                String value = contacts.get(oldName);
                contacts.remove(oldName);
                return addContact(newName, value);
            }
        }
        return false;
    }

    boolean updatePhoneNumber(String name, String newPhoneNumber){
        if(!containName(name))
            return false;
        return this.contacts.put(name, newPhoneNumber) != null;
    }

    boolean containName(String name){
        return this.contacts.containsKey(name);
    }

    boolean deleteContact(String name) {
        if (name != null) {
            if (containName(name)) {
                contacts.remove(name);
                return true;
            }
        }
        return false;
    }

    /*String getPhoneNumber(String name){
        if(name != null){
            if(containName(name))
                return this.contacts.get(name);
        }
        return null;
    }*/

    @Override
    public String toString() {
        return "Contacts{" +
                "contacts=" + contacts +
                '}';
    }

    String[] getContactsInclude(String s){
        String[] result = new String[contacts.size()];
        int i = 0;
        for(Map.Entry<String, String> entry : contacts.entrySet()) {
            String name = entry.getKey();
            if(name.contains(s)) {
                String number = entry.getValue();
                result[i++] = name + ": " + number;
            }
        }
        return result;
    }


    int size(){
        return this.contacts.size();
    }

    void save(Contacts c, String fileName){
        File fileToSave = new File(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(fileToSave);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(c);
            oos.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void load(String fileName){
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.contacts = ((Contacts) ois.readObject()).getContacts();
            ois.close();
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
