package texnologia;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GUI extends JFrame {
    private JComboBox<String> comboDirection;
    private JTextField[] textMathimata = new JTextField[4];
    private JLabel[] labelMathimata = new JLabel[4];
    private JComboBox<String> comboSpecialYesNo;
    private JCheckBox[] specialCheckBoxes;
    private JTextField[] specialGrades;
    private JTextArea resultArea;

    private HashMap<String, String[]> mathimataAnaKateuthinsi = new HashMap<>();
    private HashMap<String, double[]> syntelestesAnaKateuthinsi = new HashMap<>();
    private String[] eidikaMathimata = {
            "Αγγλικά", "Ισπανικά", "Γερμανικά", "Γαλλικά", "Ιταλικά",
            "Ελεύθερο Σχέδιο", "Γραμμικό Σχέδιο",
            "Μουσική Αντίληψη", "Μουσική Εκτέλεση & Ερμηνεία"
    };

    public GUI() {
        setTitle("Υπολογιστής Μορίων");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 750);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Κατεύθυνση
        JPanel directionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        directionPanel.setBorder(BorderFactory.createTitledBorder("Επιλογή Κατεύθυνσης"));
        comboDirection = new JComboBox<>(new String[]{"Θεωρητική", "Θετική", "Οικονομικά", "Υγείας"});
        directionPanel.add(comboDirection);
        mainPanel.add(directionPanel);

        // Μαθήματα
        JPanel lessonsPanel = new JPanel();
        lessonsPanel.setLayout(new GridLayout(4, 2, 10, 10));
        lessonsPanel.setBorder(BorderFactory.createTitledBorder("Βασικά Μαθήματα"));
        for (int i = 0; i < 4; i++) {
            labelMathimata[i] = new JLabel("Μάθημα " + (i + 1));
            textMathimata[i] = new JTextField(5);
            lessonsPanel.add(labelMathimata[i]);
            lessonsPanel.add(textMathimata[i]);
        }
        mainPanel.add(lessonsPanel);

        // Ειδικά μαθήματα επιλογή
        JPanel specialPanel = new JPanel();
        specialPanel.setLayout(new BoxLayout(specialPanel, BoxLayout.Y_AXIS));
        specialPanel.setBorder(BorderFactory.createTitledBorder("Ειδικά Μαθήματα"));
        JPanel specialChoicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        specialChoicePanel.add(new JLabel("Έχεις ειδικό μάθημα;"));
        comboSpecialYesNo = new JComboBox<>(new String[]{"Όχι", "Ναι"});
        specialChoicePanel.add(comboSpecialYesNo);
        specialPanel.add(specialChoicePanel);

        JPanel checkBoxPanel = new JPanel(new GridLayout(5, 2));
        specialCheckBoxes = new JCheckBox[eidikaMathimata.length];
        specialGrades = new JTextField[eidikaMathimata.length];
        for (int i = 0; i < eidikaMathimata.length; i++) {
            JPanel boxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            specialCheckBoxes[i] = new JCheckBox(eidikaMathimata[i]);
            specialGrades[i] = new JTextField(3);
            specialGrades[i].setEnabled(false);
            boxPanel.add(specialCheckBoxes[i]);
            boxPanel.add(specialGrades[i]);
            checkBoxPanel.add(boxPanel);
            int finalI = i;
            specialCheckBoxes[i].addActionListener(e -> {
                specialGrades[finalI].setEnabled(specialCheckBoxes[finalI].isSelected());
            });
        }
        specialPanel.add(checkBoxPanel);
        mainPanel.add(specialPanel);

        // Κουμπί και αποτέλεσμα
        JButton calcButton = new JButton("Υπολογισμός Μορίων");
        mainPanel.add(calcButton);

        resultArea = new JTextArea(4, 40);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createTitledBorder("Αποτέλεσμα"));
        mainPanel.add(resultArea);

        // Προσθήκη στο frame
        setContentPane(new JScrollPane(mainPanel));

        // Δεδομένα κατευθύνσεων
        mathimataAnaKateuthinsi.put("Θεωρητική", new String[]{"Έκθεση", "Ιστορία", "Αρχαία", "Λατινικά"});
        mathimataAnaKateuthinsi.put("Θετική", new String[]{"Έκθεση", "Μαθηματικά", "Φυσική", "Χημεία"});
        mathimataAnaKateuthinsi.put("Οικονομικά", new String[]{"Έκθεση", "Μαθηματικά", "ΑΟΘ", "ΑΕΠΠ"});
        mathimataAnaKateuthinsi.put("Υγείας", new String[]{"Έκθεση", "Βιολογία", "Φυσική", "Χημεία"});

        syntelestesAnaKateuthinsi.put("Θεωρητική", new double[]{0.25, 0.30, 0.30, 0.15});
        syntelestesAnaKateuthinsi.put("Θετική", new double[]{0.25, 0.25, 0.25, 0.25});
        syntelestesAnaKateuthinsi.put("Οικονομικά", new double[]{0.25, 0.30, 0.25, 0.20});
        syntelestesAnaKateuthinsi.put("Υγείας", new double[]{0.25, 0.30, 0.25, 0.20});

        comboDirection.addActionListener(e -> ενημέρωσηΜαθημάτων());
        ενημέρωσηΜαθημάτων();

        calcButton.addActionListener(e -> υπολογισμός());
    }

    private void ενημέρωσηΜαθημάτων() {
        String kateuthinsi = (String) comboDirection.getSelectedItem();
        String[] mathimata = mathimataAnaKateuthinsi.get(kateuthinsi);
        for (int i = 0; i < 4; i++) {
            labelMathimata[i].setText(mathimata[i] + ":");
            textMathimata[i].setText("");
        }
    }

    private void υπολογισμός() {
        try {
            String kateuthinsi = (String) comboDirection.getSelectedItem();
            double[] syntelestes = syntelestesAnaKateuthinsi.get(kateuthinsi);

            double sunoloMorion = 0;
            double sunoloVathmon = 0;
            int count = 0;

            for (int i = 0; i < 4; i++) {
                double v = Double.parseDouble(textMathimata[i].getText());
                sunoloMorion += v * syntelestes[i] * 1000;
                sunoloVathmon += v;
                count++;
            }

            if (comboSpecialYesNo.getSelectedItem().equals("Ναι")) {
                for (int i = 0; i < eidikaMathimata.length; i++) {
                    if (specialCheckBoxes[i].isSelected()) {
                        double v = Double.parseDouble(specialGrades[i].getText());
                        sunoloVathmon += v;
                        count++;
                    }
                }
            }

            double mo = sunoloVathmon / count;
            resultArea.setText("Μόρια: " + (int) sunoloMorion + "\nΜέσος Όρος: " + String.format("%.2f", mo));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Σφάλμα στα δεδομένα. Ελέγξτε τους βαθμούς.",
                    "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
    }

   
}

    

