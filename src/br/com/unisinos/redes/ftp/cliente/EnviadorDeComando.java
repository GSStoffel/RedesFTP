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

					if (comando.trim().toLowerCase().equals("help")) {
						System.out.println(
								"Comandos dispon�veis:\n"
								+ "put: Enviar arquivo\n"
								+ "get: Receber arquivo\n"
								+ "rename: Renomear arquivo\n"
								+ "delete: Remover arquivo ou diret�rio\n"
								+ "cd: Mover-se por diret�rios\n"
								+ "ls: Exibir arquivos do Path atual\n"
								+ "pwd: Exibir diret�rio atual\n"
								+ "mkdir: Criar diret�rio\n");
						
						/*case "rename":
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
				default:*/
					}
					
					if (comando.trim().toLowerCase().equals("bye")) {
						System.out.println("At� logo!");
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
