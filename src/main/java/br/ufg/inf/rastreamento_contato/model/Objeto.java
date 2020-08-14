package br.ufg.inf.rastreamento_contato.model;

public class Objeto {
	public int x0, x1, y0, y1;

	public int risco;
	public int tempoDescontaminacao;
	// public int tipoSuperficie;

	public int infectadoPrimario = 0;
	public int infectadoSecundario = 0;

	public Objeto(int x0, int sizeX, int y0, int sizeY, int risco) {
		this.x0 = x0;
		this.x1 = x0 + sizeX;
		this.y0 = y0;
		this.y1 = y0 + sizeY;
		this.risco = risco;
	}
}
