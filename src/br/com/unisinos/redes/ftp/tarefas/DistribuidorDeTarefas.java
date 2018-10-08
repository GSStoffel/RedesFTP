package br.com.unisinos.redes.ftp.tarefas;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe que desempenhar� a fun��o de capturar e distribuir os comando/tarefas
 * que ser�o executadas no Cliente ou Servidor.
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

		try (Scanner entradaCliente = new Scanner(socket.getInputStream()); OutputStream saidaCliente = socket.getOutputStream();) {
			
			while (entradaCliente.hasNext()) {
				
				Tarefa tarefa = new Tarefa();
				
				String comando = entradaCliente.nextLine();
				
				switch (comando.toLowerCase()) {
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
					tarefa.listarDiretorio();
					break;
				case "pwd":
					break;
				case "mkdir":
					
					break;
				default:
					System.out.println("Comando n�o reconhecido!");
				}
				
				
				System.out.println(comando);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
