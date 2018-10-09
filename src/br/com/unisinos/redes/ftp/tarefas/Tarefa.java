package br.com.unisinos.redes.ftp.tarefas;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import br.com.unisinos.redes.ftp.cliente.Cliente;

/**
 * 
 * Classe respons�vel pela tarefas (Excluir, Remover, Renomear...) que ser�o
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
	 *            - Objeto cliente com as informa��es do mesmo
	 * @param nomeArquivo
	 *            - Nome do arquivo a ser criado (inclusive a extens�o)
	 * @throws IOException
	 */

	public void criarArquivo(Cliente cliente, String nomeArquivo) throws IOException {

		try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream());) {
			File file = new File(cliente.getPathAtual() + nomeArquivo);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				saidaCliente.println("O arquivo j� existe, opera��o cancelada!");
			}
		}
		// else {
		// System.out.println("O arquivo j� existe, deseja substitui-lo? S/N");
		// Scanner scanner = new Scanner(System.in);
		// if (scanner.nextLine().toUpperCase().equals("S")) {
		// file.createNewFile();
		// } else {
		// System.out.println("Opera��o cancelada!");
		// }
		// }
	}

	/**
	 * Cria Diret�rio no Path atual.
	 * @param os 
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informa��es do mesmo
	 * @param nomeDiretorio
	 *            - Nome do diret�rio a ser criado
	 * @throws IOException
	 */
	public void criarDiretorio(Cliente cliente, String nomeDiretorio) throws IOException {

		try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream())) {

			File file = new File(cliente.getPathAtual() + nomeDiretorio);
			if (!file.exists()) {
				file.mkdir();
			} else {
				saidaCliente.println("Diret�rio j� existe!");
			}
		}
	}

	/**
	 * Exclui o arquivo especificado no Path atual.
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informa��es do mesmo
	 * @param nomeArquivo
	 *            - Nome do arquivo a ser demovido (incluvise coma exten��o)
	 * @throws IOException
	 */
	public void remover(Cliente cliente, String nomeArquivo) throws IOException {
		try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream());) {
			boolean delete = new File(cliente.getPathAtual() + nomeArquivo).delete();
			if (delete) {
				saidaCliente.println("Arquivo Removido com sucesso");
			} else {
				saidaCliente.println(
						"N�o foi poss�vel excluir o Arquivo/Diret�rio especificado. Verifiquei o nome e se o mesmo existe.");
			}
		}
	}

	/**
	 * Lista conte�do do diret�rio atual informando o que � Diret�rio e o que �
	 * arquivo <br>
	 * <br>
	 * Exemplo: <br>
	 * Arquivo A.txt - Arquivo <br>
	 * Diret�rio C - Diret�rio <br>
	 * ...
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informa��es do mesmo
	 * @throws IOException
	 * 
	 */
	public void listarDiretorio(Cliente cliente) throws IOException {
		try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream());) {
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
		}
	}

	/**
	 * 
	 * Renomeio o arquivo/diret�rio do Path atual
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informa��es do mesmo
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
					saidaCliente.println("O arquivo " + nomeArquivoOld + " n�o existe");
				}
			} else {

				saidaCliente.println("text");
				saidaCliente
						.println("O nome de arquivo " + nomeArquivoNew + " j� existe, n�o foi poss�vel renomea-lo!");
			}

		}
	}

	/**
	 * Caminhar um diret�rio para frente ou para tr�s
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informa��es do mesmo
	 * @param pathAtual
	 *            - Path em que o cliente est� atualmente (PWD)
	 * @param pathNovo
	 *            - Path para qual o usu�rio ir� se mover <br>
	 *            ".." - retorna um diret�rio <br>
	 *            "/[diretorio]" avan�a para um diretorio
	 * @throws IOException
	 */
	public void caminhar(Cliente cliente, String pathNovo) throws IOException {

		String pathAtual = removeUltimaBarra(cliente.getPathAtual());
		pathNovo = removePrimeiraBarra(pathNovo);

		// Caso tenha que retonar o diret�rio o mesmo n�o pode estar j� na raiz
		if (pathNovo.equals("..") && !isRaiz(cliente)) {
			cliente.setPathAtual(pathAtual.substring(pathAtual.lastIndexOf("/")));

			// O novo path existe?
		} else if (new File(pathAtual + "/" + pathNovo).exists()) {
			cliente.setPathAtual(pathAtual + "/" + pathNovo + "/");

			// O diret�rio n�o � v�lido
		} else {
			try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream())) {
				saidaCliente.println("text");
				saidaCliente.println("Diret�rio inv�lido!");
			}
		}
	}

	/**
	 * Exibe onde o cliente est� atualmente dentro do FTP
	 * 
	 * @param cliente
	 *            - Objeto cliente com as informa��es do mesmo
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
	 *            - Objeto cliente com as informa��es do mesmo
	 * @throws IOException
	 */
	public void comunicarErro(Cliente cliente) throws IOException {
		try (PrintStream saidaCliente = new PrintStream(cliente.getSocket().getOutputStream())) {
			saidaCliente.println("text");
			saidaCliente.println("Comando n�o reconhecido!");
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
