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
import com.toedter.calendar.JDateChooser;
import dao.IDaoUserRemote;
import entities.User;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Color;

public class UserClient {

	private static IDaoUserRemote lookUpUser() throws NamingException {
		Hashtable<Object, Object> config = new Hashtable<Object, Object>();
		config.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		config.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
		final Context context = new InitialContext(config);
		return (IDaoUserRemote) context.lookup("ejb:GeolocalisationEAR/GeolocalisationServer/UserService!dao.IDaoUserRemote");		
	}


	private IDaoUserRemote daoUser;
	private JFrame frmUser;
	private JTextField nomTxt;
	private JTextField prenomTxt;
	private JTextField telephoneTxt;
	private JDateChooser dateChooser;
	private JScrollPane scrollPane;
	private JTable table;
	private JTextField emailTxt;

	private void fillTable() throws RemoteException {

		List<User> users = daoUser.getAll();
		List<String[]> data = new ArrayList();

		for (User u : users) {
			data.add(new String[] { u.getNom(), u.getPrenom(), u.getTelephone(), u.getEmail(),
					String.valueOf(u.getDateNaissance()), String.valueOf(u.getId()) });
		}

		List<String> columns = new ArrayList<String>();
		columns.add("Nom");
		columns.add("Prénom");
		columns.add("Téléphone");
		columns.add("Email");
		columns.add("Date Naissance");
		columns.add("id");

		TableModel tableModel = new DefaultTableModel(data.toArray(new Object[][] {}), columns.toArray());
		table.setModel(tableModel);
		table.removeColumn(table.getColumnModel().getColumn(5));
	}

	private void clearFields() {
		nomTxt.setText("");
		prenomTxt.setText("");
		telephoneTxt.setText("");
		emailTxt.setText("");
		dateChooser.setDate(null);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserClient window = new UserClient();
					window.frmUser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		try {
			daoUser = lookUpUser();
		} catch (NamingException e3) {
			e3.printStackTrace();
		}

		frmUser = new JFrame();
		frmUser.getContentPane().setBackground(new Color(255, 255, 204));
		frmUser.setTitle("Client UI");
		frmUser.setBounds(100, 100, 600, 350);
		frmUser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUser.getContentPane().setLayout(null);
		frmUser.setLocationRelativeTo(null);


		JLabel lblNewLabel = new JLabel("Utilisateur");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 564, 43);
		frmUser.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Nom");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1.setBounds(10, 75, 100, 14);
		frmUser.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Pr\u00E9nom");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_2.setBounds(160, 75, 100, 14);
		frmUser.getContentPane().add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("T\u00E9l\u00E9phone");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_3.setBounds(313, 75, 100, 14);
		frmUser.getContentPane().add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Date de naissance");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_4.setBounds(227, 123, 132, 14);
		frmUser.getContentPane().add(lblNewLabel_4);

		nomTxt = new JTextField();
		nomTxt.setBounds(10, 92, 120, 20);
		frmUser.getContentPane().add(nomTxt);
		nomTxt.setColumns(10);

		prenomTxt = new JTextField();
		prenomTxt.setBounds(160, 92, 120, 20);
		frmUser.getContentPane().add(prenomTxt);
		prenomTxt.setColumns(10);

		telephoneTxt = new JTextField();
		telephoneTxt.setBounds(313, 92, 120, 20);
		frmUser.getContentPane().add(telephoneTxt);
		telephoneTxt.setColumns(10);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(227, 139, 206, 20);
		frmUser.getContentPane().add(dateChooser);

		JButton ajouterBtn = new JButton("Ajouter");
		ajouterBtn.setBounds(452, 167, 103, 23);
		frmUser.getContentPane().add(ajouterBtn);

		JButton modifierBtn = new JButton("Modifier");
		modifierBtn.setBounds(452, 211, 103, 23);
		frmUser.getContentPane().add(modifierBtn);

		JButton supprimerBtn = new JButton("Supprimer");
		supprimerBtn.setBounds(452, 260, 103, 23);
		frmUser.getContentPane().add(supprimerBtn);

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
		scrollPane.setBounds(10, 170, 423, 113);
		frmUser.getContentPane().add(scrollPane);

		emailTxt = new JTextField();
		emailTxt.setBounds(10, 139, 184, 20);
		frmUser.getContentPane().add(emailTxt);
		emailTxt.setColumns(10);

		JLabel lblNewLabel_5 = new JLabel("Email");
		lblNewLabel_5.setBounds(10, 123, 46, 14);
		frmUser.getContentPane().add(lblNewLabel_5);

		ajouterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nom = nomTxt.getText();
				String prenom = prenomTxt.getText();
				String telephone = telephoneTxt.getText();
				String email = telephoneTxt.getText();
				Date dateNaissance = dateChooser.getDate();

				if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty() || email.isEmpty()
						|| dateNaissance == null)
					JOptionPane.showConfirmDialog(null, "Tous les champs sont obligatoires!", "Problème",
							JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
				else {
					User user = new User(nom, prenom, telephone, email, dateNaissance);
					try {
						daoUser.create(user);
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
					String nom = nomTxt.getText();
					String prenom = prenomTxt.getText();
					String telephone = telephoneTxt.getText();
					String email = emailTxt.getText();
					Date dateNaissance = dateChooser.getDate();

					if (nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty() || email.isEmpty()
							|| dateNaissance == null)
						JOptionPane.showConfirmDialog(null, "Tous les champs sont obligatoires!", "Problème",
								JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
					else {
						User user = new User(nom, prenom, telephone, email, dateNaissance);

						int id = Integer.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 5).toString());
						user.setId(id);
						try {
							daoUser.update(user);
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
					User user = new User();
					int id = Integer.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 5).toString());
					user.setId(id);

					try {
						daoUser.delete(user);
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
				int id = Integer.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 5).toString());

				User user = daoUser.getById(id);

				nomTxt.setText(user.getNom());
				prenomTxt.setText(user.getPrenom());
				telephoneTxt.setText(String.valueOf(user.getTelephone()));
				emailTxt.setText(String.valueOf(user.getEmail()));
				dateChooser.setDate(user.getDateNaissance());

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
