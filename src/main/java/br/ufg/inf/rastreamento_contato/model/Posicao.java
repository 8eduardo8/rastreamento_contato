package br.ufg.inf.rastreamento_contato.model;

public class Posicao {
	public int posX = 0, posY = 0;
	public int destinoX = 0, destinoY = 0;

	public Posicao() {

	}

	public Posicao(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	public Posicao(int x0, int sizeX, int y0, int sizeY) {
		this.posX = x0;
		this.destinoX = x0 + sizeX;
		this.posY = y0;
		this.destinoY = y0 + sizeY;
	}
}