package br.com.unisinos.redes.ftp.cliente;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe responsável por receber dados e resposta de comandos do servidor
 * 
 * @author Gabriel Sperb Stoffel <br>
 *         Victor E. Scherer
 *
 */
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
				} else if (respostaServidor.nextLine().equals("file")) {

					String caminho = respostaServidor.nextLine();
					
					FileOutputStream fos = new FileOutputStream(new File(caminho));
					byte[] cbuffer = new byte[1024];
					int bytesRead;

					System.out.println("Recebendo arquivo...");
					while ((bytesRead = socket.getInputStream().read(cbuffer)) != -1) {
						fos.write(cbuffer, 0, bytesRead);
						fos.flush();
					}
					
					fos.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	

}
