package br.com.unisinos.redes.ftp.cliente;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ReceptorDeResposta implements Runnable {

	private Socket socket;

	public ReceptorDeResposta(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (Scanner respostaServidor = new Scanner(socket.getInputStream())) {
			while (respostaServidor.hasNextLine()) {
				if (respostaServidor.nextLine().equals("text")) {
					System.out.println(respostaServidor.nextLine());
				} else if(respostaServidor.nextLine().equals("file")){
					//receber arquivo
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
