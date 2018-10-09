package br.com.unisinos.redes.ftp.tarefas;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import br.com.unisinos.redes.ftp.cliente.Cliente;

/**
 * Classe que desempenhar� a fun��o de capturar e distribuir os comando/tarefas
 * que ser�o executadas no Cliente ou Servidor.
 * 
 * @author Gabriel Sperb Stoffel
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
				PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream())) {

			while (entradaCliente.hasNextLine()) {

				String comando = entradaCliente.nextLine();
				
				System.out.println(cliente.getSocket() + " - " + comando);

				switch (comando.toLowerCase().split(" ")[0]) {

				case "put": // put C:\arquivo A.txt
					// TODO put
					break;

				case "get": // get Aquivo B.txt > C:\Minha Pasta\
					// TODO get
					break;

				case "rename": // rename Arquivo A.txt > Arquivo B.txt
					String[] nomes = comando.substring(7, comando.length()).split(" > ");
					File oldFile = new File(cliente.getPathAtual() + nomes[0]);
					File newFile = new File(cliente.getPathAtual() + nomes[1]);
					if (!newFile.exists()) {
						if (oldFile.exists()) {
							oldFile.renameTo(newFile);
						} else {
							saidaCliente.println("text");
							saidaCliente.println("O arquivo " + nomes[0] + " n�o existe");
						}
					} else {
						saidaCliente.println("text");
						saidaCliente
								.println("O nome de arquivo " + nomes[1] + " j� existe, n�o foi poss�vel renomea-lo!");
					}
					break;

				case "delete": // delete Arquivo A.txt
					String path = removeUltimaBarra(cliente.getPathAtual());
					String nome = removePrimeiraBarra(comando.substring(7, comando.length()));
					boolean delete = new File(path + "/" + nome).delete();
					if (delete) {
						saidaCliente.println("text");
						saidaCliente.println("Arquivo Removido com sucesso");
					} else {
						saidaCliente.println("text");
						saidaCliente.println(
								"N�o foi poss�vel excluir o Arquivo/Diret�rio especificado. Verifiquei o nome e se o mesmo existe.");
					}
					break;

				case "cd": // cd Minha Pasta
					String pathNovo = comando.substring(3, comando.length());
					String pathAtual = removeUltimaBarra(cliente.getPathAtual());
					pathNovo = removePrimeiraBarra(pathNovo);
					if (pathNovo.equals("..") && !isRaiz(cliente)) {
						cliente.setPathAtual(pathAtual.substring(0, pathAtual.lastIndexOf("/")));
					} else if (new File(pathAtual + "/" + pathNovo).exists() && !pathNovo.equals("..")) {
						cliente.setPathAtual(pathAtual + "/" + pathNovo + "/");
					} else {
						saidaCliente.println("text");
						saidaCliente.println("Diret�rio inv�lido!");
					}
					break;

				case "ls": // ls
					File[] listFiles = new File(cliente.getPathAtual()).listFiles();
					for (int i = 0; i < listFiles.length; i++) {
						if (listFiles[i].isDirectory()) {
							saidaCliente.println("text");
							saidaCliente.println(listFiles[i].getName() + " - Diret�rio");
						} else {
							saidaCliente.println("text");
							saidaCliente.println(listFiles[i].getName() + " - Arquivo");
						}
					}
					break;

				case "pwd": // pwd
					saidaCliente.println("text");
					saidaCliente.println(cliente.getPathAtual());
					break;

				case "mkdir": // mkdir Diretorio
					File file = new File(cliente.getPathAtual() + comando.substring(6, comando.length()));
					if (!file.exists()) {
						file.mkdir();
					} else {
						saidaCliente.println("Diret�rio j� existe!");
					}
					break;

				default:
					saidaCliente.println("text");
					saidaCliente.println("Comando n�o reconhecido!");
					break;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * M�todo auxiliar para remover a primeira barra
	 * 
	 * @param pathNovo
	 *            - Novo Path para o qual o cliente ir�
	 * @return String - Novo Path sem a barra inicial
	 */
	private String removePrimeiraBarra(String pathNovo) {
		if (pathNovo.charAt(0) == '/') {
			return pathNovo.substring(1, pathNovo.length());
		}
		return pathNovo;
	}

	/**
	 * M�todo auxiliar para remover a �ltima barra do path
	 * 
	 * @param pathAtual
	 *            - Caimnho onde o cliente est� atualmente
	 * @return String - Path sem a �ltima barra
	 */
	private String removeUltimaBarra(String pathAtual) {
		if (pathAtual.charAt(pathAtual.length() - 1) == '/') {
			return pathAtual.substring(0, pathAtual.length() - 1);
		}
		return pathAtual;
	}

	/**
	 * M�todo auxiliar que verifica se o Path atual do cliente � a raiz ou n�o
	 * 
	 * @param cliente
	 *            - cliente contendo a informa��o do caminho que est� atualmente
	 * @return true - � a Raiz <br>
	 *         false - N�o � a Raiz
	 */
	private boolean isRaiz(Cliente cliente) {
		return (cliente.getPathAtual().equalsIgnoreCase("./FTP/") || cliente.getPathAtual().equalsIgnoreCase("./FTP"));
	}

}
