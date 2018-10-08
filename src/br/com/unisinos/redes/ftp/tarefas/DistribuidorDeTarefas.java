package br.com.unisinos.redes.ftp.tarefas;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe que desempenhará a função de capturar e distribuir os comando/tarefas
 * que serão executadas no Cliente ou Servidor.
 * 
 * @author Gabriel Sperb Stoffel
 *
 */
public class DistribuidorDeTarefas implements Runnable {

	private Socket socket;

	public DistribuidorDeTarefas(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		
		
		try (Scanner entradaCliente = new Scanner(socket.getInputStream())) {

			while (entradaCliente.hasNext()) {
				
				Tarefa tarefa = new Tarefa();
				
				String comando = entradaCliente.nextLine();
				
				switch (comando.toLowerCase().substring(0,comando.indexOf(" "))) {
				case "put":
					break;
				case "get":
					break;
				case "rename":
					break;
				case "delete":
					break;
				case "cd":
					break;
				case "ls":
					break;
				case "pwd":
					break;
				case "mkdir":
					break;
				default:
					System.out.println("Comando não existente!");
				}
				
				
				System.out.println(comando);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
