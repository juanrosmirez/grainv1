package ar.edu.unlam.criptografía.grain;

public class Cipher {
	
	private byte[] textoPlano;
	private byte[] keystream;
	private int longitud;
	private Generator keystreamGenerator;

	public Cipher(byte[] clave, byte[] semilla, byte[] textoPlano) throws Exception {
		keystreamGenerator = new Generator(toShortArray(clave), toShortArray(semilla));

		this.textoPlano = textoPlano;
		this.keystream = toByteArray(keystreamGenerator.generarKeystream(this.textoPlano.length - 54));

		if ((this.longitud = this.textoPlano.length) - 54 != this.keystream.length) {
			throw new Exception();
		}
	}

	public byte[] xor() {
		byte[] xored = new byte[this.longitud];
		byte[] header = getHeader(this.textoPlano);
		byte[] body = getBody(this.textoPlano);

		for (int i = 0; i < 54; i++) {
			xored[i] = header[i];
		}
		
		for (int i = 0; i < this.longitud - 54; i++) {
			xored[i + 54] = (byte) ((body[i]) ^ this.keystream[i]);
		}
		
		return xored;
	}

	private byte[] toByteArray(short[] s) {
		byte[] b = new byte[s.length / 8];
		for (int i = 0; i < (s.length / 8); i++) {
			b[i] = (byte) (s[i * 8] * 128 + s[i * 8 + 1] * 64 + s[i * 8 + 2] * 32 + s[i * 8 + 3] * 16
					+ s[i * 8 + 4] * 8 + s[i * 8 + 5] * 4 + s[i * 8 + 6] * 2 + s[i * 8 + 7]);
		}
		return b;
	}

	private short[] toShortArray(byte[] b) {
		short[] s = new short[b.length * 8];
		for (int i = 0; i < b.length; i++) {
			byte aux = b[i];
			for (int j = 7; j >= 0; j--) {
				s[i * 8 + j] = (short) (aux % 2);
				aux = (byte) (aux / 2);
			}
		}
		return s;
	}

	private byte[] getHeader(byte[] textoPlano) {
		byte[] header = new byte[54];
		for (int i = 0; i < 54; i++) {
			header[i] = textoPlano[i];
		}	
		return header;
	}

	private byte[] getBody(byte[] textoPlano) {
		int longitud = textoPlano.length - 54;
		byte[] body = new byte[longitud];
		for (int i = 0; i < longitud; i++) {
			body[i] = textoPlano[i + 54];
		}
		return body;
	}
}
