package br.com.unisinos.redes.ftp.cliente;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe responsável por receber dados e resposta de comandos do servidor
 * 
 * @author 
 * Gabriel Sperb Stoffel
 * <br>
 * Victor E. Scherer
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
				} else if(respostaServidor.nextLine().equals("file")){
					
					String fileName = respostaServidor.nextLine();
					String savePath = respostaServidor.nextLine();
					int bytesRead;
				    int current = 0;
					
					
					byte [] fileBytes  = new byte [999999999];
					
				    FileOutputStream fos = new FileOutputStream(savePath + fileName);
				    BufferedOutputStream bos = new BufferedOutputStream(fos);
				    bytesRead = socket.getInputStream().read(fileBytes,0,fileBytes.length);
				    current = bytesRead;
				    
				    while(bytesRead > -1) {
				    	bytesRead = socket.getInputStream().read(fileBytes,0,(fileBytes.length - current));
		    	        if(bytesRead >= 0) {
		    	    	   current += bytesRead;
		    	        }
				    }
				    bos.write(fileBytes, 0 , current);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
