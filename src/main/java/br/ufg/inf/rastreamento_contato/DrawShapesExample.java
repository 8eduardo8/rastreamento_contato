package br.ufg.inf.rastreamento_contato;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JFrame;

import com.google.gson.Gson;

import br.ufg.inf.rastreamento_contato.model.Individuo;
import br.ufg.inf.rastreamento_contato.model.Objeto;
import br.ufg.inf.rastreamento_contato.model.Posicao;

public class DrawShapesExample {

	JFrame frame;
	int frameWidth;
	int frameHeight;
	DataSet dataSet;
	int velocidade;
	int tempoContaminacao;
	boolean somenteContatoPrimario;
	boolean ponderarContato = true;

	int t;

	public static void main(String[] args) throws Exception {

		// DataSet dataset = new DataSet();
		// dataset.init();
		//
		// // salvar gson em um dataset
		// Gson gson = new Gson();
		// String json = gson.toJson(dataset);
		//
		// try {
		// File file = new File(new
		// SimpleDateFormat("yyyyMMddHHmmss'.json'").format(new Date()));
		// System.out.println(file.getAbsolutePath());
		// FileWriter fw = new FileWriter(file);
		// fw.write(json);
		// fw.flush();
		// fw.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		String texto = "";
		List<String> linhas = Files.readAllLines(Paths.get(new File("dataset.json").getAbsolutePath()));
		for (String s : linhas) {
			texto += s;
		}
		Gson gson = new Gson();
		DataSet dataset = gson.fromJson(texto, DataSet.class);

		new DrawShapesExample(new JFrame(), dataset, 100, 50, false).executar();

		// new Parametro();

	}

	public DrawShapesExample(JFrame frame, DataSet dataSet, int velocidade, int tempoContaminacao,
			boolean somenteContatoPrimario) {
		this.frame = frame;
		this.dataSet = dataSet;
		this.velocidade = velocidade;
		this.tempoContaminacao = tempoContaminacao;
		this.somenteContatoPrimario = somenteContatoPrimario;
	}

