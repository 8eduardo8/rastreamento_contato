package br.ufg.inf.rastreamento_contato;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.xml.crypto.Data;

import br.ufg.inf.rastreamento_contato.DataSet.Agente;
import br.ufg.inf.rastreamento_contato.DataSet.Posicao;
import br.ufg.inf.rastreamento_contato.DataSet.Zona;

public class DrawShapesExample {

	static DataSet dataSet;
	public static final int frameWidth = DataSet.linhas * 10;
	public static final int frameHeight = DataSet.colunas * 10;
	static int sleep = 100;

	public static int t;

	public static void main(String[] args) {

		// Create a frame

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add a component with a custom paint method

		// Display the frame

		frame.setSize(frameWidth + 30, frameHeight + 50);
		frame.setVisible(true);

		dataSet = new DataSet();
		dataSet.init();

		// SELECIONA UM AGENTE PARA SER INFECTADO
		dataSet.agentes.get(0).infectado = true;

		frame.add(new CustomPaintComponent());

		for (t = 0; t <= DataSet.tempos; t++) {
			System.out.println("Desenhando: " + t);

			frame.repaint();
			frame.setVisible(true);

			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * To draw on the screen, it is first necessary to subclass a Component and
	 * override its paint() method. The paint() method is automatically called
	 * by the windowing system whenever component's area needs to be repainted.
	 */
	static class CustomPaintComponent extends Component {

		public void paint(Graphics g) {

			// Retrieve the graphics context; this object is used to paint
			// shapes

			Graphics2D g2d = (Graphics2D) g;

			g2d.setColor(Color.black);
			g2d.fillRect(0, 0, frameWidth, frameHeight);

			for (Zona zona : dataSet.zonas) {
				if (zona.risco == 1) {
					g2d.setColor(Color.GRAY);
				} else if (zona.risco == 2) {
					g2d.setColor(Color.ORANGE);
				} else if (zona.risco == 3) {
					g2d.setColor(Color.RED);
				}
				if (zona.infectado > 0) {
					g2d.setColor(Color.BLUE);
				}

				g2d.fillRect(zona.x0 * 10, zona.y0 * 10, (zona.x1 - zona.x0) * 10, (zona.y1 - zona.y0) * 10);
			}

			for (Agente agente : dataSet.agentes) {
				if (agente.infectado) {
					g2d.setColor(Color.BLUE);
				} else {
					g2d.setColor(Color.WHITE);
				}
				// g2d.setColor(agente.color);
				Posicao atual = agente.movimentos.get(t);
				g2d.fillOval(atual.posX * 10, atual.posY * 10, 10, 10);

				g2d.drawOval(atual.destinoX * 10, atual.destinoY * 10, 10, 10);

			}

			// DIMINIU AS ZONAS INFECTADAS
			for (Zona zona : dataSet.zonas) {
				if (zona.infectado > 0) {
					zona.infectado--;

					// VERIFICA SE TEM ALGUM AGENTE NA ZONA INFECTADAS
					for (Agente agente : dataSet.agentes) {
						Posicao posicao = agente.movimentos.get(t);
						if (posicao.posX == posicao.destinoX && posicao.posY == posicao.destinoY) {
							if (posicao.posX >= zona.x0 && posicao.posX < zona.x1 && posicao.posY >= zona.y0
									&& posicao.posY < zona.y1) {
								agente.infectado = true;
							}
						}
					}
				}
			}

			// VERIFICA SE ALGUM INFECTADO CHEGOU PERTO DE ALGUEM
			int infectados = 0;
			for (Agente agente : dataSet.agentes) {
				if (agente.infectado) {
					infectados++;
					// CALCULA A DISTÂNCIA PARA OS DEMAIS AGENTES
					for (Agente x : dataSet.agentes) {
						if (proximo(x.movimentos.get(t), agente.movimentos.get(t))) {
							x.infectado = true;
						}
					}

					// VERIFICA SE O INFECTADO ESTÁ PARADO EM UM LOCAL DE RISCO
					Posicao posicao = agente.movimentos.get(t);
					if (posicao.posX == posicao.destinoX && posicao.posY == posicao.destinoY) {
						// VERIFICA SE TEM ALGUMA ZONA
						for (Zona zona : dataSet.zonas) {
							if (posicao.posX >= zona.x0 && posicao.posX < zona.x1 && posicao.posY >= zona.y0
									&& posicao.posY < zona.y1) {
								zona.infectado = zona.risco * 50;
							}
						}
					}
				}
			}

			g2d.setColor(Color.WHITE);
			g2d.drawString(
					"Infectados: " + infectados + " / " + dataSet.agentes.size()
							+ new DecimalFormat("' ('0.0'%)'").format((infectados * 100f) / dataSet.agentes.size()),
					10, 20);
			g2d.drawString("Tempo: " + t + new DecimalFormat("' ('0.0'%)'").format((t * 100f) / dataSet.tempos), 10,
					40);
			g2d.drawString("Probabilidade de Movimentar: "
					+ new DecimalFormat("0.0'%'").format(dataSet.probabilidadeMover * 100f), 10, 60);

			g2d.dispose();

		}

	}

	public static boolean proximo(Posicao pos1, Posicao pos2) {
		if (Math.abs(pos1.posX - pos2.posX) <= 0 && Math.abs(pos1.posY - pos2.posY) <= 0)
			return true;
		return false;
	}

}