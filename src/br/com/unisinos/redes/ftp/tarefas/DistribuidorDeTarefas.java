package br.com.unisinos.redes.ftp.tarefas;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import br.com.unisinos.redes.ftp.cliente.Cliente;

/**
 * Classe que desempenhará a função de capturar e distribuir os comando/tarefas
 * que serão executadas no Cliente ou Servidor.
 * 
 * @author Gabriel Sperb Stoffel <br>
 *         Victor E. Scherer
 *
 */
public class DistribuidorDeTarefas implements Runnable {

	private Cliente cliente;

	public DistribuidorDeTarefas(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public void run() {

		try /*
			 * (Scanner entradaCliente = new Scanner(cliente.getSocket().getInputStream());
			 * PrintStream saidaCliente = new
			 * PrintStream(cliente.getSocket().getOutputStream()); OutputStream clienteOS =
			 * cliente.getSocket().getOutputStream();)
			 */ {

			InputStream inputStream = cliente.getSocket().getInputStream();
			Scanner scanner = new Scanner(inputStream);

			while (scanner.hasNextLine()) {
				String comando = scanner.nextLine();
				System.out.println(cliente.getSocket() + " - " + comando);

				switch (comando.toLowerCase().split(" ")[0]) {

				case "put": // put C:\arquivo A.txt

					FileOutputStream fos = new FileOutputStream(new File("./FTP/documento.docx"));
					byte[] cbuffer = new byte[1024];
					int bytesRead;

					System.out.println("Recebendo arquivo...");
					while ((bytesRead = inputStream.read(cbuffer)) != -1) {
						fos.write(cbuffer, 0, bytesRead);
						fos.flush();
					}
					
					fos.close();
					new PrintStream(cliente.getSocket().getOutputStream()).println();

					break;

				case "get": // get Aquivo B.txt > C:\Minha Pasta\

					break;

				case "rename": // rename Arquivo A.txt > Arquivo B.txt
					new Tarefas().renomearArquivo(new PrintStream(cliente.getSocket().getOutputStream()), comando,
							cliente.getPathAtual());
					break;

				case "delete": // delete Arquivo A.txt
					new Tarefas().deletar(new PrintStream(cliente.getSocket().getOutputStream()), comando,
							cliente.getPathAtual());
					break;

				case "cd": // cd Minha Pasta
					new Tarefas().navegar(new PrintStream(cliente.getSocket().getOutputStream()), comando, cliente);
					break;

				case "ls": // ls
					new Tarefas().listaArquivosDiretorioAtual(new PrintStream(cliente.getSocket().getOutputStream()),
							cliente.getPathAtual());
					break;

				case "pwd": // pwd
					new Tarefas().exibirDiretorioAtual(new PrintStream(cliente.getSocket().getOutputStream()),
							cliente.getPathAtual());
					break;

				case "mkdir": // mkdir Diretorio
					new Tarefas().cirarDiretorio(new PrintStream(cliente.getSocket().getOutputStream()), comando,
							cliente.getPathAtual());
					break;

				default:
					new PrintStream(cliente.getSocket().getOutputStream()).println("text");
					new PrintStream(cliente.getSocket().getOutputStream()).println("Comando não reconhecido!");
					System.out.println("Comando não reconhecido!");
					break;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
