package br.com.unisinos.redes.ftp.tarefas;

import java.io.File;
import java.io.PrintStream;

import br.com.unisinos.redes.ftp.cliente.Cliente;

/**
 * 
 * Classe respons�vel pelas funcionalidades das tarefas que ser�o executadas
 * 
 * @author Gabriel Sperb Stoffel
 *
 *
 */
public class Tarefas {

	/**
	 * 
	 * Exive o diret�rio atual que o cliente est� no servidor
	 * 
	 * @param saidaCliente
	 *            - PrintStream do cliente
	 * @param pathAtual
	 *            - Path atual que o cliente se encontra
	 */
	public void exibirDiretorioAtual(PrintStream saidaCliente, String pathAtual) {
		saidaCliente.println("text");
		saidaCliente.println(pathAtual);
	}

	/**
	 * 
	 * Cria o diret�rio, no path atual, especificado no comando, caso o mesmo n�o
	 * exista.
	 * 
	 * @param saidaCliente
	 *            - PrintStream do cliente
	 * @param comando
	 *            - comando recebido pelo servidor
	 * @param pathAtual
	 *            - Path atual que o cliente se encontra
	 */
	public void cirarDiretorio(PrintStream saidaCliente, String comando, String pathAtual) {
		File file = new File(pathAtual + comando.substring(6, comando.length()));
		if (!file.exists()) {
			file.mkdir();
		} else {
			saidaCliente.println("Diret�rio j� existe!");
		}
	}

	/**
	 * 
	 * Lista arquivos do diret�rio atual
	 * 
	 * @param saidaCliente
	 *            - PrintStream do cliente
	 * @param pathAtual
	 *            - Path atual que o cliente se encontra
	 */
	public void listaArquivosDiretorioAtual(PrintStream saidaCliente, String pathAtual) {
		File[] listFiles = new File(pathAtual).listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			if (listFiles[i].isDirectory()) {
				saidaCliente.println("text");
				saidaCliente.println(listFiles[i].getName() + " - Diret�rio");
			} else {
				saidaCliente.println("text");
				saidaCliente.println(listFiles[i].getName() + " - Arquivo");
			}
		}
	}

	/**
	 * 
	 * Navega entra diret�rio dentro do servidor
	 * 
	 * @param saidaCliente
	 *            - PrintStream do cliente
	 * @param comando
	 *            - comando recebido pelo servidor
	 * @param cliente
	 *            - Objeto que carrega as informa��es do cliente no servidor
	 */
	public void navegar(PrintStream saidaCliente, String comando, Cliente cliente) {
		String pathNovo = comando.substring(3, comando.length());
		String path = removeUltimaBarra(cliente.getPathAtual());
		pathNovo = removePrimeiraBarra(pathNovo);
		if (pathNovo.equals("..") && !isRaiz(cliente)) {
			cliente.setPathAtual(path.substring(0, path.lastIndexOf("/")));
		} else if (new File(path + "/" + pathNovo).exists() && !pathNovo.equals("..")) {
			cliente.setPathAtual(path + "/" + pathNovo + "/");
		} else {
			saidaCliente.println("text");
			saidaCliente.println("Diret�rio inv�lido!");
		}
	}

	/**
	 * 
	 * Remover arquivo/diret�rio do path atual (N�o � poss�vel remover diret�rio que
	 * contenham arquivos ou outros diret�rio dentro)
	 * 
	 * @param saidaCliente
	 *            - PrintStream do cliente
	 * @param comando
	 *            - comando recebido pelo servidor
	 * @param pathAtual
	 *            - Path atual que o cliente se encontra
	 */
	public void deletar(PrintStream saidaCliente, String comando, String pathAtual) {
		String path = removeUltimaBarra(pathAtual);
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
	}

	/**
	 * Renomeia arquivo especificado no comando no path atual
	 * 
	 * @param saidaCliente
	 *            - PrintStream do cliente
	 * @param comando
	 *            - comando recebido pelo servidor
	 * @param pathAtual
	 *            - Path atual que o cliente se encontra
	 */
	public void renomearArquivo(PrintStream saidaCliente, String comando, String pathAtual) {
		String[] nomes = comando.substring(7, comando.length()).split(" > ");
		File oldFile = new File(pathAtual + nomes[0]);
		File newFile = new File(pathAtual + nomes[1]);
		if (!newFile.exists()) {
			if (oldFile.exists()) {
				oldFile.renameTo(newFile);
			} else {
				saidaCliente.println("text");
				saidaCliente.println("O arquivo " + nomes[0] + " n�o existe");
			}
		} else {
			saidaCliente.println("text");
			saidaCliente.println("O nome de arquivo " + nomes[1] + " j� existe, n�o foi poss�vel renomea-lo!");
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
