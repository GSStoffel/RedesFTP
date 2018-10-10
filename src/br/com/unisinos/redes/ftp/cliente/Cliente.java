package br.com.unisinos.redes.ftp.cliente;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Classe que desempenhará o papel de Cliente onde fará a conexão com o servidor
 * e executará/enviará os comandos respectivos ao seu papel
 * 
 * @author 
 * Gabriel Sperb Stoffel
 * <br>
 * Victor E. Scherer
 *
 */
public class Cliente {

	private String pathAtual;
	private Socket socket;

	public Cliente(Socket socket) {
		this.socket = socket;
		this.pathAtual = "./FTP/";
	}

	public String getPathAtual() {
		return pathAtual;
	}

	public void setPathAtual(String pathAtual) {
		this.pathAtual = pathAtual;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {

		Socket socketCliente = new Socket("localhost", 12345);

			System.out.println("Conexão estabelecida com sucesso!");

			Thread threadEnviaComandos = new Thread(new EnviadorDeComando(socketCliente));
			Thread threadRecebeResposta = new Thread(new ReceptorDeResposta(socketCliente));

			threadEnviaComandos.start();
			threadRecebeResposta.start();

			threadEnviaComandos.join();
	}
}