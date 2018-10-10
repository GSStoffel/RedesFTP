package br.com.unisinos.redes.ftp.cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe responsável pelo envio de comandos para o servidor
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
					System.out.println("Até logo!");
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
			"Comandos disponíveis:\n"
			+ "put [/arquivo a enviar.txt] : Enviar arquivo\n"
			+ "get [/arquivo a receber.txt] [/path receptor] : Receber arquivo\n" 
			+ "rename [Nome antigo.txt] [Novo Nome] : Renomear arquivo\n"
			+ "delete [Arquivo.txt] : Remover arquivo ou diretório\n" 
			+ "cd [/caminho] : Mover-se por diretórios\n"
			+ "ls : Exibir arquivos do Path atual\n" 
			+ "pwd : Exibir diretório atual\n"
			+ "mkdir [Diretório] : Criar diretório\n" 
			+ "bye: Sair do FTP"
		);
	}
}
