package br.com.unisinos.redes.ftp.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe respons�vel pelo envio de comandos para o servidor
 * 
 * @author 
 * Gabriel Sperb Stoffel
 * <br>
 * Victor E. Scherer
 *
 */
public class EnviadorDeComando implements Runnable {

	private Socket socket;

	public EnviadorDeComando(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		// Enviando dados para o servidor
		try (PrintStream saida = new PrintStream(socket.getOutputStream()); Scanner scanner = new Scanner(System.in);) {

			while (scanner.hasNextLine()) {
				String comando = scanner.nextLine();
				
				if (comando.trim().toLowerCase().equals("help")) {
					exibirComandos();
				} else if (comando.trim().toLowerCase().equals("bye")) {
					System.out.println("At� logo!");
					break;
				} else {
					saida.println(comando);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void exibirComandos() {
		System.out.println(
			"Comandos dispon�veis:\n"
			+ "put [/arquivo a enviar.txt] : Enviar arquivo\n"
			+ "get [/arquivo a receber.txt] [/path receptor] : Receber arquivo\n" 
			+ "rename [Nome antigo.txt] [Novo Nome] : Renomear arquivo\n"
			+ "delete [Arquivo.txt] : Remover arquivo ou diret�rio\n" 
			+ "cd [/caminho] : Mover-se por diret�rios\n"
			+ "ls : Exibir arquivos do Path atual\n" 
			+ "pwd : Exibir diret�rio atual\n"
			+ "mkdir [Diret�rio] : Criar diret�rio\n" 
			+ "bye: Sair do FTP"
		);
	}
}
