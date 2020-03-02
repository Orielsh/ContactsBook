import javax.swing.*;

public class Test {

    public static void main(String[] args){
        JFrame frame = new JFrame("Contact Book");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        ContactsPanel panel = new ContactsPanel();
        frame.add(panel);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
