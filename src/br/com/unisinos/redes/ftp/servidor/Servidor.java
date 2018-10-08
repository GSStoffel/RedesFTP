package br.com.unisinos.redes.ftp.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe que desempenhar� o papel de Servidor onde abrir� a para os Clientes e executar� os comandos respectivos ao seu papel.
 * 
 * @author Gabriel Sperb Stoffel
 *
 */
public class Servidor {

	public static void main(String[] args) throws IOException {

		System.out.println("----- Iniciando o Servidor -----");
		try (ServerSocket server = new ServerSocket(12345)) {

			while (true) {
				try (Socket socket = server.accept()) {
					System.out.println("Aceitando novo cliente na porta" + socket.getPort());
				}
			}
		}
	}
}
