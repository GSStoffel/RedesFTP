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
		tarefa.criarArquivo("teste1.txt");
		tarefa.criarArquivo("teste2.txt");
		tarefa.criarArquivo("teste3.txt");
		tarefa.listarDiretorio();
		tarefa.renomear("teste1.txt", "teste4.txt");
		tarefa.listarDiretorio();
		tarefa.remover("teste3.txt");
	}
}
