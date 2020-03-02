import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

class ContactsPanel extends JPanel {

    //Data Members:
    private final static int buttons = 5;
    private Contacts contacts = new Contacts();
    private JButton[] cmdButtons = new JButton[buttons];
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JTextField txtSearch = new JTextField();
    private JList<String> list = new JList<>();

    //Constructors:
    ContactsPanel(){
        super();
        this.setLayout(null);

        String[] cmdButtonsNames = {"Add", "Update", "Delete", "Save", "Load"};
        Listener lis = new Listener();
        for(int i = 0; i < buttons; i++) {           //Adding buttons to panel
            cmdButtons[i] = new JButton(cmdButtonsNames[i]);
            cmdButtons[i].setFont(new Font("Arial", Font.PLAIN, 20));
            cmdButtons[i].setBounds(20,(i*45)+65,100,30);
            cmdButtons[i].addActionListener(lis);
            this.add(cmdButtons[i]);
        }

        cmdButtons[1].setEnabled(false);
        cmdButtons[2].setEnabled(false);
        cmdButtons[3].setEnabled(false);

        JLabel txtLabel = new JLabel("Search");
        txtLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        txtLabel.setBounds(250,10,100,50);
        this.add(txtLabel);

        txtSearch.setBounds(155,60,300,35);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 20));
        this.add(txtSearch);
        txtSearch.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                updateList(txtSearch.getText());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                updateList(txtSearch.getText());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                updateList(txtSearch.getText());
            }
        });

        list.setModel(listModel);
        list.setFont(new Font("Arial", Font.PLAIN, 20));
        list.setBounds(155,105, 300,335);
        this.add(list);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                cmdButtons[1].setEnabled(true);
                cmdButtons[2].setEnabled(true);
            }
        });

        Border gray = BorderFactory.createLineBorder(Color.GRAY);
        list.setBorder(gray);
        this.setBorder(BorderFactory.createTitledBorder("Contacts"));

    }

    private class Listener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame f = (JFrame)SwingUtilities.getWindowAncestor(getParent());
            if(e.getSource() == cmdButtons[0]){             //add
                f.setEnabled(false);
                showAddUpdatePanel("Add", e.getSource());
            }
            if(e.getSource() == cmdButtons[1]){             //update
                f.setEnabled(false);
                showAddUpdatePanel("Update", e.getSource());
            }
            if(e.getSource() == cmdButtons[2]){             //delete
                cmdButtons[1].setEnabled(false);
                contacts.deleteContact(list.getSelectedValue().split(":")[0]);
                updateList(txtSearch.getText());
            }
            if(e.getSource() == cmdButtons[3]){             //save
                handleSave(f);
            }

            if(e.getSource() == cmdButtons[4]){             //load
                handleLoad(f);
            }
        }
    }

    private void handleLoad(JFrame f){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(f);
        if(fileChooser.getSelectedFile() != null){
            String fineName = fileChooser.getSelectedFile().toString();
            contacts.load(fineName);
            updateList("");
        }
    }
    private void handleSave(JFrame f){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showSaveDialog(f);
        if(fileChooser.getSelectedFile() !=null) {
            String fineName = fileChooser.getSelectedFile().toString();
            File cFile = new File(fineName);
            if (cFile.exists()) {
                int response = JOptionPane.showConfirmDialog(null, //
                        "Do you want to replace the existing file?", //
                        "Confirm", JOptionPane.YES_NO_OPTION, //
                        JOptionPane.QUESTION_MESSAGE);
                if (response != JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "File didn't saved!!");
                } else {
                    contacts.save(contacts, fineName);
                }
            } else {
                contacts.save(contacts, fineName);
            }
        }
    }



    private void showAddUpdatePanel(String title, Object input){
        JFrame frame = new JFrame(title + " Contact");
        frame.setSize(500, 250);
        JPanel addingPanel=new JPanel();
        addingPanel.setLayout(null);

        //Name part
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        nameLabel.setBounds(30,20,100,50);
        addingPanel.add(nameLabel);

        JTextField txtName = new JTextField();
        txtName.setBounds(30,80,200,35);
        txtName.setFont(new Font("Arial", Font.PLAIN, 20));
        addingPanel.add(txtName);
        String value = list.getSelectedValue();
        String name = "";
        String number = "";
        if(input == cmdButtons[1] && value != null){
            name = value.split(":")[0];
            number = value.split(":")[1];
            number = number.substring(1);
        }

        if(input == cmdButtons[1])
            txtName.setText(name);

        //Number part
        JLabel numberLabel = new JLabel("Number");
        numberLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        numberLabel.setBounds(250,20,100,50);
        addingPanel.add(numberLabel);

        JTextField txtNumber = new JTextField();
        txtNumber.setBounds(250,80,200,35);
        txtNumber.setFont(new Font("Arial", Font.PLAIN, 20));
        addingPanel.add(txtNumber);
        if(input == cmdButtons[1])
            txtNumber.setText(number);

        //Confirmation part
        JButton cmdOK = new JButton("OK");
        cmdOK.setBounds(175,145,120,40);
        cmdOK.setFont(new Font("Arial", Font.PLAIN, 20));
        addingPanel.add(cmdOK);

        //Border
        addingPanel.setBorder(BorderFactory.createTitledBorder(title + " Contact"));

        cmdOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (input == cmdButtons[0])//add
                    handleAdd(txtName.getText(), txtNumber.getText(),frame);
                if (input == cmdButtons[1])//update
                    handleUpdate(txtName.getText(), txtNumber.getText(),
                            list.getSelectedValue().split(":")[0], frame);
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                setMainDisplayEnable();
            }
        });

        //Display part
        frame.add(addingPanel);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private void handleAdd(String name, String number, JFrame frame){
        while(name.length() != 0 && name.charAt(0)==' ')
            name = name.substring(1);
        if (name.length() == 0 || number.length() == 0)
            JOptionPane.showMessageDialog(null, "Missing Input");
        else if (isNumeric(number)) {
            if (contacts.containName(name))
                JOptionPane.showMessageDialog(null, "Name Already exist");
            else {

                contacts.addContact(name, number);
                txtSearch.setText("");
                updateList("");
                JFrame f = (JFrame) SwingUtilities.getWindowAncestor(getParent());
                frame.setVisible(false);
                f.toFront();
                f.setEnabled(true);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Not a number");
        }
    }

    private void handleUpdate(String newName, String newNumber,
                              String oldName, JFrame frame){
        while(newName.length() != 0 && newName.charAt(0)==' ')
            newName = newName.substring(1);
        if (newName.length() == 0 || newNumber.length() == 0)
            JOptionPane.showMessageDialog(null, "Missing Input");
        else if (isNumeric(newNumber)) {
            if (newName.equals(oldName)) {
                contacts.updatePhoneNumber(newName, newNumber);
                txtSearch.setText("");
                updateList("");
                JFrame f = (JFrame) SwingUtilities.getWindowAncestor(getParent());
                frame.setVisible(false);
                f.toFront();
                f.setEnabled(true);
            }
            else {
                if( contacts.containName(newName))
                    JOptionPane.showMessageDialog(null, "Name taken");
                else{
                    contacts.updatePhoneNumber(newName, newNumber);
                    contacts.updateName(oldName, newName);
                    txtSearch.setText("");
                    updateList("");
                    JFrame f = (JFrame) SwingUtilities.getWindowAncestor(getParent());
                    frame.setVisible(false);
                    f.toFront();
                    f.setEnabled(true);
                }
            }

        }else{
            JOptionPane.showMessageDialog(null, "Not a number");
        }
    }


    /*******************************************/
    private void setMainDisplayEnable(){
        JFrame f = (JFrame)SwingUtilities.getWindowAncestor(getParent());
        f.setEnabled(true);
        f.toFront();
    }           //Re-enable main display frame

    private static boolean isNumeric(String str) {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    private void updateList(String subString){
        listModel.clear();
        for(String s : contacts.getContactsInclude(subString)){
            if(s!=null)
                listModel.addElement(s);
        }
        cmdButtons[1].setEnabled(false);
        cmdButtons[2].setEnabled(false);
        if(contacts.size() > 0)
            cmdButtons[3].setEnabled(true);
        else
            cmdButtons[3].setEnabled(false);
    }       //Refresh JList component

}