package br.com.unisinos.redes.ftp.tarefas;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import br.com.unisinos.redes.ftp.cliente.Cliente;

/**
 * 
 * Classe responsável pela tarefas (Excluir, Remover, Renomear...) que serão
 * executadas pelo Cliente e/ou Servidor.
 * 
 * @author Gabriel Sperb Stoffel
 *
 */
public class Tarefa {

	/**
	 * Cria arquivo no Path atual, caso o arquivo exista deve decidir (S/N) se
	 * deseja substituir o arquivo original.
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informações do mesmo
	 * @param nomeArquivo
	 *            - Nome do arquivo a ser criado (inclusive a extensão)
	 * @throws IOException
	 */

	public void criarArquivo(Cliente cliente, String nomeArquivo) throws IOException {

		try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream());) {
			File file = new File(cliente.getPathAtual() + nomeArquivo);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				saidaCliente.println("O arquivo já existe, operação cancelada!");
			}
		}
		// else {
		// System.out.println("O arquivo já existe, deseja substitui-lo? S/N");
		// Scanner scanner = new Scanner(System.in);
		// if (scanner.nextLine().toUpperCase().equals("S")) {
		// file.createNewFile();
		// } else {
		// System.out.println("Operação cancelada!");
		// }
		// }
	}

	/**
	 * Cria Diretório no Path atual.
	 * @param os 
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informações do mesmo
	 * @param nomeDiretorio
	 *            - Nome do diretório a ser criado
	 * @throws IOException
	 */
	public void criarDiretorio(Cliente cliente, String nomeDiretorio) throws IOException {

		try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream())) {

			File file = new File(cliente.getPathAtual() + nomeDiretorio);
			if (!file.exists()) {
				file.mkdir();
			} else {
				saidaCliente.println("Diretório já existe!");
			}
		}
	}

	/**
	 * Exclui o arquivo especificado no Path atual.
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informações do mesmo
	 * @param nomeArquivo
	 *            - Nome do arquivo a ser demovido (incluvise coma extenção)
	 * @throws IOException
	 */
	public void remover(Cliente cliente, String nomeArquivo) throws IOException {
		try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream());) {
			boolean delete = new File(cliente.getPathAtual() + nomeArquivo).delete();
			if (delete) {
				saidaCliente.println("Arquivo Removido com sucesso");
			} else {
				saidaCliente.println(
						"Não foi possível excluir o Arquivo/Diretório especificado. Verifiquei o nome e se o mesmo existe.");
			}
		}
	}

	/**
	 * Lista conteúdo do diretório atual informando o que é Diretório e o que é
	 * arquivo <br>
	 * <br>
	 * Exemplo: <br>
	 * Arquivo A.txt - Arquivo <br>
	 * Diretório C - Diretório <br>
	 * ...
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informações do mesmo
	 * @throws IOException
	 * 
	 */
	public void listarDiretorio(Cliente cliente) throws IOException {
		try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream());) {
			File[] listFiles = new File(cliente.getPathAtual()).listFiles();
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
	}

	/**
	 * 
	 * Renomeio o arquivo/diretório do Path atual
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informações do mesmo
	 * @param nomeArquivoOld
	 *            - Nome atual do arquivo
	 * @param nomeArquivoNew
	 *            - Novo nome para o arquivo
	 * @throws IOException
	 */
	public void renomear(Cliente cliente, String nomeArquivoOld, String nomeArquivoNew) throws IOException {
		try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream())) {

			File oldFile = new File(cliente.getPathAtual() + nomeArquivoOld);
			File newFile = new File(cliente.getPathAtual() + nomeArquivoNew);

			if (!newFile.exists()) {
				if (oldFile.exists()) {
					oldFile.renameTo(newFile);
				} else {
					saidaCliente.println("text");
					saidaCliente.println("O arquivo " + nomeArquivoOld + " não existe");
				}
			} else {

				saidaCliente.println("text");
				saidaCliente
						.println("O nome de arquivo " + nomeArquivoNew + " já existe, não foi possível renomea-lo!");
			}

		}
	}

	/**
	 * Caminhar um diretório para frente ou para trás
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informações do mesmo
	 * @param pathAtual
	 *            - Path em que o cliente está atualmente (PWD)
	 * @param pathNovo
	 *            - Path para qual o usuário irá se mover <br>
	 *            ".." - retorna um diretório <br>
	 *            "/[diretorio]" avança para um diretorio
	 * @throws IOException
	 */
	public void caminhar(Cliente cliente, String pathNovo) throws IOException {

		String pathAtual = removeUltimaBarra(cliente.getPathAtual());
		pathNovo = removePrimeiraBarra(pathNovo);

		// Caso tenha que retonar o diretório o mesmo não pode estar já na raiz
		if (pathNovo.equals("..") && !isRaiz(cliente)) {
			cliente.setPathAtual(pathAtual.substring(pathAtual.lastIndexOf("/")));

			// O novo path existe?
		} else if (new File(pathAtual + "/" + pathNovo).exists()) {
			cliente.setPathAtual(pathAtual + "/" + pathNovo + "/");

			// O diretório não é válido
		} else {
			try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream())) {
				saidaCliente.println("text");
				saidaCliente.println("Diretório inválido!");
			}
		}
	}

	/**
	 * Exibe onde o cliente está atualmente dentro do FTP
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informações do mesmo
	 * @throws IOException
	 */
	public void exibirDiretorio(Cliente cliente) throws IOException {
		try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream())) {
			saidaCliente.println("text");
			saidaCliente.println(cliente.getPathAtual());
		}
	}

	/**
	 * Comunica cliente caso ocorra erro
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informações do mesmo
	 * @throws IOException
	 */
	public void comunicarErro(Cliente cliente) throws IOException {
		try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream())) {
			saidaCliente.println("text");
			saidaCliente.println("Comando não reconhecido!");
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
