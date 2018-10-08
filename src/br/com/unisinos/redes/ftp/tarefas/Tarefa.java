package br.com.unisinos.redes.ftp.tarefas;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * 
 * Classe responsável pela tarefas (Excluir, Remover, Renomear...) que serão
 * executadas pelo Cliente e/ou Servidor.
 * 
 * @author Gabriel Sperb Stoffel
 *
 */
public class Tarefa {

	// Provisório, fiz para ir testando os métodos testar
	private static final String ROOT = "./FTP/";

	/**
	 * Cria arquivo no Path atual, caso o arquivo exista deve decidir (S/N) se
	 * deseja substituir o arquivo original.
	 * 
	 * @param nomeArquivo
	 *            - Nome do arquivo a ser criado (inclusive a extensão)
	 * @throws IOException
	 */
	public void criarArquivo(String nomeArquivo) throws IOException {
		File file = new File(ROOT + nomeArquivo);
		if (!file.exists()) {
			file.createNewFile();
		} else {
			System.out.println("O arquivo já existe, deseja substitui-lo? S/N");
			Scanner scanner = new Scanner(System.in);
			if (scanner.nextLine().toUpperCase().equals("S")) {
				file.createNewFile();
			} else {
				System.out.println("Operação cancelada!");
			}
		}
	}

	/**
	 * Cria Diretório no Path atual.
	 * 
	 * @param nomeDiretorio
	 *            - Nome do diretório a ser criado
	 * @throws IOException
	 */
	public void criarDiretorio(String nomeDiretorio) throws IOException {
		File file = new File(ROOT + nomeDiretorio);
		if (!file.exists()) {
			file.mkdir();
		} else {
			System.out.println("Diretório já existe!");
		}
	}

	/**
	 * Exclui o arquivo especificado no Path atual.
	 * 
	 * @param nome
	 *            - Nome do arquivo/diretório a ser excluido (inclusive a extensão
	 *            no caso de arquivo)
	 */
	public void remover(String nome) {
		boolean delete = new File(ROOT + nome).delete();
		if (delete) {
			System.out.println("Arquivo Removido com sucesso");
		} else {
			System.out.println(
					"Não foi possível excluir o Arquivo/Diretório especificado. Verifiquei o nome e se o mesmo existe.");
		}
	}

	/**
	 * Lista conteúdo do diretório atual informando o que é Diretório e o que é
	 * arquivo
	 * 
	 * Exemplo: Arquivo A.txt - Arquivo Diretório C - Diretório
	 */
	public void listarDiretorio() {
		File[] listFiles = new File(ROOT).listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			if (listFiles[i].isDirectory()) {
				System.out.println(listFiles[i].getName() + " - Diretório");
			} else {
				System.out.println(listFiles[i].getName() + " - Arquivo");
			}
		}
	}

	/**
	 * 
	 * Renomeio o arquivo/diretório do Path atual
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
				System.out.println("O arquivo " + nomeArquivoOld + " não existe");
			}
		} else {
			System.out.println("O nome de arquivo " + nomeArquivoNew + " já existe, não foi possível renomea-lo!");
		}
	}
}
