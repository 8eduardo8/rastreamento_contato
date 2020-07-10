package br.ufg.inf.rastreamento_contato;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataSet {

	public static final int linhas = 100, colunas = 50;
	public static final int qtdeAgentes = 20;
	public static final int tempos = 10;

	private static final double probabilidadeMover = 1;

	private static final int TIPOMOVIMENTO_ALEATORIO = 1;
	private static final int TIPOMOVIMENTO_DIRECIONAL = 2;
	private static final int tipoMovimento = TIPOMOVIMENTO_DIRECIONAL;

	private static Random random = new Random();

	public List<Agente> agentes;
	public List<Zona> zonas;

	public void init() {
		System.out.println("Criando DatSet");

		zonas = new ArrayList<>();
		zonas.add(new Zona(4, 2, 10, 2, 1));
		zonas.add(new Zona(4, 2, 14, 2, 1));
		zonas.add(new Zona(4, 2, 18, 2, 1));
		zonas.add(new Zona(4, 2, 22, 2, 1));

		zonas.add(new Zona(80, 4, 30, 4, 2));
		zonas.add(new Zona(80, 4, 36, 4, 2));

		zonas.add(new Zona(30, 4, 20, 4, 2));
		zonas.add(new Zona(36, 4, 20, 4, 2));

		zonas.add(new Zona(10, 4, 40, 4, 2));
		zonas.add(new Zona(16, 4, 40, 4, 2));

		zonas.add(new Zona(70, 4, 10, 4, 3));
		zonas.add(new Zona(76, 4, 10, 4, 3));

		zonas.add(new Zona(52, 2, 30, 2, 3));
		zonas.add(new Zona(52, 2, 34, 2, 3));

		zonas.add(new Zona(56, 2, 30, 2, 3));
		zonas.add(new Zona(56, 2, 34, 2, 3));

		zonas.add(new Zona(60, 2, 30, 2, 3));
		zonas.add(new Zona(60, 2, 34, 2, 3));

		zonas.add(new Zona(64, 2, 30, 2, 3));
		zonas.add(new Zona(64, 2, 34, 2, 3));

		agentes = new ArrayList();
		// criando os agentes
		char id = 'A';
		for (int i = 0; i < qtdeAgentes; i++) {
			agentes.add(new Agente(id++, sorteiaPosicao()));
		}

		for (int t = 0; t < tempos; t++) {
			// System.out.println("Iteração: " + t);
			for (Agente agente : agentes) {
				Posicao atual = agente.movimentos.get(agente.movimentos.size() - 1);
				Posicao proximo = new Posicao();
				proximo.posX = atual.posX;
				proximo.posY = atual.posY;
				proximo.destinoX = atual.destinoX;
				proximo.destinoY = atual.destinoY;

				System.out.println(proximo.destinoX + " , " + proximo.destinoY);

				agente.movimentos.add(proximo);

				// sorteia se vai relaizar ou não um movimento
				if (random.nextDouble() < probabilidadeMover) {
					if (agente.chegouDestino) {
						// se tiver chegado ao destino sorteia um novo destino
						Posicao pos = sorteiaPosicao();
						proximo.destinoX = pos.posX;
						proximo.destinoY = pos.posY;
						agente.chegouDestino = false;
					} else {
						// desloca no sentido do destino

						int distanciaX = atual.destinoX - atual.posX;
						int distanciaY = atual.destinoY - atual.posY;
						if (distanciaX == 0 && distanciaY == 0) {
							agente.chegouDestino = true;
						} else {
							if (distanciaX > 0) {
								proximo.posX = atual.posX + 1;
							} else if (distanciaX < 0) {
								proximo.posX = atual.posX - 1;
							}

							if (distanciaY > 0) {
								proximo.posY = atual.posY + 1;
							} else if (distanciaY < 0) {
								proximo.posY = atual.posY - 1;
							}
						}
					}
				}
			}
		}

		// // imprime na tela
		// int t = 1;
		// int encontrado;
		// char charEncontrado = '.';
		// for (int y = 0; y < colunas; y++) {
		// for (int x = 0; x < linhas; x++) {
		// encontrado = 0;
		// charEncontrado = '.';
		// for (Agente agente : agentes) {
		// Posicao atual = agente.movimentos.get(t);
		// if (atual.posX == x && atual.posY == y) {
		// encontrado++;
		// charEncontrado = agente.id;
		// }
		// }
		// if (encontrado > 1) {
		// charEncontrado = '#';
		// }
		// System.out.print(charEncontrado);
		// }
		// System.out.println();
		// }

	}

	public static Posicao sorteiaPosicao() {
		Posicao posicao = new Posicao();
		posicao.posX = random.nextInt(linhas);
		posicao.posY = random.nextInt(colunas);
		return posicao;
	}

	public static class Agente {
		char id;
		Color color;
		public List<Posicao> movimentos;

		boolean chegouDestino;

		public Agente(char id, Posicao posicaoInicial) {
			color = randomColor();
			this.id = id;
			movimentos = new ArrayList();
			movimentos.add(posicaoInicial);
			chegouDestino = true;

		}
	}

	public static class Posicao {
		int posX = 0, posY = 0;
		int destinoX = 0, destinoY = 0;

		public Posicao() {

		}

		public Posicao(int x0, int sizeX, int y0, int sizeY) {
			this.posX = x0;
			this.destinoX = x0 + sizeX;
			this.posY = y0;
			this.destinoY = y0 + sizeY;
		}
	}

	public static class Zona {
		int x0, x1, y0, y1, risco;

		public Zona(int x0, int sizeX, int y0, int sizeY, int risco) {
			this.x0 = x0;
			this.x1 = x0 + sizeX;
			this.y0 = y0;
			this.y1 = y0 + sizeY;
			this.risco = risco;
		}
	}

	public static Color randomColor() {

		int r = (int) (0xff * Math.random());
		int g = (int) (0xff * Math.random());
		int b = (int) (0xff * Math.random());

		return new Color(r, g, b);
	}

}
