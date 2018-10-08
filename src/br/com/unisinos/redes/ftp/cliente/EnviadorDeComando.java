package br.com.unisinos.redes.ftp.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class EnviadorDeComando implements Runnable {

	private Socket socket;

	public EnviadorDeComando(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		// Enviando dados para o servidor
		try (PrintStream saida = new PrintStream(socket.getOutputStream())) {

			try (Scanner scanner = new Scanner(System.in)) {
				while (scanner.hasNextLine()) {
					String comando = scanner.nextLine();

					if (comando.trim().toLowerCase().equals("bye")) {
						System.out.println("Até logo!");
						break;
					}

					saida.println(comando);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
