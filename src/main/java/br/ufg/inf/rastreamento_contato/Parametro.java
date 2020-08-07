package br.ufg.inf.rastreamento_contato;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.google.gson.Gson;

public class Parametro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton btnSair = new JButton("Sair");
	private JButton btnExecutar = new JButton("Iniciar");

	private JTextField txtVelocidade = new JTextField();
	private JTextField txtTempoDescontaminacao = new JTextField();
	private JCheckBox ckContatoSecundario = new JCheckBox();

	private JLabel lblA = new JLabel("Velocidade de Execução :");
	private JLabel lblB = new JLabel("Tempo de Descontaminação :");
	private JLabel lblC = new JLabel("Somente Contato Primario :");

	public static void main(String[] args) {
		new Parametro();
	}

	public Parametro() {
		setTitle("Rastreamento de Contatos");
		setSize(400, 200);
		setLocation(new Point(300, 200));
		setLayout(null);
		setResizable(false);
		setVisible(true);

		initComponent();
		initEvent();
	}

	private void initComponent() {

		// txtVelocidade.addKeyListener(adapterNumber(txtVelocidade));
		// txtTempoDescontaminacao.addKeyListener(adapterNumber(txtTempoDescontaminacao));

		btnSair.setBounds(300, 130, 80, 25);
		btnExecutar.setBounds(300, 100, 80, 25);

		txtVelocidade.setBounds(220, 10, 50, 20);
		txtTempoDescontaminacao.setBounds(220, 35, 50, 20);
		ckContatoSecundario.setBounds(220, 65, 50, 20);

		lblA.setBounds(20, 10, 200, 20);
		lblB.setBounds(20, 35, 200, 20);
		lblC.setBounds(20, 65, 200, 20);

		add(btnSair);
		add(btnExecutar);

		add(lblA);
		add(lblB);
		add(lblC);

		add(txtVelocidade);
		add(txtTempoDescontaminacao);
		add(ckContatoSecundario);
	}

	public KeyAdapter adapterNumber(JTextField tf) {
		return new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				String value = tf.getText();
				int l = value.length();
				if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') {
					tf.setEditable(true);
				} else {
					tf.setEditable(false);
				}
			}
		};
	}

	private void initEvent() {

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});

		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSair(e);
			}
		});

		btnExecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnExecutar(e);
			}
		});
	}

	private void btnSair(ActionEvent evt) {
		System.exit(0);
	}

	private void btnExecutar(ActionEvent evt) {
		try {

			int velocidade = Integer.parseInt(txtVelocidade.getText());
			int descontaminacao = Integer.parseInt(txtTempoDescontaminacao.getText());
			boolean contatoSecundario = ckContatoSecundario.isSelected();

			String texto = "";
			List<String> linhas = Files.readAllLines(Paths.get(new File("dataset.json").getAbsolutePath()));
			for (String s : linhas) {
				texto += s;
			}
			Gson gson = new Gson();
			DataSet dataset = gson.fromJson(texto, DataSet.class);

			// DataSet dataset = new DataSet();
			// dataset.init();
			this.dispose();

			new DrawShapesExample(this, dataset, velocidade, descontaminacao, contatoSecundario).executar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
