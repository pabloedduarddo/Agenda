package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.DAO;
import util.Validador;

public class Agenda extends JFrame {
	// Criação de objetos (JDBC)
	DAO dao = new DAO();
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtID;
	private JTextField txtEmail;
	private JTextField txtNome;
	private JTextField txtFone;
	private JLabel lblStatus;
	private JButton btnCreate;
	private JButton btnBuscar;
	private JButton btnUpdate;
	private JButton btnDelete;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Agenda frame = new Agenda();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Agenda() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				status();
			}
		});
		setResizable(false);
		setTitle("Agenda de contatos");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Agenda.class.getResource("/img/notepad.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(192, 192, 192));
		contentPane.setBorder(null);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setBounds(32, 25, 46, 14);
		contentPane.add(lblNewLabel);

		txtID = new JTextField();
		txtID.setEditable(false);
		txtID.setBounds(73, 22, 121, 20);
		contentPane.add(txtID);
		txtID.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Nome:");
		lblNewLabel_1.setBounds(32, 50, 46, 14);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_3 = new JLabel("Fone:");
		lblNewLabel_3.setBounds(32, 105, 46, 14);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_5 = new JLabel("E-mail:");
		lblNewLabel_5.setBounds(32, 133, 46, 14);
		contentPane.add(lblNewLabel_5);

		txtEmail = new JTextField();
		txtEmail.setBounds(73, 130, 246, 20);
		contentPane.add(txtEmail);
		txtEmail.setColumns(10);
		//Uso do PlainDocument para limpar os campos
		txtEmail.setDocument(new Validador (30));

		txtNome = new JTextField();
		txtNome.setBounds(73, 50, 246, 20);
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		//Uso do PlainDocument para limpar os campos
		txtNome.setDocument(new Validador (30));

		txtFone = new JTextField();
		txtFone.setBounds(73, 102, 121, 20);
		contentPane.add(txtFone);
		txtFone.setColumns(10);
		//Uso do PlainDocument para limpar os campos
		txtFone.setDocument(new Validador (15));

		btnCreate = new JButton("");
		btnCreate.setEnabled(false);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// botão adicionar contato
				adicionarContato();
			}
		});
		btnCreate.setToolTipText("Adicionar Contato");
		btnCreate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCreate.setContentAreaFilled(false);
		btnCreate.setBorder(null);
		btnCreate.setIcon(new ImageIcon(Agenda.class.getResource("/img/add.png")));
		btnCreate.setBounds(30, 171, 48, 48);
		contentPane.add(btnCreate);

		lblStatus = new JLabel("");
		lblStatus.setIcon(new ImageIcon(Agenda.class.getResource("/img/dboff1.png")));
		lblStatus.setBounds(493, 202, 48, 48);
		contentPane.add(lblStatus);

		JButton btnSobre = new JButton("");
		btnSobre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// as linhas abaixo fazem o link entre o JFrame e JDialog
				Sobre sobre = new Sobre();
				sobre.setVisible(true);
			}
		});
		btnSobre.setIcon(new ImageIcon(Agenda.class.getResource("/img/about.png")));
		btnSobre.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSobre.setBorder(null);
		btnSobre.setBackground(new Color(240, 240, 240));
		btnSobre.setBounds(493, 22, 48, 48);
		contentPane.add(btnSobre);

		JButton btnLimpar = new JButton("");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Evento clicar no botão
				limparCampos();
			}
		});
		btnLimpar.setContentAreaFilled(false);
		btnLimpar.setBorderPainted(false);
		btnLimpar.setToolTipText("Limpar os campos");
		btnLimpar.setIcon(new ImageIcon(Agenda.class.getResource("/img/boracha.png")));
		btnLimpar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLimpar.setBounds(204, 171, 48, 48);
		contentPane.add(btnLimpar);

		btnBuscar = new JButton("");
		btnBuscar.setContentAreaFilled(false);
		btnBuscar.setBorder(null);
		btnBuscar.setIcon(new ImageIcon(Agenda.class.getResource("/img/lupa.png")));
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarContato();
			}
		});
		btnBuscar.setBounds(329, 25, 46, 48);
		contentPane.add(btnBuscar);

		// Substituir i clique pela tecla <enter> em um botão
		getRootPane().setDefaultButton(btnBuscar);

		btnUpdate = new JButton("");
		btnUpdate.setEnabled(false);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editarContato();
			}
		});
		btnUpdate.setContentAreaFilled(false);
		btnUpdate.setBorder(null);
		btnUpdate.setBorderPainted(false);
		btnUpdate.setIcon(new ImageIcon(Agenda.class.getResource("/img/up.png")));
		btnUpdate.setBounds(88, 171, 48, 48);
		contentPane.add(btnUpdate);

		btnDelete = new JButton("");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excluirContato();
			}
		});
		btnDelete.setEnabled(false);
		btnDelete.setContentAreaFilled(false);
		btnDelete.setBorderPainted(false);
		btnDelete.setBorder(null);
		btnDelete.setIcon(new ImageIcon(Agenda.class.getResource("/img/delet.png")));
		btnDelete.setBounds(146, 171, 48, 48);
		contentPane.add(btnDelete);

	} // fim do construtor

	/**
	 * Método para verificar o status de conexão
	 */
	private void status() {
		// System.out.println("teste janela ativada");
		try {
			// Abrir a conexão com o banco
			con = dao.conectar();
			if (con == null) {// Mudar o ícone da jlabel
				lblStatus.setIcon(new ImageIcon(Agenda.class.getResource("/img/dboff1.png")));
			} else {
				lblStatus.setIcon(new ImageIcon(Agenda.class.getResource("/img/dbon11.png")));
			}
			// Fechar a conexão
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}// fim do método status

	/**
	 * Método para adicionar um contato no banco
	 */
	private void adicionarContato() {
		System.out.println("Teste do botão adicionar");

		// validação de campos obrigatórios
		if (txtNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o nome do contato");
			txtNome.requestFocus();
		} else if (txtFone.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o telefone do contato");
			{
				txtFone.requestFocus();

			}
		} else {
			// lógica principal
			// a linha abaixo cria uma variável de nome create que recebe o código SQL
			String create = "insert into contatos (nome,fone,email) values (?,?,?)";
			// tratamento de exceção
			try {
				// Abrir a conexão com o banco
				con = dao.conectar();
				// Uso da classe PreparedStatement para executar a função sql e replicar no
				// banco
				pst = con.prepareStatement(create);
				// setar(definir) os parâmetros (?,?,?) de acordo com o preenchimento das caixas
				// de texto
				pst.setString(1, txtNome.getText());
				pst.setString(2, txtFone.getText());
				pst.setString(3, txtEmail.getText());
				// Executar a instrução sql (query)
				pst.executeUpdate();
				// Confirmar para o usuário
				JOptionPane.showMessageDialog(null, "Contato adicionado com sucesso!");
				// Limpar os campos

				limparCampos();

				// Fechar a conexão com o banco
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}// fim do método adicionarContato

	/**
	 * Método responsável por limpar os campos
	 */
	private void buscarContato() {
		// Validação da busca (obrigar o usuário a preencher um nome)
		if (txtNome.getText().isEmpty()) {
			JOptionPane.showInternalMessageDialog(null, "Digite o nome do contato");
			txtNome.requestFocus(); // Setar o cursor na caixa de texto
		} else {

			// System.out.println("Teste do botão buscar");
			String read = "select * from contatos where nome = ?";
			try {
				// Abrir a conexão
				con = dao.conectar();
				// Prepara a execução da query
				pst = con.prepareStatement(read);
				// Pegar o conteúdo da caixa de texto e substituir o parâmetro "?"
				pst.setString(1, txtNome.getText());
				// Uso do ResultSet para obter os dados do contatos
				ResultSet rs = pst.executeQuery();
				// Se existir um contato cadastrado
				if (rs.next()) {
					// Preencher as caixas de texto com o fone e email
					// ATENÇÃO O NOME (Já foi preenchido)
					txtID.setText(rs.getString(1));// 1 ID
					txtFone.setText(rs.getString(3));// 3 FONE
					txtEmail.setText(rs.getString(4));// 4 EMAIL
					// Desativar o botão adicionar
					btnCreate.setEnabled(false);
					//Desativar o botão buscar
					btnBuscar.setEnabled(false);
					// Ativar os botões editar e excluir
					btnUpdate.setEnabled(true);
					btnDelete.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(null, "Contato inexistente");
					// Ativar o botão adicionar
					btnCreate.setEnabled(true);
					// Desativar o botão buscar
					btnBuscar.setEnabled(false);
				}
				// NUNCA ESQUECER DE FECHAR A CONEXÃO
				con.close();

			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}

	/**
	 * Método responsável pela edição dos dados de um contato
	 */
	private void editarContato() {
		// System.out.println("teste do botão editar");
		// Validação de campos obrigatórios
		if (txtNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o nome do contato");
			txtNome.requestFocus();
		} else if (txtFone.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o telefone");
			txtFone.requestFocus();
		} else {
			// Lógica principal (CRUD Update)
			// Criando uma variaável string que irá receber a query
			String update = "update contatos set nome = ?, fone = ?, email = ? where id = ?";
			// Tratamento de exceções
			try {
				// Abrir a conexão com o banco
				con = dao.conectar();
				// Preparar a query(substituir ????)
				pst = con.prepareStatement(update);
				pst.setString(1, txtNome.getText());
				pst.setString(2, txtFone.getText());
				pst.setString(3, txtEmail.getText());
				pst.setString(4, txtID.getText());
				// Executar o update no banco
				pst.executeUpdate();
				// Confirmar para o usuário
				JOptionPane.showMessageDialog(null, "Dados do contato alterado com sucesso");
				// NUNCA esquecer de fechar a conexão
				con.close();
				// Limpar os campos
				limparCampos();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
	
	/**
	 * Método responsável por excluir um contato
	 */
	private void excluirContato() {
		//System.out.println("teste do botão excluir");
		//Confirmação de exclusão
		//A variável confirma captura a opção escolhida do JOptionPane
		int confirma = JOptionPane.showConfirmDialog(null, "Confirmar a exclusão deste contato?", "ATENÇÃO", JOptionPane.YES_NO_OPTION);
		if (confirma == JOptionPane.YES_OPTION) {
			//QUERY	 (INSTRUÇÃO SQL)
			String delete = "delete from contatos where id=?";
			//Tratamento de exceções
			try {
				//Abrir a conexão
				con = dao.conectar();
				//Preparar a query (instrução sql)
				pst = con.prepareStatement(delete);
				pst.setString(1, txtID.getText());
				pst.executeUpdate();
				//Limpar os campos
				limparCampos();
				//Exibir mensagem ao usuário
				JOptionPane.showMessageDialog(null, "Contato excluído");
				
				//Fechar conexão
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * Método resposável por limpar os campos
	 */
	private void limparCampos() {
		txtID.setText(null);
		txtNome.setText(null);
		txtFone.setText(null);
		txtEmail.setText(null);
		// Setar botões
		btnCreate.setEnabled(false);
		btnUpdate.setEnabled(false);
		btnDelete.setEnabled(false);
		btnBuscar.setEnabled(true);
	}// fim do método limpar campos
}// fim do código
