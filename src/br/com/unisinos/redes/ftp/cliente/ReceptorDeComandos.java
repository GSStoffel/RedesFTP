package br.com.unisinos.redes.ftp.cliente;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ReceptorDeComandos implements Runnable {

	private Socket socket;

	public ReceptorDeComandos(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (Scanner respostaServidor = new Scanner(socket.getInputStream())) {
			while (respostaServidor.hasNextLine()) {
				String line = respostaServidor.nextLine();
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
