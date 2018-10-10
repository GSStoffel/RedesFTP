package br.com.unisinos.redes.ftp.tarefas;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import br.com.unisinos.redes.ftp.cliente.Cliente;

/**
 * Classe que desempenhará a função de capturar e distribuir os comando/tarefas
 * que serão executadas no Cliente ou Servidor.
 * 
 * @author 
 * Gabriel Sperb Stoffel
 * <br>
 * Victor E. Scherer
 *
 */
public class DistribuidorDeTarefas implements Runnable {

	private Cliente cliente;

	public DistribuidorDeTarefas(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public void run() {

		try (Scanner entradaCliente = new Scanner(cliente.getSocket().getInputStream());
				PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream());
				OutputStream clienteOS = cliente.getSocket().getOutputStream();) {

			while (entradaCliente.hasNextLine()) {
				String comando = entradaCliente.nextLine();
				System.out.println(cliente.getSocket() + " - " + comando);

				switch (comando.toLowerCase().split(" ")[0]) {

				case "put": // put C:\arquivo A.txt
					// TODO put
					break;

				case "get": // get Aquivo B.txt > C:\Minha Pasta\
					String[] parametros = comando.substring(4, comando.length()).split(" > ");
					File returnFile = new File(cliente.getPathAtual() + parametros[0]);

					if (!returnFile.exists()) {
						saidaCliente.println("Arquivo não encontrado:" + parametros[0]);
						break;
					}

					saidaCliente.println("file");
					saidaCliente.println(parametros[0]);
					saidaCliente.println(parametros[1]);

					byte[] fileByteArray = new byte[(int) returnFile.length()];
					FileInputStream fis = new FileInputStream(returnFile);
					BufferedInputStream bis = new BufferedInputStream(fis);
					bis.read(fileByteArray, 0, fileByteArray.length);

					saidaCliente.write(fileByteArray, 0, fileByteArray.length);
					clienteOS.flush();
					break;

				case "rename": // rename Arquivo A.txt > Arquivo B.txt
					new Tarefas().renomearArquivo(saidaCliente, comando, cliente.getPathAtual());
					break;

				case "delete": // delete Arquivo A.txt
					new Tarefas().deletar(saidaCliente, comando, cliente.getPathAtual());
					break;

				case "cd": // cd Minha Pasta
					new Tarefas().navegar(saidaCliente, comando, cliente);
					break;

				case "ls": // ls
					new Tarefas().listaArquivosDiretorioAtual(saidaCliente, cliente.getPathAtual());
					break;

				case "pwd": // pwd
					new Tarefas().exibirDiretorioAtual(saidaCliente, cliente.getPathAtual());
					break;

				case "mkdir": // mkdir Diretorio
					new Tarefas().cirarDiretorio(saidaCliente, comando, cliente.getPathAtual());
					break;

				default:
					saidaCliente.println("text");
					saidaCliente.println("Comando não reconhecido!");
					break;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
