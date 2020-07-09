package br.ufg.inf.rastreamento_contato;

import java.util.ArrayList;
import java.util.List;

public class DataSet {

	private static final int linhas = 50, colunas = 50;
	private static final int qtdeAgentes = 10;
	private static final double probabilidadeMover = 0.2;
	private static final int tempos = 100;

	private static final int TIPOMOVIMENTO_ALEATORIO = 1;
	private static final int TIPOMOVIMENTO_DIRECIONAL = 2;

	private static final int tipoMovimento = TIPOMOVIMENTO_DIRECIONAL;

	public static void main(String[] args) {
		System.out.println("Criando DatSet");

		List<Agente> agentes = new ArrayList();

		for (int i = 0; i < qtdeAgentes; i++) {
			agentes.add(new Agente());
		}

	}

	public static class Agente {
		int id;
		public List<Movimento> movimentos;

		public Agente() {
		}
	}

	public static class Movimento {
		int tempo;
		int posX = 0, posY = 0;
	}
}
