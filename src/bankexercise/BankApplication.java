package bankexercise;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;


import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class BankApplication extends JFrame {

    ArrayList<BankAccount> accountList = new ArrayList<>();
    static HashMap<Integer, BankAccount> table = new HashMap<>();
    private static final int TABLE_SIZE = 29;
    private static final String newline = "\n";
    
    static String fileToSaveAs = "";
    private static RandomAccessFile input;
    private static RandomAccessFile output;
    private FileHelper fileHelper;
    
    JMenuBar menuBar;
    JMenu navigateMenu, recordsMenu, transactionsMenu, fileMenu, exitMenu;
    JMenuItem nextItem, prevItem, firstItem, lastItem, findByAccount, findBySurname, listAll;
    JMenuItem createItem, modifyItem, deleteItem, setOverdraft, setInterest;
    JMenuItem deposit, withdraw, calcInterest;
    JMenuItem open, save, saveAs;
    JMenuItem closeApp;
    JButton firstItemButton, lastItemButton, nextItemButton, prevItemButton;
    JLabel accountIDLabel, accountNumberLabel, firstNameLabel, surnameLabel, accountTypeLabel, balanceLabel,
	    overdraftLabel;
    JTextField accountIDTextField, accountNumberTextField, firstNameTextField, surnameTextField, accountTypeTextField,
	    balanceTextField, overdraftTextField;
    static JFileChooser fc;
    JTable jTable;
    double interestRate;

    int currentItem = 0;

    boolean openValues;

    public BankApplication() {
	super("Bank Application");
	fileHelper = new FileHelper(input, output, fileToSaveAs);
	int currentItem;
	initComponents();
    }

    public void initComponents() {
	setLayout(new BorderLayout());
	JPanel displayPanel = new JPanel(new MigLayout());

	accountIDLabel = new JLabel("Account ID: ");
	accountIDTextField = new JTextField(15);
	accountIDTextField.setEditable(false);

	displayPanel.add(accountIDLabel, "growx, pushx");
	displayPanel.add(accountIDTextField, "growx, pushx, wrap");

	accountNumberLabel = new JLabel("Account Number: ");
	accountNumberTextField = new JTextField(15);
	accountNumberTextField.setEditable(false);

	displayPanel.add(accountNumberLabel, "growx, pushx");
	displayPanel.add(accountNumberTextField, "growx, pushx, wrap");

	surnameLabel = new JLabel("Last Name: ");
	surnameTextField = new JTextField(15);
	surnameTextField.setEditable(false);

	displayPanel.add(surnameLabel, "growx, pushx");
	displayPanel.add(surnameTextField, "growx, pushx, wrap");

	firstNameLabel = new JLabel("First Name: ");
	firstNameTextField = new JTextField(15);
	firstNameTextField.setEditable(false);

	displayPanel.add(firstNameLabel, "growx, pushx");
	displayPanel.add(firstNameTextField, "growx, pushx, wrap");

	accountTypeLabel = new JLabel("Account Type: ");
	accountTypeTextField = new JTextField(5);
	accountTypeTextField.setEditable(false);

	displayPanel.add(accountTypeLabel, "growx, pushx");
	displayPanel.add(accountTypeTextField, "growx, pushx, wrap");

	balanceLabel = new JLabel("Balance: ");
	balanceTextField = new JTextField(10);
	balanceTextField.setEditable(false);

	displayPanel.add(balanceLabel, "growx, pushx");
	displayPanel.add(balanceTextField, "growx, pushx, wrap");

	overdraftLabel = new JLabel("Overdraft: ");
	overdraftTextField = new JTextField(10);
	overdraftTextField.setEditable(false);

	displayPanel.add(overdraftLabel, "growx, pushx");
	displayPanel.add(overdraftTextField, "growx, pushx, wrap");

	add(displayPanel, BorderLayout.CENTER);

	JPanel buttonPanel = new JPanel(new GridLayout(1, 4));

	nextItemButton = new JButton(new ImageIcon("next.png"));
	prevItemButton = new JButton(new ImageIcon("previous.png"));
	firstItemButton = new JButton(new ImageIcon("first.png"));
	lastItemButton = new JButton(new ImageIcon("last.png"));

	buttonPanel.add(firstItemButton);
	buttonPanel.add(prevItemButton);
	buttonPanel.add(nextItemButton);
	buttonPanel.add(lastItemButton);

	add(buttonPanel, BorderLayout.SOUTH);

	menuBar = new JMenuBar();
	setJMenuBar(menuBar);

	//-----Navigate Menu Start-------
	navigateMenu = new JMenu("Navigate");

	nextItem = new JMenuItem("Next Item");
	prevItem = new JMenuItem("Previous Item");
	firstItem = new JMenuItem("First Item");
	lastItem = new JMenuItem("Last Item");
	findByAccount = new JMenuItem("Find by Account Number");
	findBySurname = new JMenuItem("Find by Surname");
	listAll = new JMenuItem("List All Records");

	List<JMenuItem> navigateMenuItems =Arrays.asList(nextItem, prevItem, firstItem, lastItem, findByAccount, findBySurname, listAll);
	createMenu(navigateMenu, navigateMenuItems);
	menuBar.add(navigateMenu);
	//-----Navigate Menu End-------


	//-----Records Menu Start-------
	recordsMenu = new JMenu("Records");

	createItem = new JMenuItem("Create Item");
	modifyItem = new JMenuItem("Modify Item");
	deleteItem = new JMenuItem("Delete Item");
	setOverdraft = new JMenuItem("Set Overdraft");
	setInterest = new JMenuItem("Set Interest");

	List<JMenuItem> recordsMenuItems =Arrays.asList(createItem, modifyItem, deleteItem, setOverdraft, setInterest);
	createMenu(recordsMenu, recordsMenuItems);
	menuBar.add(recordsMenu);
	//-----Records Menu End-------


	//-----Transactions Menu Start-----
	transactionsMenu = new JMenu("Transactions");

	deposit = new JMenuItem("Deposit");
	withdraw = new JMenuItem("Withdraw");
	calcInterest = new JMenuItem("Calculate Interest");
	List<JMenuItem> transactionsMenuItems =Arrays.asList(deposit, withdraw, calcInterest);
	createMenu(transactionsMenu, transactionsMenuItems);

	menuBar.add(transactionsMenu);
	//-----Transactions Menu End-----


	//-----File Menu Start-------
	fileMenu = new JMenu("File");

	open = new JMenuItem("Open File");
	save = new JMenuItem("Save File");
	saveAs = new JMenuItem("Save As");
	List<JMenuItem> fileMenuItems =Arrays.asList(open, save, saveAs); 
	createMenu(fileMenu, fileMenuItems);

	menuBar.add(fileMenu);
	//-----File Menu End-------

	exitMenu = new JMenu("Exit");

	closeApp = new JMenuItem("Close Application");

	exitMenu.add(closeApp);

	menuBar.add(exitMenu);

	setDefaultCloseOperation(EXIT_ON_CLOSE);

	setOverdraft.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (table.get(currentItem).getAccountType().trim().equals("Current")) {
		    String newOverdraftStr = JOptionPane.showInputDialog(null, "Enter new Overdraft",
			    JOptionPane.OK_CANCEL_OPTION);
		    overdraftTextField.setText(newOverdraftStr);
		    table.get(currentItem).setOverdraft(Double.parseDouble(newOverdraftStr));
		} else
		    JOptionPane.showMessageDialog(null, "Overdraft only applies to Current Accounts");

	    }
	});

	// Add Action For Navigation
	addActionsForNavigation();
	// Add Action For Record
	addActionsForRecords();
	// Add Action For File
	addActionsForFile();
	// Add Action for Search
	addActionsForSearch();
	// Add Action For Transactions
	addActionsForTransactions();

	closeApp.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {

		int answer = JOptionPane.showConfirmDialog(BankApplication.this,
			"Do you want to save before quitting?");
		if (answer == JOptionPane.YES_OPTION) {
		    fileHelper.saveFileAs(table,fc);
		    dispose();
		} else if (answer == JOptionPane.NO_OPTION) {
		    dispose();
		}
	    }
	});

	
	
    }

    public void saveOpenValues() {
	if (openValues) {
	    surnameTextField.setEditable(false);
	    firstNameTextField.setEditable(false);

	    table.get(currentItem).setSurname(surnameTextField.getText());
	    table.get(currentItem).setFirstName(firstNameTextField.getText());
	}
    }

    public void displayDetails(int currentItem) {

	accountIDTextField.setText(table.get(currentItem).getAccountID() + "");
	accountNumberTextField.setText(table.get(currentItem).getAccountNumber());
	surnameTextField.setText(table.get(currentItem).getSurname());
	firstNameTextField.setText(table.get(currentItem).getFirstName());
	accountTypeTextField.setText(table.get(currentItem).getAccountType());
	balanceTextField.setText(table.get(currentItem).getBalance() + "");
	if (accountTypeTextField.getText().trim().equals("Current"))
	    overdraftTextField.setText(table.get(currentItem).getOverdraft() + "");
	else
	    overdraftTextField.setText("Only applies to current accs");

    }

    public void put(int key, BankAccount value) {
	int hash = (key % TABLE_SIZE);

	while (table.containsKey(key)) {
	    hash = hash + 1;

	}
	table.put(hash, value);

    }

    public void addActionsForTransactions() {

	deposit.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		String accNum = JOptionPane.showInputDialog("Account number to deposit into: ");
		boolean found = false;

		for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
		    if (accNum.equals(entry.getValue().getAccountNumber().trim())) {
			found = true;
			String toDeposit = JOptionPane.showInputDialog("Account found, Enter Amount to Deposit: ");
			entry.getValue().setBalance(entry.getValue().getBalance() + Double.parseDouble(toDeposit));
			displayDetails(entry.getKey());
		    }
		}
		if (!found)
		    JOptionPane.showMessageDialog(null, "Account number " + accNum + " not found.");
	    }
	});

	withdraw.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		String accNum = JOptionPane.showInputDialog("Account number to withdraw from: ");
		String toWithdraw = JOptionPane.showInputDialog("Account found, Enter Amount to Withdraw: ");
		boolean found;

		for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {

		    if (accNum.equals(entry.getValue().getAccountNumber().trim())) {

			found = true;

			if (entry.getValue().getAccountType().trim().equals("Current")) {
			    if (Double.parseDouble(toWithdraw) > entry.getValue().getBalance()
				    + entry.getValue().getOverdraft())
				JOptionPane.showMessageDialog(null, "Transaction exceeds overdraft limit");
			    else {
				entry.getValue()
					.setBalance(entry.getValue().getBalance() - Double.parseDouble(toWithdraw));
				displayDetails(entry.getKey());
			    }
			} else if (entry.getValue().getAccountType().trim().equals("Deposit")) {
			    if (Double.parseDouble(toWithdraw) <= entry.getValue().getBalance()) {
				entry.getValue()
					.setBalance(entry.getValue().getBalance() - Double.parseDouble(toWithdraw));
				displayDetails(entry.getKey());
			    } else
				JOptionPane.showMessageDialog(null, "Insufficient funds.");
			}
		    }
		}
	    }
	});

	calcInterest.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
		    if (entry.getValue().getAccountType().equals("Deposit")) {
			double equation = 1 + ((interestRate) / 100);
			entry.getValue().setBalance(entry.getValue().getBalance() * equation);
			JOptionPane.showMessageDialog(null, "Balances Updated");
			displayDetails(entry.getKey());
		    }
		}
	    }
	});
    }

    public void addActionsForSearch() {
	findBySurname.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {

		String sName = JOptionPane.showInputDialog("Search for surname: ");
		boolean found = false;

		for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {

		    if (sName.equalsIgnoreCase((entry.getValue().getSurname().trim()))) {
			found = true;
			accountIDTextField.setText(entry.getValue().getAccountID() + "");
			accountNumberTextField.setText(entry.getValue().getAccountNumber());
			surnameTextField.setText(entry.getValue().getSurname());
			firstNameTextField.setText(entry.getValue().getFirstName());
			accountTypeTextField.setText(entry.getValue().getAccountType());
			balanceTextField.setText(entry.getValue().getBalance() + "");
			overdraftTextField.setText(entry.getValue().getOverdraft() + "");
		    }
		}
		if (found)
		    JOptionPane.showMessageDialog(null, "Surname  " + sName + " found.");
		else
		    JOptionPane.showMessageDialog(null, "Surname " + sName + " not found.");
	    }
	});

	findByAccount.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {

		String accNum = JOptionPane.showInputDialog("Search for account number: ");
		boolean found = false;

		for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {

		    if (accNum.equals(entry.getValue().getAccountNumber().trim())) {
			found = true;
			accountIDTextField.setText(entry.getValue().getAccountID() + "");
			accountNumberTextField.setText(entry.getValue().getAccountNumber());
			surnameTextField.setText(entry.getValue().getSurname());
			firstNameTextField.setText(entry.getValue().getFirstName());
			accountTypeTextField.setText(entry.getValue().getAccountType());
			balanceTextField.setText(entry.getValue().getBalance() + "");
			overdraftTextField.setText(entry.getValue().getOverdraft() + "");

		    }
		}
		if (found)
		    JOptionPane.showMessageDialog(null, "Account number " + accNum + " found.");
		else
		    JOptionPane.showMessageDialog(null, "Account number " + accNum + " not found.");

	    }
	});

    }

    public void addActionsForFile() {
	open.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		fileHelper.readFile(table,fc);
		currentItem = 0;
		while (!table.containsKey(currentItem)) {
		    currentItem++;
		}
		displayDetails(currentItem);
	    }
	});

	save.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if(table != null && table.size() > 0)
		{
		    fileHelper.writeFile(table,fc);
		}
		else
		{
		    JOptionPane.showMessageDialog(null, "Can not save empty content!");
		}
	    }
	});

	saveAs.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if(table != null && table.size() > 0)
		{
		    fileHelper.saveFileAs(table,fc);
		}
		else
		{
		    JOptionPane.showMessageDialog(null, "Can not save empty content!");
		}
	    }
	});
    }

    public void addActionsForRecords() {
	deleteItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {

		table.remove(currentItem);
		JOptionPane.showMessageDialog(null, "Account Deleted");

		currentItem = 0;
		while (!table.containsKey(currentItem)) {
		    currentItem++;
		}
		displayDetails(currentItem);

	    }
	});

	createItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		new CreateBankDialog(table);
	    }
	});

	modifyItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		surnameTextField.setEditable(true);
		firstNameTextField.setEditable(true);

		openValues = true;
	    }
	});

	setInterest.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {

		String interestRateStr = JOptionPane.showInputDialog("Enter Interest Rate: (do not type the % sign)");
		if (interestRateStr != null)
		    interestRate = Double.parseDouble(interestRateStr);

	    }
	});
    }

    public void addActionsForNavigation() {
	ActionListener first = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		saveOpenValues();
		currentItem = 0;
		if (table != null && table.size() > 0) {
		    while (!table.containsKey(currentItem)) {
			currentItem++;
		    }
		    displayDetails(currentItem);
		} else {
		    JOptionPane.showMessageDialog(null, "Nothing To Display!");
		}
	    }
	};

	ActionListener next = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (table != null && table.size() > 0) {
		    ArrayList<Integer> keyList = new ArrayList<>();
		    int i = 0;
		    while (i < TABLE_SIZE) {
			i++;
			if (table.containsKey(i))
			    keyList.add(i);
		    }

		    int maxKey = Collections.max(keyList);
		    saveOpenValues();
		    if (currentItem < maxKey) {
			currentItem++;
			while (!table.containsKey(currentItem)) {
			    currentItem++;
			}
		    }
		    displayDetails(currentItem);
		} else {
		    JOptionPane.showMessageDialog(null, "Nothing To Display!");
		}
	    }

	};

	ActionListener prev = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (table != null && table.size() > 0) {
		    ArrayList<Integer> keyList = new ArrayList<Integer>();
		    int i = 0;
		    while (i < TABLE_SIZE) {
			i++;
			if (table.containsKey(i))
			    keyList.add(i);
		    }
		    int minKey = Collections.min(keyList);
		    if (currentItem > minKey) {
			currentItem--;
			while (!table.containsKey(currentItem)) {
			    currentItem--;
			}

		    }
		    displayDetails(currentItem);
		} else {
		    JOptionPane.showMessageDialog(null, "Nothing To Display!");
		}
	    }
	};

	ActionListener last = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		saveOpenValues();
		currentItem = 29;
		if (table != null && table.size() > 0) {
		    while (!table.containsKey(currentItem)) {
			currentItem--;

		    }
		    displayDetails(currentItem);
		} else {
		    JOptionPane.showMessageDialog(null, "Nothing To Display!");
		}
	    }
	};

	listAll.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {

		JFrame frame = new JFrame("TableDemo");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String col[] = { "ID", "Number", "Name", "Account Type", "Balance", "Overdraft" };

		DefaultTableModel tableModel = new DefaultTableModel(col, 0);
		jTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(jTable);
		jTable.setAutoCreateRowSorter(true);

		for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {

		    Object[] objs = { entry.getValue().getAccountID(), entry.getValue().getAccountNumber(),
			    entry.getValue().getFirstName().trim() + " " + entry.getValue().getSurname().trim(),
			    entry.getValue().getAccountType(), entry.getValue().getBalance(),
			    entry.getValue().getOverdraft() };

		    tableModel.addRow(objs);
		}
		frame.setSize(600, 500);
		frame.add(scrollPane);
		frame.setVisible(true);
	    }
	});

	nextItemButton.addActionListener(next);
	nextItem.addActionListener(next);

	prevItemButton.addActionListener(prev);
	prevItem.addActionListener(prev);

	firstItemButton.addActionListener(first);
	firstItem.addActionListener(first);

	lastItemButton.addActionListener(last);
	lastItem.addActionListener(last);
    }
    
    private void createMenu(JMenu menu, List<JMenuItem> items) {
	for (JMenuItem item : items) {
	    menu.add(item);
	}
    }

    
    public static void main(String[] args) {
	BankApplication ba = new BankApplication();
	ba.setSize(1200, 400);
	ba.pack();
	ba.setVisible(true);
    }

}
