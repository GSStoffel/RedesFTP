package br.com.unisinos.redes.ftp;

import java.io.File;

public class Ftp {

	private File caminho;

	public Ftp(String pathName) {
		this.caminho = new File(pathName);
	}

	public File getCaminho() {
		return caminho;
	}
	
}
