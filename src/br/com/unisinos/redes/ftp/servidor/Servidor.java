package br.com.unisinos.redes.ftp.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.unisinos.redes.ftp.cliente.Cliente;
import br.com.unisinos.redes.ftp.tarefas.DistribuidorDeTarefas;

/**
 * Classe que desempenhará o papel de Servidor onde abrirá a para os Clientes e
 * executará os comandos respectivos ao seu papel.
 * 
 * @author 
 * Gabriel Sperb Stoffel
 * <br>
 * Victor E. Scherer
 *
 */
public class Servidor {

	public static void main(String[] args) throws IOException {

		System.out.println("----- Iniciando o Servidor -----");
		
		try (ServerSocket server = new ServerSocket(12345)) {

			ExecutorService threadPool = Executors.newCachedThreadPool();

			while (true) {
				Cliente cliente = new Cliente(server.accept());
					System.out.println("Aceitando novo cliente na porta " + cliente.getSocket().getPort());
					
					DistribuidorDeTarefas distribuidorDeTarefas = new DistribuidorDeTarefas(cliente);
					threadPool.execute(distribuidorDeTarefas);
			}
		}
	}
}
