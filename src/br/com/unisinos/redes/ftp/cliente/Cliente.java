package br.com.unisinos.redes.ftp.cliente;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import br.com.unisinos.redes.ftp.Ftp;


/**
 * Classe que desempenhará o papel de Cliente onde fará a conexão com o servidor e executará/enviará os comandos respectivos ao seu papel
 * 
 * @author Gabriel Sperb Stoffel
 *
 */
public class Cliente {

	private Ftp ftp;
	
	public Cliente(Ftp ftp) {
		this.ftp = ftp;
	}

	public static void main(String[] args) throws UnknownHostException, IOException {

		try (Socket socket = new Socket("localhost", 12345)) {
			
		}

	}

}
