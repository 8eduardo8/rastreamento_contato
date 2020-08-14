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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.google.gson.Gson;

public class Parametro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton btnSair = new JButton("Sair");
	private JButton btnExecutar = new JButton("Iniciar");
	private JButton btnAbrirArquivo = new JButton("Selecionar Arquivo");

	private JTextField txtVelocidade = new JTextField();
	private JTextField txtTempoDescontaminacao = new JTextField();
	private JTextField txtFator = new JTextField();
	private JCheckBox ckContatoPrimario = new JCheckBox();
	private JCheckBox ckContatoPassagem = new JCheckBox();
	private JCheckBox ckContatoObjeto = new JCheckBox();

	private JLabel lblVelocidade = new JLabel("Aceleracao da Execucao :");
	private JLabel lblTempoDescontaminacao = new JLabel("Tempo de Descontaminação :");
	private JLabel lblFator = new JLabel("Fator :");
	private JLabel lblContatoPrimario = new JLabel("Identificar Contatos Indiretos :");
	private JLabel lblContatoPassagem = new JLabel("Identificar Contato de Passagem :");
	private JLabel lblContatoObjeto = new JLabel("Identificar Contato em Objetos :");

	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				new Parametro();
			}
		}).start();
	}

	public Parametro() {
		setTitle("Rastreamento de Contatos");
		setSize(500, 400);
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
		btnExecutar.setBounds(360, 300, 120, 25);
		btnSair.setBounds(360, 330, 120, 25);
		btnAbrirArquivo.setBounds(10, 300, 200, 25);

		lblVelocidade.setBounds(10, 10, 299, 20);
		lblTempoDescontaminacao.setBounds(10, 40, 299, 20);

		lblContatoPrimario.setBounds(10, 70, 299, 20);
		lblFator.setBounds(350, 70, 100, 20);

		lblContatoPassagem.setBounds(10, 130, 299, 20);
		lblContatoObjeto.setBounds(10, 160, 299, 20);

		txtVelocidade.setBounds(300, 10, 50, 20);
		txtTempoDescontaminacao.setBounds(300, 40, 50, 20);
		txtFator.setBounds(400, 70, 50, 20);
		ckContatoPrimario.setBounds(300, 70, 50, 20);

		ckContatoPassagem.setBounds(300, 130, 50, 20);
		ckContatoObjeto.setBounds(300, 160, 50, 20);

		txtVelocidade.setText("2");
		txtTempoDescontaminacao.setText("50");
		txtFator.setText("1");

		add(btnSair);
		add(btnExecutar);
		add(btnAbrirArquivo);

		add(lblVelocidade);
		add(lblTempoDescontaminacao);
		add(lblFator);
		add(lblContatoPrimario);
		add(lblContatoPassagem);
		add(lblContatoObjeto);

		add(txtVelocidade);
		add(txtTempoDescontaminacao);
		add(txtFator);
		add(ckContatoPrimario);
		add(ckContatoPassagem);
		add(ckContatoObjeto);

		repaint();
		setVisible(true);
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

		btnAbrirArquivo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAbrirArquivo(e);
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
			boolean contatoSecundario = ckContatoPrimario.isSelected();

			double fator = Double.parseDouble(txtFator.getText().replace(",", "."));
			if (fator > 1)
				fator = 1;

			String texto = "";
			List<String> linhas = Files.readAllLines(Paths.get(new File("dataset.json").getAbsolutePath()));
			for (String s : linhas) {
				texto += s;
			}
			Gson gson = new Gson();
			DataSet dataset = gson.fromJson(texto, DataSet.class);

			new Thread(new Runnable() {
				@Override
				public void run() {
					new DrawShapesExample(new JFrame(), dataset, velocidade, descontaminacao, contatoSecundario)
							.executar();
				}
			}).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void btnAbrirArquivo(ActionEvent e) {

		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			// This is where a real application would open the file.
			System.out.println("Opening: " + file.getName());
		} else {
			System.out.println("Open command cancelled by user.");
		}
	}

}
