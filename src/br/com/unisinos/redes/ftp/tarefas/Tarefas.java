package br.com.unisinos.redes.ftp.tarefas;

import java.io.File;
import java.io.PrintStream;

import br.com.unisinos.redes.ftp.cliente.Cliente;

/**
 * 
 * Classe responsável pelas funcionalidades das tarefas que serão executadas
 * 
 * @author Gabriel Sperb Stoffel
 *
 *
 */
public class Tarefas {

	/**
	 * 
	 * Exive o diretório atual que o cliente está no servidor
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
	 * Cria o diretório, no path atual, especificado no comando, caso o mesmo não
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
			saidaCliente.println("Diretório já existe!");
		}
	}

	/**
	 * 
	 * Lista arquivos do diretório atual
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
				saidaCliente.println(listFiles[i].getName() + " - Diretório");
			} else {
				saidaCliente.println("text");
				saidaCliente.println(listFiles[i].getName() + " - Arquivo");
			}
		}
	}

	/**
	 * 
	 * Navega entra diretório dentro do servidor
	 * 
	 * @param saidaCliente
	 *            - PrintStream do cliente
	 * @param comando
	 *            - comando recebido pelo servidor
	 * @param cliente
	 *            - Objeto que carrega as informações do cliente no servidor
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
			saidaCliente.println("Diretório inválido!");
		}
	}

	/**
	 * 
	 * Remover arquivo/diretório do path atual (Não é possível remover diretório que
	 * contenham arquivos ou outros diretório dentro)
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
					"Não foi possível excluir o Arquivo/Diretório especificado. Verifiquei o nome e se o mesmo existe.");
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
				saidaCliente.println("O arquivo " + nomes[0] + " não existe");
			}
		} else {
			saidaCliente.println("text");
			saidaCliente.println("O nome de arquivo " + nomes[1] + " já existe, não foi possível renomea-lo!");
		}
	}

	/**
	 * Método auxiliar para remover a primeira barra
	 * 
	 * @param pathNovo
	 *            - Novo Path para o qual o cliente irá
	 * @return String - Novo Path sem a barra inicial
	 */
	private String removePrimeiraBarra(String pathNovo) {
		if (pathNovo.charAt(0) == '/') {
			return pathNovo.substring(1, pathNovo.length());
		}
		return pathNovo;
	}

	/**
	 * Método auxiliar para remover a última barra do path
	 * 
	 * @param pathAtual
	 *            - Caimnho onde o cliente está atualmente
	 * @return String - Path sem a última barra
	 */
	private String removeUltimaBarra(String pathAtual) {
		if (pathAtual.charAt(pathAtual.length() - 1) == '/') {
			return pathAtual.substring(0, pathAtual.length() - 1);
		}
		return pathAtual;
	}

	/**
	 * Método auxiliar que verifica se o Path atual do cliente é a raiz ou não
	 * 
	 * @param cliente
	 *            - cliente contendo a informação do caminho que está atualmente
	 * @return true - É a Raiz <br>
	 *         false - Não é a Raiz
	 */
	private boolean isRaiz(Cliente cliente) {
		return (cliente.getPathAtual().equalsIgnoreCase("./FTP/") || cliente.getPathAtual().equalsIgnoreCase("./FTP"));
	}

}
