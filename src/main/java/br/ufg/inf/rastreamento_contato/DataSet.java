package br.ufg.inf.rastreamento_contato;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufg.inf.rastreamento_contato.model.*;

public class DataSet {

	public static final int linhas = 100, colunas = 50;
	public static final int qtdeAgentes = 20;
	public static final int tempos = 10;

	public static final double probabilidadeMover = 0.1f;

	// private static final int TIPOMOVIMENTO_ALEATORIO = 1;
	// private static final int TIPOMOVIMENTO_DIRECIONAL = 2;
	// private static final int tipoMovimento = TIPOMOVIMENTO_DIRECIONAL;

	private static Random random = new Random();

	public List<Individuo> individuos;
	public List<Objeto> objetos;

	public void init() {
		System.out.println("Criando DatSet");

		objetos = new ArrayList<>();
		objetos.add(new Objeto(4, 2, 10, 2, 1));
		objetos.add(new Objeto(4, 2, 14, 2, 1));
		objetos.add(new Objeto(4, 2, 18, 2, 1));
		objetos.add(new Objeto(4, 2, 22, 2, 1));

		objetos.add(new Objeto(80, 2, 20, 2, 2));
		objetos.add(new Objeto(80, 2, 24, 2, 2));
		objetos.add(new Objeto(80, 2, 28, 2, 2));
		objetos.add(new Objeto(80, 2, 32, 2, 2));
		objetos.add(new Objeto(80, 2, 36, 2, 2));
		objetos.add(new Objeto(80, 2, 40, 2, 2));

		objetos.add(new Objeto(30, 2, 20, 2, 2));
		objetos.add(new Objeto(34, 2, 20, 2, 2));
		objetos.add(new Objeto(38, 2, 20, 2, 2));
		objetos.add(new Objeto(42, 2, 20, 2, 2));
		objetos.add(new Objeto(46, 2, 20, 2, 2));
		objetos.add(new Objeto(50, 2, 20, 2, 2));

		objetos.add(new Objeto(10, 2, 30, 2, 2));
		objetos.add(new Objeto(14, 2, 30, 2, 2));
		objetos.add(new Objeto(10, 2, 34, 2, 2));
		objetos.add(new Objeto(14, 2, 34, 2, 2));
		objetos.add(new Objeto(10, 2, 38, 2, 2));
		objetos.add(new Objeto(14, 2, 38, 2, 2));

		objetos.add(new Objeto(70, 2, 10, 2, 3));
		objetos.add(new Objeto(74, 2, 10, 2, 3));
		objetos.add(new Objeto(70, 2, 14, 2, 3));
		objetos.add(new Objeto(74, 2, 14, 2, 3));
		objetos.add(new Objeto(70, 2, 18, 2, 3));
		objetos.add(new Objeto(74, 2, 18, 2, 3));

		objetos.add(new Objeto(50, 2, 30, 2, 3));
		objetos.add(new Objeto(50, 2, 34, 2, 3));
		objetos.add(new Objeto(54, 2, 30, 2, 3));
		objetos.add(new Objeto(54, 2, 34, 2, 3));
		objetos.add(new Objeto(58, 2, 30, 2, 3));
		objetos.add(new Objeto(58, 2, 34, 2, 3));
		objetos.add(new Objeto(62, 2, 30, 2, 3));
		objetos.add(new Objeto(62, 2, 34, 2, 3));

		individuos = new ArrayList<Individuo>();
		// criando os individuos
		char id = 'A';
		for (int i = 0; i < qtdeAgentes; i++) {
			individuos.add(new Individuo(id++, sorteiaPosicao()));
		}

		for (int t = 0; t < tempos; t++) {
			for (Individuo individuo : individuos) {
				Posicao atual = individuo.movimentos.get(individuo.movimentos.size() - 1);
				Posicao proximo = new Posicao();
				proximo.posX = atual.posX;
				proximo.posY = atual.posY;
				proximo.destinoX = atual.destinoX;
				proximo.destinoY = atual.destinoY;

				individuo.movimentos.add(proximo);

				// sorteia se vai relaizar ou não um movimento
				if (individuo.chegouDestino) {
					if (random.nextDouble() < probabilidadeMover) {
						// se tiver chegado ao destino sorteia um novo destino
						Posicao pos = sorteiaPosicao();
						proximo.destinoX = pos.posX;
						proximo.destinoY = pos.posY;
						individuo.chegouDestino = false;
					} else {
						// // ZERA O DESTINO
						// proximo.destinoX = 0;
						// proximo.destinoY = 0;
					}
				} else {
					// desloca no sentido do destino
					int distanciaX = atual.destinoX - atual.posX;
					int distanciaY = atual.destinoY - atual.posY;
					if (distanciaX == 0 && distanciaY == 0) {
						individuo.chegouDestino = true;
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

	public Posicao sorteiaPosicao() {
		Objeto objeto = sorteiaObjeto();
		return new Posicao(objeto.x0, objeto.y0);
		// Posicao posicao = new Posicao();
		// posicao.posX = random.nextInt(linhas);
		// posicao.posY = random.nextInt(colunas);
		// return posicao;
	}

	public Objeto sorteiaObjeto() {
		return objetos.get(random.nextInt(objetos.size()));
	}

	public static Color randomColor() {

		int r = (int) (0xff * Math.random());
		int g = (int) (0xff * Math.random());
		int b = (int) (0xff * Math.random());

		return new Color(r, g, b);
	}

}
