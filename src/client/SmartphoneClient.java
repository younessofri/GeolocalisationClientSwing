package client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.Font;
import javax.swing.JTextField;
import dao.IDaoSmartphoneRemote;
import dao.IDaoUserRemote;
import entities.Smartphone;
import entities.User;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import java.awt.Color;

public class SmartphoneClient {

	private static IDaoUserRemote lookUpUser() throws NamingException {
		Hashtable<Object, Object> config = new Hashtable<Object, Object>();
		config.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		config.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
		final Context context = new InitialContext(config);
		return (IDaoUserRemote) context.lookup("ejb:GeolocalisationEAR/GeolocalisationServer/UserService!dao.IDaoUserRemote");		
	}

	private static IDaoSmartphoneRemote lookUpSmartphone() throws NamingException {
		Hashtable<Object, Object> config = new Hashtable<Object, Object>();
		config.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		config.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
		final Context context = new InitialContext(config);
		return (IDaoSmartphoneRemote) context.lookup("ejb:GeolocalisationEAR/GeolocalisationServer/SmartphoneService!dao.IDaoSmartphoneRemote");

		
	}

	private IDaoUserRemote daoUser;
	private IDaoSmartphoneRemote daoSmartphone;
	private JFrame frmSmartphone;
	private JTextField imeiTxt;
	private JScrollPane scrollPane;
	private JTable table;
	private JComboBox userComboBox;
	private String[] comboUserList;

	private void fillTable() throws RemoteException {

		List<Smartphone> smartphones = daoSmartphone.getAll();
		List<String[]> data = new ArrayList();

		for (Smartphone s : smartphones) {
			data.add(new String[] { s.getImei(), s.getUser().getNom()+" "+s.getUser().getPrenom(), String.valueOf(s.getId())});
		}

		List<String> columns = new ArrayList<String>();
		columns.add("IMEI");
		columns.add("Utilisateur");
		columns.add("id");

		TableModel tableModel = new DefaultTableModel(data.toArray(new Object[][] {}), columns.toArray());
		table.setModel(tableModel);
		table.removeColumn(table.getColumnModel().getColumn(2));
	}

	private void clearFields() {
		imeiTxt.setText("");
		userComboBox.setSelectedIndex(-1);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SmartphoneClient window = new SmartphoneClient();
					window.frmSmartphone.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SmartphoneClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		try {
			daoUser = lookUpUser();
			daoSmartphone = lookUpSmartphone();
		} catch (NamingException e3) {
			e3.printStackTrace();
		}

		frmSmartphone = new JFrame();
		frmSmartphone.getContentPane().setBackground(new Color(255, 255, 204));
		frmSmartphone.setTitle("Smartphone UI");
		frmSmartphone.setBounds(100, 100, 600, 350);
		frmSmartphone.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSmartphone.getContentPane().setLayout(null);
		frmSmartphone.setLocationRelativeTo(null);


		JLabel lblNewLabel = new JLabel("Smartphones");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 564, 43);
		frmSmartphone.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("IMEI");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1.setBounds(10, 90, 100, 14);
		frmSmartphone.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Utilisateur");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2.setBounds(230, 90, 100, 14);
		frmSmartphone.getContentPane().add(lblNewLabel_2);

		imeiTxt = new JTextField();
		imeiTxt.setBounds(10, 107, 203, 20);
		frmSmartphone.getContentPane().add(imeiTxt);
		imeiTxt.setColumns(10);

		JButton ajouterBtn = new JButton("Ajouter");
		ajouterBtn.setBounds(457, 138, 103, 23);
		frmSmartphone.getContentPane().add(ajouterBtn);

		JButton modifierBtn = new JButton("Modifier");
		modifierBtn.setBounds(457, 199, 103, 23);
		frmSmartphone.getContentPane().add(modifierBtn);

		JButton supprimerBtn = new JButton("Supprimer");
		supprimerBtn.setBounds(457, 260, 103, 23);
		frmSmartphone.getContentPane().add(supprimerBtn);

		table = new JTable() {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};

		try {
			fillTable();
		} catch (RemoteException e2) {
			e2.printStackTrace();
		}
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(10, 138, 423, 145);
		frmSmartphone.getContentPane().add(scrollPane);
		
		List<User> users = daoUser.getAll();
		comboUserList = new String[users.size()];
		for(int i=0; i < users.size(); i++) {
			comboUserList[i] = users.get(i).getNom()+" "+users.get(i).getPrenom();
		}
		userComboBox = new JComboBox(comboUserList);
		userComboBox.setBounds(230, 107, 203, 21);
		frmSmartphone.getContentPane().add(userComboBox);

		ajouterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String imei = imeiTxt.getText();
				int idIndex = userComboBox.getSelectedIndex();

				if (imei.isEmpty() || idIndex == -1)
					JOptionPane.showConfirmDialog(null, "Tous les champs sont obligatoires!", "Problème",
							JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				else {
					User u = users.get(idIndex);
					Smartphone smartphone = new Smartphone(imei, u);
					try {
						daoSmartphone.create(smartphone);
						fillTable();
						clearFields();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}

			}
		});

		modifierBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int decision = JOptionPane.showConfirmDialog(null, "Modifier?", "Confirmation",
						JOptionPane.YES_NO_OPTION);
				if (decision == JOptionPane.YES_OPTION) {
					String imei = imeiTxt.getText();
					int idIndex = userComboBox.getSelectedIndex();

					if (imei.isEmpty() || idIndex == -1)
						JOptionPane.showConfirmDialog(null, "Tous les champs sont obligatoires!", "Problème",
								JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
					else {
						User u = users.get(idIndex);
						Smartphone smartphone = new Smartphone(imei, u);
						
						int id = Integer.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 2).toString());
						smartphone.setId(id);
						try {
							daoSmartphone.update(smartphone);
							fillTable();
							clearFields();
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});

		supprimerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int decision = JOptionPane.showConfirmDialog(null, "Supprimer?", "Confirmation",
						JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
				if (decision == JOptionPane.YES_OPTION) {
					Smartphone smarphone = new Smartphone();
					int id = Integer.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 2).toString());
					smarphone.setId(id);

					try {
						daoSmartphone.delete(smarphone);
						fillTable();
						clearFields();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		table.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				int id = Integer.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 2).toString());
				Smartphone smartphone = daoSmartphone.getById(id);
				imeiTxt.setText(smartphone.getImei());
				userComboBox.setSelectedItem(smartphone.getUser().getNom()+" "+smartphone.getUser().getPrenom());
				
				for(int i=0; i<comboUserList.length; i++) {
					if(smartphone.getUser().getNom()+" "+smartphone.getUser().getPrenom() == comboUserList[i]) {
						userComboBox.setSelectedIndex(i);
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}
}
