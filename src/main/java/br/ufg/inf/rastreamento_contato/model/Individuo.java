package br.ufg.inf.rastreamento_contato.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Individuo {

	public char id;
	public Color color;
	public List<Posicao> movimentos;
	public boolean infectado;

	public int qtdContatoPrimario;
	public int qtdContatoSecundario;
	public int qtdContatoObjetoPrimario;
	public int qtdContatoObjetoSecundario;

	public boolean chegouDestino;

	public Individuo(char id, Posicao posicaoInicial) {
		this.id = id;
		movimentos = new ArrayList();

		posicaoInicial.destinoX = posicaoInicial.posX;
		posicaoInicial.destinoY = posicaoInicial.posY;

		movimentos.add(posicaoInicial);
		chegouDestino = true;

	}
}