	public void executar() {
		// Create a frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add a component with a custom paint method
		frameWidth = DataSet.linhas * 10;
		frameHeight = DataSet.colunas * 10;

		// Display the frame
		frame.setSize(frameWidth + 30, frameHeight + 50);
		frame.setVisible(true);

		// SELECIONA UM AGENTE PARA SER INFECTADO
		dataSet.individuos.get(0).infectado = true;
		dataSet.individuos.get(0).qtdContatoObjetoPrimario = 1;

		frame.add(new CustomPaintComponent());

		for (t = 0; t < DataSet.tempos; t++) {
			System.out.println("Desenhando: " + t);

			frame.repaint();
			frame.setVisible(true);

			try {
				Thread.sleep(velocidade);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class CustomPaintComponent extends Component {

		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {
			// DIMINIU AS ZONAS INFECTADAS
			for (Objeto objeto : dataSet.objetos) {
				if (objeto.infectadoPrimario > 0)
					objeto.infectadoPrimario--;
				if (objeto.infectadoSecundario > 0)
					objeto.infectadoSecundario--;

				for (Individuo individuo : dataSet.individuos) {
					Posicao posicao = individuo.movimentos.get(t);
					if (posicao.posX == posicao.destinoX && posicao.posY == posicao.destinoY) {
						if (posicao.posX >= objeto.x0 && posicao.posX < objeto.x1 && posicao.posY >= objeto.y0
								&& posicao.posY < objeto.y1) {
							// INDIVÃ�DUO ESTÃ� PARADO NO OBJETO
							if (somenteContatoPrimario == false) {
								if (objeto.infectadoSecundario > 0) {
									individuo.qtdContatoObjetoSecundario++;
								}
								if (individuo.qtdContatoObjetoSecundario > 0 || individuo.qtdContatoSecundario > 0) {
									objeto.infectadoSecundario = tempoContaminacao * objeto.risco;
								}
							}

							if (objeto.infectadoPrimario > 0) {
								individuo.qtdContatoObjetoPrimario++;
							}
							if (individuo.qtdContatoObjetoPrimario > 0 || individuo.qtdContatoPrimario > 0) {
								objeto.infectadoPrimario = tempoContaminacao * objeto.risco;
							}
						}
					}
				}
			}
			// VERIFICA SE ALGUM INFECTADO CHEGOU PERTO DE ALGUEM

			for (Individuo individuo : dataSet.individuos) {
				// CALCULA A DISTÃ‚NCIA PARA OS DEMAIS AGENTES
				for (Individuo x : dataSet.individuos) {
					if (proximo(x.movimentos.get(t), individuo.movimentos.get(t))) {
						if (individuo.qtdContatoPrimario > 0 || individuo.qtdContatoObjetoPrimario > 0) {
							x.qtdContatoPrimario++;
						}
						if (somenteContatoPrimario == false) {
							if (individuo.qtdContatoSecundario > 0 || individuo.qtdContatoObjetoSecundario > 0) {
								x.qtdContatoSecundario++;
							}
						}
					}
				}
			}

			desenha(g);
		}

		void desenha(Graphics g) {
			// Retrieve the graphics context; this object is used to paint
			// shapes
			Graphics2D g2d = (Graphics2D) g;

			g2d.setColor(Color.black);
			g2d.fillRect(0, 0, frameWidth, frameHeight);

			for (Objeto objeto : dataSet.objetos) {
				if (objeto.risco == 1) {
					g2d.setColor(Color.GRAY);
				} else if (objeto.risco == 2) {
					g2d.setColor(Color.ORANGE);
				} else if (objeto.risco == 3) {
					g2d.setColor(Color.RED);
				}
				if (objeto.infectadoPrimario > 0 || objeto.infectadoSecundario > 0) {
					g2d.setColor(Color.BLUE);
				}

				g2d.fillRect(objeto.x0 * 10, objeto.y0 * 10, (objeto.x1 - objeto.x0) * 10,
						(objeto.y1 - objeto.y0) * 10);
			}

			int infectados = 0;
			for (Individuo individuo : dataSet.individuos) {
				if (individuo.qtdContatoPrimario > 0 || individuo.qtdContatoSecundario > 0) {
					infectados++;
				} else if (somenteContatoPrimario == false
						&& (individuo.qtdContatoObjetoSecundario > 0 || individuo.qtdContatoSecundario > 0)) {
					infectados++;
				}

				if (individuo.infectado == true) {
					g2d.setColor(Color.RED);
				} else if (individuo.qtdContatoPrimario > 0 || individuo.qtdContatoSecundario > 0
						|| individuo.qtdContatoObjetoPrimario > 0 || individuo.qtdContatoObjetoSecundario > 0) {
					g2d.setColor(Color.GREEN);
				} else {
					g2d.setColor(Color.WHITE);
				}
				// g2d.setColor(individuo.color);
				Posicao atual = individuo.movimentos.get(t);
				g2d.fillOval(atual.posX * 10, atual.posY * 10, 10, 10);

				g2d.drawOval(atual.destinoX * 10, atual.destinoY * 10, 10, 10);
			}
			g2d.setColor(Color.WHITE);
			g2d.drawString(
					"Infectados: " + infectados + " / " + dataSet.individuos.size()
							+ new DecimalFormat("' ('0.0'%)'").format((infectados * 100f) / dataSet.individuos.size()),
					10, 20);
			g2d.drawString("Tempo: " + t + new DecimalFormat("' ('0.0'%)'").format((t * 100f) / DataSet.tempos), 10,
					40);
			g2d.drawString("Probabilidade de Movimentar: "
					+ new DecimalFormat("0.0'%'").format(DataSet.probabilidadeMover * 100f), 10, 60);

			g2d.dispose();
		}

	}

	public static boolean proximo(Posicao pos1, Posicao pos2) {
		if (Math.abs(pos1.posX - pos2.posX) <= 0 && Math.abs(pos1.posY - pos2.posY) <= 0)
			return true;
		return false;
	}

}