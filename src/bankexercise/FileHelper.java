package bankexercise;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileHelper {

    private static final int TABLE_SIZE = 29;

    private RandomAccessFile input;
    private RandomAccessFile output;
    private String fileToSaveAs;

    FileHelper(RandomAccessFile input, RandomAccessFile output, String fileToSaveAs) {
	this.input = input;
	this.output = output;
	this.fileToSaveAs = fileToSaveAs;
    }

    public void writeFile(HashMap<Integer, BankAccount> table, JFileChooser fc) {
	openFileWrite(table, fc);
	saveToFile(table);
	closeFile();
    }

    public void saveFileAs(HashMap<Integer, BankAccount> table, JFileChooser fc) {
	saveToFileAs(fc);
	saveToFile(table);
	closeFile();
    }

    public void readFile(HashMap<Integer, BankAccount> table, JFileChooser fc) {
	openFileRead(table, fc);
	readRecords(table);
	closeFile();
    }

    private void openFileWrite(HashMap<Integer, BankAccount> table, JFileChooser fc) {
	this.fileToSaveAs = "";
	if (fileToSaveAs != "") {
	    try // open file
	    {
		output = new RandomAccessFile(fileToSaveAs, "rw");
		JOptionPane.showMessageDialog(null, "Accounts saved to " + fileToSaveAs);
	    } // end try
	    catch (IOException ioException) {
		JOptionPane.showMessageDialog(null, "File does not exist.");
	    } // end catch
	} else
	    saveToFileAs(fc);
    }

    private void saveToFileAs(JFileChooser fc) {

	fc = new JFileChooser();

	int returnVal = fc.showSaveDialog(null);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();

	    this.fileToSaveAs = file.getName();
	    JOptionPane.showMessageDialog(null, "Accounts saved to " + file.getName());
	} else {
	    JOptionPane.showMessageDialog(null, "Save cancelled by user");
	}

	try {
	    if (fc.getSelectedFile() == null) {
		JOptionPane.showMessageDialog(null, "Cancelled");
	    } else
		output = new RandomAccessFile(fc.getSelectedFile(), "rw");
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

    }

    private void saveToFile(HashMap<Integer, BankAccount> table) {

	RandomAccessBankAccount record = new RandomAccessBankAccount();

	Scanner input = new Scanner(System.in);

	for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
	    record.setAccountID(entry.getValue().getAccountID());
	    record.setAccountNumber(entry.getValue().getAccountNumber());
	    record.setFirstName(entry.getValue().getFirstName());
	    record.setSurname(entry.getValue().getSurname());
	    record.setAccountType(entry.getValue().getAccountType());
	    record.setBalance(entry.getValue().getBalance());
	    record.setOverdraft(entry.getValue().getOverdraft());

	    if (output != null) {

		try {
		    record.write(output);
		} catch (IOException u) {
		    u.printStackTrace();
		}
	    }

	}

    }

    private void closeFile() {
	try // close file and exit
	{
	    if (input != null)
		input.close();
	} // end try
	catch (IOException ioException) {
	    JOptionPane.showMessageDialog(null, "Error closing file.");
	} // end catch
    } // end method closeFile

    private void openFileRead(HashMap<Integer, BankAccount> table, JFileChooser fc) {

	table.clear();

	fc = new JFileChooser();
	int returnVal = fc.showOpenDialog(null);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();
	}

	try // open file
	{
	    if (fc.getSelectedFile() != null)
		input = new RandomAccessFile(fc.getSelectedFile(), "r");
	} // end try
	catch (IOException ioException) {
	    JOptionPane.showMessageDialog(null, "File Does Not Exist.");
	} // end catch

    } // end method openFile

    private void readRecords(HashMap<Integer, BankAccount> table) {

	RandomAccessBankAccount record = new RandomAccessBankAccount();

	try // read a record and display
	{
	    while (true) {
		do {
		    if (input != null)
			record.read(input);
		} while (record.getAccountID() == 0);

		BankAccount ba = new BankAccount(record.getAccountID(), record.getAccountNumber(),
			record.getFirstName(), record.getSurname(), record.getAccountType(), record.getBalance(),
			record.getOverdraft());

		Integer key = Integer.valueOf(ba.getAccountNumber().trim());

		int hash = (key % TABLE_SIZE);

		while (table.containsKey(hash)) {

		    hash = hash + 1;
		}

		table.put(hash, ba);

	    } // end while
	} // end try
	catch (EOFException eofException) // close file
	{
	    return; // end of file was reached
	} // end catch
	catch (IOException ioException) {
	    JOptionPane.showMessageDialog(null, "Error reading file.");
	    System.exit(1);
	} // end catch
    }

}