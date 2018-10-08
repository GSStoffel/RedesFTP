package br.com.unisinos.redes.ftp.tarefas;

import java.io.IOException;

/**
 * Classe para testar as tarefas separadamente enquanto a aplicação não está completa, posteriormente será removida
 *  
 * @author Gabriel Sperb Stoffel
 *
 */
public class TesteTarefas {

	public static void main(String[] args) throws IOException {
		Tarefa tarefa = new Tarefa();
		tarefa.remover("gabriel.txt");
		tarefa.criarArquivo("paulo.txt");
		tarefa.criarArquivo("sonia.txt");
		tarefa.listarDiretorio();
		tarefa.renomear("paulo.txt", "vitor.txt");
		tarefa.listarDiretorio();
	}
}
