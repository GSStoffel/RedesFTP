package br.com.unisinos.redes.ftp.tarefas;

import java.io.IOException;
import java.util.Scanner;

import br.com.unisinos.redes.ftp.cliente.Cliente;

/**
 * Classe que desempenhará a função de capturar e distribuir os comando/tarefas
 * que serão executadas no Cliente ou Servidor.
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

		try (Scanner entradaCliente = new Scanner(cliente.getSocket().getInputStream())) {
			
			while (entradaCliente.hasNextLine()) {
				
				Tarefa tarefa = new Tarefa();
				
				String comando = entradaCliente.nextLine();
				
				System.out.println(comando);
				
				switch (comando.toLowerCase().split(" ")[0]) {
				case "put":		// put C:\arquivo A.txt
					break;
				case "get":		// get Aquivo B.txt > C:\Minha Pasta\
					break;
				case "rename": 	// rename Arquivo A.txt > Arquivo B.txt
					String[] nomes = comando.substring(7, comando.length()).split(" > ");
					tarefa.renomear(cliente, nomes[0], nomes[1]);
					break;
				case "delete": 	// delete Arquivo A.txt
					tarefa.remover(cliente, comando.substring(6, comando.length()));
					break;
				case "cd": 		// cd Minha Pasta
					tarefa.caminhar(cliente, comando.substring(2, comando.length()));
					break;
				case "ls": 		// ls 
					tarefa.listarDiretorio(cliente);
					break;
				case "pwd": 	// pwd
					tarefa.exibirDiretorio(cliente);
					break;
				case "mkdir":	// mkdir Diretorio
					tarefa.criarDiretorio(cliente, comando.substring(6, comando.length()));
					break;
				default:
					tarefa.comunicarErro(cliente);
					break;
				}
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
