package br.com.unisinos.redes.ftp.tarefas;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * 
 * Classe respons�vel pela tarefas (Excluir, Remover, Renomear...) que ser�o
 * executadas pelo Cliente e/ou Servidor.
 * 
 * @author Gabriel Sperb Stoffel
 *
 */
public class Tarefa {

	// Provis�rio, fiz para ir testando os m�todos testar
	private static final String ROOT = "./FTP/";

	/**
	 * Cria arquivo no Path atual, caso o arquivo exista deve decidir (S/N) se
	 * deseja substituir o arquivo original.
	 * 
	 * @param nomeArquivo
	 *            - Nome do arquivo a ser criado (inclusive a extens�o)
	 * @throws IOException
	 */
	public void criarArquivo(String nomeArquivo) throws IOException {
		File file = new File(ROOT + nomeArquivo);
		if (!file.exists()) {
			file.createNewFile();
		} else {
			System.out.println("O arquivo j� existe, deseja substitui-lo? S/N");
			Scanner scanner = new Scanner(System.in);
			if (scanner.nextLine().toUpperCase().equals("S")) {
				file.createNewFile();
			} else {
				System.out.println("Opera��o cancelada!");
			}
		}
	}

	/**
	 * Cria Diret�rio no Path atual.
	 * 
	 * @param nomeDiretorio
	 *            - Nome do diret�rio a ser criado
	 * @throws IOException
	 */
	public void criarDiretorio(String nomeDiretorio) throws IOException {
		File file = new File(ROOT + nomeDiretorio);
		if (!file.exists()) {
			file.mkdir();
		} else {
			System.out.println("Diret�rio j� existe!");
		}
	}

	/**
	 * Exclui o arquivo especificado no Path atual.
	 * 
	 * @param nome
	 *            - Nome do arquivo/diret�rio a ser excluido (inclusive a extens�o
	 *            no caso de arquivo)
	 */
	public void remover(String nome) {
		boolean delete = new File(ROOT + nome).delete();
		if (delete) {
			System.out.println("Arquivo Removido com sucesso");
		} else {
			System.out.println(
					"N�o foi poss�vel excluir o Arquivo/Diret�rio especificado. Verifiquei o nome e se o mesmo existe.");
		}
	}

	/**
	 * Lista conte�do do diret�rio atual informando o que � Diret�rio e o que �
	 * arquivo
	 * 
	 * Exemplo: Arquivo A.txt - Arquivo Diret�rio C - Diret�rio
	 */
	public void listarDiretorio() {
		File[] listFiles = new File(ROOT).listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			if (listFiles[i].isDirectory()) {
				System.out.println(listFiles[i].getName() + " - Diret�rio");
			} else {
				System.out.println(listFiles[i].getName() + " - Arquivo");
			}
		}
	}

	/**
	 * 
	 * Renomeio o arquivo/diret�rio do Path atual
	 * 
	 * @param nomeArquivoOld
	 *            - Nome atual do arquivo
	 * @param nomeArquivoNew
	 *            - Novo nome para o arquivo
	 */
	public void renomear(String nomeArquivoOld, String nomeArquivoNew) {

		File oldFile = new File(ROOT + nomeArquivoOld);
		File newFile = new File(ROOT + nomeArquivoNew);

		if (!newFile.exists()) {

			if (oldFile.exists()) {
				oldFile.renameTo(newFile);
			} else {
				System.out.println("O arquivo " + nomeArquivoOld + " n�o existe");
			}
		} else {
			System.out.println("O nome de arquivo " + nomeArquivoNew + " j� existe, n�o foi poss�vel renomea-lo!");
		}
	}
}
