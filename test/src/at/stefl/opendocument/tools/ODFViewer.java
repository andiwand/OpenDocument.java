package at.stefl.opendocument.tools;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import at.stefl.opendocument.odf.LocatedOpenDocumentFile;
import at.stefl.opendocument.odf.OpenDocumentFile;
import at.stefl.opendocument.test.TestFileChooser;

public class ODFViewer extends JFrame {

    private static class FileNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = -2009449414837874826L;

	public FileNode(String file) {
	    super(file);
	}

	@Override
	public String getUserObject() {
	    return (String) userObject;
	}

	@Override
	public String toString() {
	    return new File((String) userObject).getName();
	}
    }

    private static final long serialVersionUID = 4105090050594763787L;

    private static String showPasswordDialog(Component parent, String message) {
	JPasswordField passwordField = new JPasswordField(null);
	int result = JOptionPane.showConfirmDialog(parent, passwordField,
		message, JOptionPane.OK_OPTION);
	if (result != JOptionPane.OK_OPTION)
	    return null;
	return new String(passwordField.getPassword());
    }

    private JFileChooser fileChooser = new JFileChooser();
    private OpenDocumentFile documentFile;

    private DefaultTreeModel treeModel = new DefaultTreeModel(null);
    private JTree tree = new JTree(treeModel);

    private TreeSelectionListener treeSelectionHandler = new TreeSelectionListener() {
	public void valueChanged(TreeSelectionEvent event) {
	    Object source = event.getPath().getLastPathComponent();
	    if (!(source instanceof FileNode))
		return;

	    FileNode node = (FileNode) source;
	    String file = node.getUserObject();

	    try {
		subOpen(file);
	    } catch (Exception e) {
		e.printStackTrace();
		JOptionPane.showMessageDialog(ODFViewer.this, e, "Exception",
			JOptionPane.ERROR_MESSAGE);
	    }
	}
    };

    public ODFViewer() {
	super("ODF Viewer");

	add(new JScrollPane(tree));

	JMenuBar menuBar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenuItem open = new JMenuItem("Open");
	file.add(open);
	menuBar.add(file);
	setJMenuBar(menuBar);

	open.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent event) {
		int result = fileChooser.showOpenDialog(ODFViewer.this);
		if (result == JFileChooser.CANCEL_OPTION)
		    return;

		try {
		    File file = fileChooser.getSelectedFile();
		    open(file);
		} catch (Exception e) {
		    e.printStackTrace();
		    JOptionPane.showMessageDialog(ODFViewer.this, e,
			    "Exception", JOptionPane.ERROR_MESSAGE);
		}
	    }
	});
    }

    public void setFileChooser(JFileChooser fileChooser) {
	this.fileChooser = fileChooser;
    }

    public JFileChooser getFileChooser() {
	return fileChooser;
    }

    public void open(File file) throws IOException {
	documentFile = new LocatedOpenDocumentFile(file);
	if (documentFile.isEncrypted()) {
	    String password;
	    do {
		password = showPasswordDialog(this, "Password");
		if (password == null)
		    return;
	    } while (!documentFile.isPasswordValid());
	}

	List<String> fileList = new ArrayList<String>(
		documentFile.getFileNames());
	Collections.sort(fileList);

	DefaultMutableTreeNode root = generateTree(fileList, file.getName());
	tree.removeTreeSelectionListener(treeSelectionHandler);
	treeModel.setRoot(root);
	tree.addTreeSelectionListener(treeSelectionHandler);

    }

    private DefaultMutableTreeNode generateTree(List<String> fileList,
	    String rootName) {
	DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootName);
	Map<String, DefaultMutableTreeNode> directoryMap = new HashMap<String, DefaultMutableTreeNode>();
	directoryMap.put(null, root);

	for (String file : fileList) {
	    File path = new File(file);
	    DefaultMutableTreeNode parentNode = getParentNode(path.getParent(),
		    directoryMap);
	    parentNode.add(new FileNode(file));
	}

	return root;
    }

    private DefaultMutableTreeNode getParentNode(String path,
	    Map<String, DefaultMutableTreeNode> directoryMap) {
	DefaultMutableTreeNode result = directoryMap.get(path);

	if (result == null) {
	    File directory = new File(path);
	    String parent = directory.getParent();
	    DefaultMutableTreeNode parentNode = getParentNode(parent,
		    directoryMap);
	    result = new DefaultMutableTreeNode(directory.getName());
	    directoryMap.put(path, result);
	    parentNode.add(result);
	}

	return result;
    }

    public void open(String string) throws IOException {
	open(new File(string));
    }

    private void subOpen(String file) throws IOException {
	if (file.endsWith(".xml")) {
	    InputStream inputStream = documentFile.getFileStream(file);
	    XMLViewer viewer = new XMLViewer(inputStream, file);
	    inputStream.close();
	    viewer.setSize(600, 500);
	    viewer.setLocationRelativeTo(this);
	    viewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	    viewer.setVisible(true);
	}
    }

    public static void main(String[] args) {
	ODFViewer viewer = new ODFViewer();
	viewer.setFileChooser(new TestFileChooser());
	viewer.setDefaultCloseOperation(EXIT_ON_CLOSE);
	viewer.setSize(300, 400);
	viewer.setLocationRelativeTo(null);
	viewer.setVisible(true);
    }

}