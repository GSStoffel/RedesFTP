package br.com.unisinos.redes.ftp.cliente;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Classe que desempenhará o papel de Cliente onde fará a conexão com o servidor
 * e executará/enviará os comandos respectivos ao seu papel
 * 
 * @author Gabriel Sperb Stoffel
 *
 */
public class Cliente {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {

		try (Socket socket = new Socket("localhost", 12345)) {

			System.out.println("Conexão estabelecida com sucesso!");

			Thread threadEnviaComandos = new Thread(new EnviadorDeComando(socket));
			Thread threadRecebeResposta = new Thread(new ReceptorDeComandos(socket));

			threadEnviaComandos.start();
			threadRecebeResposta.start();

			threadEnviaComandos.join();

		}
	}
}