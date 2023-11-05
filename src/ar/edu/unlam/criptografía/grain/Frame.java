package ar.edu.unlam.criptografía.grain;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	private JPanel contentPane;
	private JTextField txtFieldClave;
	private JTextField txtFieldSemilla;
	private BufferedImage imagenInicial = null;
	private BufferedImage imagenFinal = null;
	private byte[] imagenByteArray;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Frame() {
		setTitle("Cifrador Grain V1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 690, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(this);
		setResizable(false);

		JPanel panelImagenInicial = new JPanel();
		panelImagenInicial.setBorder(new LineBorder(Color.DARK_GRAY));
		panelImagenInicial.setBounds(20, 39, 200, 240);
		panelImagenInicial.setBackground(Color.WHITE);
		contentPane.add(panelImagenInicial);
		panelImagenInicial.setLayout(null);

		JLabel lblImagenInicial = new JLabel("");
		lblImagenInicial.setBounds(0, 0, 200, 240);
		panelImagenInicial.add(lblImagenInicial);
		panelImagenInicial.revalidate();
		panelImagenInicial.repaint();

		JPanel panelImagenFinal = new JPanel();
		panelImagenFinal.setBorder(new LineBorder(Color.DARK_GRAY));
		panelImagenFinal.setBounds(463, 37, 200, 240);
		panelImagenFinal.setBackground(Color.WHITE);
		contentPane.add(panelImagenFinal);
		panelImagenFinal.setLayout(null);

		JLabel lblImagenFinal = new JLabel("");
		lblImagenFinal.setBounds(0, 0, 200, 240);
		panelImagenFinal.add(lblImagenFinal);
		panelImagenFinal.revalidate();
		panelImagenFinal.repaint();

		JLabel lblClave = new JLabel("Clave - 10 Caracteres");
		lblClave.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblClave.setHorizontalAlignment(SwingConstants.CENTER);
		lblClave.setBounds(145, 370, 150, 14);
		contentPane.add(lblClave);
		
		txtFieldClave = new JTextField(10);
		txtFieldClave.setToolTipText("");
		txtFieldClave.setBounds(135, 390, 181, 20);
		txtFieldClave.setText("1234567890");
		contentPane.add(txtFieldClave);
		txtFieldClave.setColumns(10);
		
		JLabel lblSemilla = new JLabel("Semilla - 8 Caracteres");
		lblSemilla.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSemilla.setHorizontalAlignment(SwingConstants.CENTER);
		lblSemilla.setBounds(380, 370, 120, 14);
		contentPane.add(lblSemilla);

		txtFieldSemilla = new JTextField();
		txtFieldSemilla.setBounds(355, 390, 181, 20);
		txtFieldSemilla.setText("12345678");
		contentPane.add(txtFieldSemilla);
		txtFieldSemilla.setColumns(10);

		JButton btnAbrirImagen = new JButton("Abrir imagen");
		btnAbrirImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Archivos de imagen", "jpg", "jpeg", "png", "bmp", "gif");
				fileChooser.setFileFilter(fileNameExtensionFilter);
				
				int result = fileChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();
					if (!Arrays.asList("jpg", "jpeg", "png", "bmp", "gif").contains(fileExtension)) {
						JOptionPane.showMessageDialog(null, "Debe seleccionar un archivo de imagen (jpg, jpeg, png, bmp, gif)", "Archivo inv�lido", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					try {
						imagenInicial = ImageIO.read(file);
					} catch (IOException e) {
						JOptionPane.showMessageDialog(Frame.this, "Error al abrir la imagen para cifrar", "Error",
								JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}

					ImageIcon imageIcon = new ImageIcon(imagenInicial.getScaledInstance(200, 240, Image.SCALE_SMOOTH));
					lblImagenInicial.setIcon(imageIcon);
					lblImagenFinal.setIcon(null);
					imagenFinal = null;
					imagenByteArray = null;
				}
			}
		});
		btnAbrirImagen.setBounds(54, 312, 130, 23);
		contentPane.add(btnAbrirImagen);

		JButton btnGuardarImagen = new JButton("Guardar imagen");
		btnGuardarImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (imagenFinal == null) {
					JOptionPane.showMessageDialog(null, "Debe cifrar una imagen para poder guardarla", "Imagen inv�lida", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showSaveDialog(null);
				File file = null;
				
				if (result == JFileChooser.APPROVE_OPTION) {
					file = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".bmp");
				}
				
				try {
					ImageIO.write(imagenFinal, "bmp", file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnGuardarImagen.setBounds(500, 312, 130, 23);
		contentPane.add(btnGuardarImagen);

		String rutaImagen = "decryptIcon.png";
		ImageIcon decryptIcon = new ImageIcon(rutaImagen, "Cifrar/Descifrar");
		decryptIcon = new ImageIcon(decryptIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
		JButton btnCifrarDescifrar = new JButton(decryptIcon);
		
		btnCifrarDescifrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtFieldClave.getDocument().getLength() != 10) {
					JOptionPane.showMessageDialog(null, "La clave debe ser de 10 caracteres", "Clave inv�lida",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (txtFieldSemilla.getDocument().getLength() != 8) {
					JOptionPane.showMessageDialog(null, "La semilla debe ser de 8 caracteres", "Semilla inv�lida",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (imagenInicial == null) {
					JOptionPane.showMessageDialog(null, "Debe cargar una imagen para cifrar", "Imagen inv�lida",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try {
					imagenByteArray = Converter.imageToByteArray(imagenInicial);
					Cipher grain = new Cipher(txtFieldClave.getText().getBytes(),
							txtFieldSemilla.getText().getBytes(), imagenByteArray);
					imagenFinal = Converter.byteArrayToImage(grain.xor());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				ImageIcon imageIcon = new ImageIcon(imagenFinal.getScaledInstance(200, 240, Image.SCALE_SMOOTH));
				lblImagenFinal.setIcon(imageIcon);
			}
		});
		btnCifrarDescifrar.setBounds(310, 125, 60, 60);
		contentPane.add(btnCifrarDescifrar);
	}
}
