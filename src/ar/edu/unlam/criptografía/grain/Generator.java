package ar.edu.unlam.criptografía.grain;

public class Generator {
	
	private short[] clave;
	private short[] semilla;
	private short[] lfsr;
	private short[] nfsr;
	
	private static final Integer LONGITUD_CLAVE = 80;
	private static final Integer LONGITUD_SEMILLA = 64;

	public Generator(short[] clave, short[] semilla) throws Exception {

		if (clave.length == LONGITUD_CLAVE) {
			this.clave = new short[LONGITUD_CLAVE];
			for (int i = 0; i < this.clave.length; i++) {
				this.clave[i] = clave[i];
			}

		} else {
			throw new Exception("Longitud de la clave: " + clave.length + ". Longitud requerida: " + LONGITUD_CLAVE);
		}

		if (semilla.length == LONGITUD_SEMILLA) {
			this.semilla = new short[LONGITUD_SEMILLA];
			for (int i = 0; i < this.semilla.length; i++) {
				this.semilla[i] = semilla[i];
			}
		} else {
			throw new Exception("Longitud de la semilla: " + semilla.length + ". Longitud requerida: " + LONGITUD_SEMILLA);
		}

		this.lfsr = new short[LONGITUD_CLAVE];
		this.nfsr = new short[LONGITUD_CLAVE];

		inicializar();

	}

	private void inicializar() {
		int i;

		for (i = 0; i < clave.length; i++) {
			nfsr[i] = clave[i];
		}

		for (i = 0; i < semilla.length; i++) {
			lfsr[i] = semilla[i];
		}

		for (; i < lfsr.length; i++) {
			lfsr[i] = 1;
		}

		for (i = 0; i < 160; i++) {
			clockInicial();
		}

	}

	public short[] generarKeystream(int bytes) {
		short[] keystream = new short[bytes * 8];

		for (int i = 0; i < keystream.length; i++) {
			keystream[i] = clock();
		}

		return keystream;
	}

	private short clock() {
		short output = output();
		shiftLeftNFSR(feedbackNFSR());
		shiftLeftLFSR(feedbackLFSR());
		return output;
	}

	private void clockInicial() {
		short output = output();
		shiftLeftNFSR((short) ((feedbackNFSR() + output) % 2));
		shiftLeftLFSR((short) ((feedbackLFSR() + output) % 2));
	}

	private short feedbackLFSR() {
		return (short) ((lfsr[0] + lfsr[13] + lfsr[23] + lfsr[38] + lfsr[51] + lfsr[62]) % 2);
	}

	private void shiftLeftLFSR(short bit) {
		for (int i = 0; i < lfsr.length - 1; i++) {
			lfsr[i] = lfsr[i + 1];
		}
		lfsr[lfsr.length - 1] = bit;
	}

	private short feedbackNFSR() {
		return (short) ((lfsr[0] + nfsr[62] + nfsr[60] + nfsr[52] + nfsr[45] + nfsr[37] + nfsr[33] + nfsr[28] + nfsr[21]
				+ nfsr[14] + nfsr[9] + nfsr[0] + nfsr[63] * nfsr[60] + nfsr[37] * nfsr[33] + nfsr[15] * nfsr[9]
				+ nfsr[60] * nfsr[52] * nfsr[45] + nfsr[33] * nfsr[28] * nfsr[21]
				+ nfsr[63] * nfsr[45] * nfsr[28] * nfsr[9] + nfsr[60] * nfsr[52] * nfsr[37] * nfsr[33]
				+ nfsr[63] * nfsr[60] + nfsr[63] * nfsr[60] * nfsr[52] * nfsr[45] * nfsr[37]
				+ nfsr[33] * nfsr[28] * nfsr[21] * nfsr[15] * nfsr[9]
				+ nfsr[52] * nfsr[45] * nfsr[37] * nfsr[33] * nfsr[28] * nfsr[21]) % 2);
	}

	private void shiftLeftNFSR(short bit) {
		for (int i = 0; i < nfsr.length - 1; i++) {
			nfsr[i] = nfsr[i + 1];
		}
		nfsr[nfsr.length - 1] = bit;
	}

	private short output() {
		short bi = (short) (nfsr[1] + nfsr[2] + nfsr[4] + nfsr[10] + nfsr[31] + nfsr[43] + nfsr[56]);
		short h = filtro();
		return (short) ((bi + h) % 2);
	}
	
	private short filtro() {
		return (short) (lfsr[25] + nfsr[63] + lfsr[3] * lfsr[64] + lfsr[46] * lfsr[64] + lfsr[64] * nfsr[63]
				+ lfsr[3] * lfsr[25] * lfsr[46] + lfsr[3] * lfsr[46] * lfsr[64] + lfsr[3] * lfsr[46] * nfsr[63]
				+ lfsr[25] * lfsr[46] * nfsr[63] + lfsr[46] * lfsr[64] * nfsr[63]);
	}

}
