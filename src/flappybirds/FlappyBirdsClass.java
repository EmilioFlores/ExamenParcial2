/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybirds;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Emilio
 */
public class FlappyBirdsClass extends JFrame implements KeyListener, MouseListener, Runnable {

    private int score;

    private int randPosY;

// strings
    private String[] arr;
    private String nombre;
    private final String nombreArchivo = "guardar.txt";

// boleanos
    private boolean pausa;      // bool que checa si se pauso
    private boolean instrucciones;
    private boolean sonido;
    private boolean inicio;

//floating
    private long tiempoActual;  // tiempo actual

    // images
    private Image dbImage;	// Imagen a proyectar	
    private Graphics dbg;	// Objeto grafico

    private Image fotoBarraAbajo;
    private Image fotoBarraArriba;

    private Image gameOver;
    private Image fotoAvion;
    private Image tableroInstrucciones;
    private Image pausaImagen;
    private Image background;

    AffineTransform identity = new AffineTransform();

// animaciones
    private Animacion animAvion;
    private Animacion animArriba;
    private Animacion animAbajo;

// sounds
    private SoundClip jump; // sonido cuando saltas
    private SoundClip backMusic; //musica de fondo del juego
    private SoundClip loseSound; //musica de perder el juego
    private SoundClip crashSound; //musica cuando chocas
    private SoundClip coin; //sonido cuando ganas punto

    // objetos
    private Avion avion;

    private LinkedList<Tubos> listaTubosArriba;
    private LinkedList<Tubos> listaTubosAbajo;
    private boolean perdio;
    private int randPosYabajo;
    private int tempScore;
    private boolean nombreIngresado;
    private int distX;

    /**
     * Se crea un objeto de la misma clase
     */
    public FlappyBirdsClass() {
        init();
        start();
    }

    /**
     * Se inicializan las variables en el metodo <I>Init</>
     * Se inicializa el tamaño del applet en 1000x500
     *
     */
    void init() {

        setTitle("Flappy Paper Plane");
        addKeyListener(this);
        addMouseListener(this);
        setSize(1200, 760);

        Base.setW(getWidth());
        Base.setH(getHeight());

        listaTubosArriba = new LinkedList();
        listaTubosAbajo = new LinkedList();

        
        score = 0;
        distX = 100;


        instrucciones = false;
        sonido = true;
        inicio = false;
        perdio = false;
        nombreIngresado = false;

        loseSound = new SoundClip("Resources/lostSound.wav");
        crashSound = new SoundClip("Resources/crashSound.wav");
        backMusic = new SoundClip("Resources/gameMusic.wav");
        jump = new SoundClip("Resources/jumpSound.wav");
        coin = new SoundClip("Resources/coin.wav");

        gameOver = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resources/Gameover.png"));
        pausaImagen = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resources/pause.png"));
        fotoAvion = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resources/avion/ah.png"));
        background = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resources/background.png"));
        tableroInstrucciones = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resourses/instruccionesTiroParabolico.jpg"));
        fotoBarraAbajo = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resources/tube1.png"));
        fotoBarraArriba = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resources/tube2.png"));

        animAvion = new Animacion();
        animArriba = new Animacion();
        animAbajo = new Animacion();

        animArriba.sumaCuadro(fotoBarraArriba, tiempoActual);
        animAbajo.sumaCuadro(fotoBarraAbajo, tiempoActual);
        animAvion.sumaCuadro(fotoAvion, 700);

        randPosY = -(300 + (int) (Math.random() * (getHeight() - 300)));
        randPosYabajo = randPosY + new ImageIcon(fotoBarraArriba).getIconHeight() + 200;

        // crea 2 tubos a partir de posiciones random
        listaTubosArriba.add(new Tubos(getWidth() + 100, randPosY, animArriba));
        listaTubosAbajo.add(new Tubos(getWidth() + 100, randPosYabajo, animAbajo));

//        randPosY = -(300 + (int) (Math.random() * (getHeight() - 300)));
//        randPosYabajo = randPosY + new ImageIcon(fotoBarraArriba).getIconHeight() + 240;
//        listaTubosArriba.add(new Tubos(getWidth() + getWidth() / 2 + distX, randPosY, animArriba));
//        listaTubosAbajo.add(new Tubos(getWidth() + getWidth() / 2 + distX, randPosYabajo, animAbajo));

        avion = new Avion(getWidth() / 2 - new ImageIcon(fotoAvion).getIconWidth() / 2 + 20, 300, animAvion);

        avion.empieza();
        backMusic.play();
    }

    /**
     * Metodo <I>start</I> sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo para la animacion este metodo
     * es llamado despues del init o cuando el usuario visita otra pagina y
     * luego regresa a la pagina en donde esta este <code>Applet</code>
     *
     */
    public void start() {
        //Crea el thread
        Thread hilo = new Thread(this);
        //Inicializa el thread
        hilo.start();
    }

    /**
     * Metodo que reinicia todas las variables en caso de que se quiera volver a
     * jugar
     */
    public void restart() {

        listaTubosArriba.clear();
        listaTubosAbajo.clear();
        avion.volverInicio();
        backMusic.play();
        avion.empieza();

        Tubos.nivel = 1;
        score = 0;
        tempScore = 0;
        distX = 100;

        perdio = false;
        inicio = false;
        nombreIngresado = false;

        listaTubosArriba.add(new Tubos(getWidth() + 100, randPosY, animArriba));
        listaTubosAbajo.add(new Tubos(getWidth() + 100, randPosYabajo, animAbajo));

//        randPosY = -(300 + (int) (Math.random() * (getHeight() - 300)));
//        randPosYabajo = randPosY + new ImageIcon(fotoBarraArriba).getIconHeight() + 240;
//        listaTubosArriba.add(new Tubos(getWidth() + getWidth() / 2 + distX, randPosY, animArriba));
//        listaTubosAbajo.add(new Tubos(getWidth() + getWidth() / 2 + distX, randPosYabajo, animAbajo));
    }

    /**
     * Metodo stop sobrescrito de la clase Applet. En este metodo se pueden
     * tomar acciones para cuando se termina de usar el Applet. Usualmente
     * cuando el usuario sale de la pagina en donde esta este Applet.
     */
    public void stop() {

    }

    /**
     * Metodo destroy sobrescrito de la clase Applet. En este metodo se toman
     * las acciones necesarias para cuando el Applet ya no va a ser usado.
     * Usualmente cuando el usuario cierra el navegador.
     */
    public void destroy() {

    }

    /**
     * Metodo <I>run</I> sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, checa si pauso el juego, actualiza
     * llama al metodo checaColision, finalmente se repinta el
     * <code>Applet</code> y luego manda a dormir el hilo.
     *
     */
    public void run() {

        //Guarda el tiempo actual del sistema
        tiempoActual = System.currentTimeMillis();

        //Ciclo principal del Applet. Actualiza y despliega en pantalla la animación hasta que el Applet sea cerrado
        while (true) {
            if (!pausa && !instrucciones) {
                try {
                    actualiza();
                } catch (IOException ex) {
                    Logger.getLogger(FlappyBirdsClass.class.getName()).log(Level.SEVERE, null, ex);
                }
             
                checaColision();
                

            }

            repaint();

            //Hace una pausa de 200 milisegundos
            try {
                Thread.sleep(60);
            } catch (InterruptedException ex) {
                // no hace nada
            }
        }

    }

    /**
     * Metodo usado para checar la colision del objeto bueno con algún objeto
     * malo de la lista de malos, checa si algun malo choco con el <code>Applet
     * </code> por la parte inferior.
     */
    public void checaColision() {

        if (avion.getPosY() - 23 < 0) {
             if (!perdio) {
                loseSound.play();
                crashSound.play();
                backMusic.stop();
            }
            perdio = true;

        }
        if (avion.getPosY() + avion.getAlto() >= Base.getH()) {
            if (!perdio) {
                loseSound.play();
                crashSound.play();
                backMusic.stop();
            }
            perdio = true;
        }

        for (Tubos tubo : listaTubosArriba) {
            if (avion.intersecta(tubo)) {
                if (!perdio) {
                    loseSound.play();
                    crashSound.play();
                    backMusic.stop();

                }
                perdio = true;

                break;
            }
        }

        for (Tubos tubo : listaTubosAbajo) {
            if (avion.intersecta(tubo)) {
                if (!perdio) {
                    loseSound.play();
                    crashSound.play();
                    backMusic.stop();
          

                }
                perdio = true;
                

                break;
            }
        }

        // aumenta score
        for (Tubos tubo : listaTubosAbajo) {

            if (avion.getPosX() + avion.getAncho() >= tubo.getPosX() + tubo.getAncho() && !tubo.getPassed()) {
                tubo.setPassed(true);
                score++;
                if (!perdio) {
                    coin.play();
                }
                tempScore++;
                break;
            }
        }
        
        
        
        

    }

    /**
     * El método actualiza() del <code>Applet</code> que actualiza las
     * posiciones de el objeto bueno, los objetos malos y da los tiempos para
     * cada segmento de animacion.
     */
    public void actualiza() throws IOException {

        if (inicio) {
            long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;

            avion.avanza();

            if (avion.getMovimiento()) {
                avion.actualiza(tiempoTranscurrido);

            }

            if (avion.getVelY() > 0 && avion.getVelY() < 5) {
                avion.setAngle(-25);

            } else if (avion.getVelY() > 5) {
                avion.setAngle(-45);
            } else if (avion.getVelY() < 0 && avion.getVelY() > -5) {
                avion.setAngle(25);

            } else if (avion.getVelY() < -10) {
                avion.setAngle(45);
            } else if (avion.getVelY() == 0) {
                avion.setAngle(0);
            }

            // tubos arriba
            for (Tubos tubo : listaTubosArriba) {
                tubo.setPosX(tubo.getPosX() - 20 * Tubos.nivel);

            }

            for (Tubos tubo : listaTubosAbajo) {
                tubo.setPosX(tubo.getPosX() - 20 * Tubos.nivel);

            }

            if (tempScore == 100) {
                tempScore = 0;
                distX +=100;
                
            }

            for (Tubos tubo : listaTubosAbajo) {

                if (tubo.getPosX() + tubo.getAncho() <= getWidth()/2 + distX && !tubo.getPassed2()) {
                    tubo.setPassed2(true);
                    
                    randPosY = -(300 + (int) (Math.random() * (getHeight() - 300)));
                    randPosYabajo = randPosY + new ImageIcon(fotoBarraArriba).getIconHeight() + 240;
               
                    listaTubosArriba.add(new Tubos(getWidth() + 100, randPosY, animArriba));
                    listaTubosAbajo.add(new Tubos(getWidth() + 100, randPosYabajo, animAbajo));

                    break;

                }
            }
            
            if ( perdio && !nombreIngresado) {
                nombre = JOptionPane.showInputDialog("Cual es tu nombre?");
                nombreIngresado = true;
                grabaArchivo();
                
            }
            

        }
    }

    /**
     * Metodo <I>update</I> sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor
     *
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     */
    public void paint(Graphics g) {
        // Inicializan el DoubleBuffer

        dbImage = createImage(this.getSize().width, this.getSize().height);
        dbg = dbImage.getGraphics();

        // Actualiza la imagen de fondo.
        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

        // Actualiza el Foreground.
        dbg.setColor(getForeground());
        paint1(dbg);

        // Dibuja la imagen actualizada
        g.drawImage(dbImage, 0, 0, this);
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    /**
     * Metodo <I>paint</I> sobrescrito de la clase <code>Applet</code>, heredado
     * de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada, ademas
     * que cuando la imagen es cargada te despliega una advertencia. ph
     *
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     */
    public void paint1(Graphics g) {

        g.drawImage(background, 8, 30, this);
        g.setFont(new Font("Serif", Font.BOLD, 34));
        g.drawString("" + score, 220, 80);

        if (avion != null) {

            if (!perdio) {

                Graphics2D g2d = (Graphics2D) g;
                // Rotation information
                BufferedImage avionB = toBufferedImage(fotoAvion);
                double rotationRequired = avion.getAngle();

                double locationX = avionB.getWidth() / 2;
                double locationY = avionB.getHeight() / 2;
                AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

                // Drawing the rotated image at the required drawing locations
                g2d.drawImage(op.filter(avionB, null), (int) avion.getPosX(), (int) avion.getPosY(), this);

                for (Tubos tubo : listaTubosArriba) {
                    g.drawImage(tubo.animacion.getImagen(), (int) tubo.getPosX(), (int) tubo.getPosY(), this);
                }

                for (Tubos tubo : listaTubosAbajo) {
                    g.drawImage(tubo.animacion.getImagen(), (int) tubo.getPosX(), (int) tubo.getPosY(), this);

                }

                if (!inicio) {

                    g.drawString("Preciones ESPACIO para empezar ", Base.getW() / 2 - 200, Base.getH() / 3);
                }
                if (instrucciones) {

                    g.drawImage(tableroInstrucciones, getWidth() / 2 - new ImageIcon(tableroInstrucciones).getIconWidth() / 2,
                            getHeight() / 2 - new ImageIcon(tableroInstrucciones).getIconHeight() / 2, this);    // Tablero de instrucciones
                }

              

            } else {
                g.drawImage(gameOver, 8, 30, this);

            }

        }
        if (pausa) {
            g.drawImage(pausaImagen, getWidth() / 2 - new ImageIcon(pausaImagen).getIconWidth() / 2, getHeight() / 2 - new ImageIcon(pausaImagen).getIconHeight() / 2, this);
        }

    }

    /**
     * Método para grabar archivo que envia todas las variables del juego dentro
     * de un string, el cual es guardado con el nombre <code> NombreArchivo
     * </code>
     *
     * @throws IOException
     */
    public void grabaArchivo() throws IOException {

        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("guardar.txt", true)))) {
        out.println(nombre + ", " + score);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    /**
     * Método que lee el <code> nombreArchivo</code> que contiene una linea con
     * todos los valores utilizados en el juego para volverse a cargar
     *
     * @throws IOException
     */
    public void leeArchivo() throws IOException {

        BufferedReader fileIn;
        try {
            fileIn = new BufferedReader(new FileReader(nombreArchivo));

            String dato = fileIn.readLine();

            arr = dato.split(" ");

            score = Integer.parseInt(arr[0]);
   
            avion.setPosX(Double.parseDouble(arr[5]));
            avion.setPosY(Double.parseDouble(arr[6]));
            avion.setDatos(arr[3], arr[4], arr[5], arr[6], arr[7], arr[8], arr[9]);
            
            pausa = Boolean.parseBoolean(arr[12]);
            instrucciones = Boolean.parseBoolean(arr[13]);
            sonido = Boolean.parseBoolean(arr[14]);
            avion.setPosX(Double.parseDouble(arr[15]));

            fileIn.close();
        } catch (FileNotFoundException e) {
        }

    }

    /**
     * Método que identifica si se movio hacia algun lado la canasta
     *
     * @param e
     */
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Metodo <I>keyPressed</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar cualquier la
     * tecla.
     *
     * @param e es el <code>evento</code> generado al presionar las teclas.
     */
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_P) {

            if (!pausa) {

                pausa = true;
                avion.pausa();

            } else {

                pausa = false;
                avion.despausa();

            }
        
        } else if (e.getKeyCode() == KeyEvent.VK_S) {

            sonido = !sonido;

        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!pausa && !perdio) { // si el juego no ha empezado
                avion.setVelY(10);
                jump.play();
                inicio = true;
            }
 if (perdio) {
                loseSound.stop();
                restart();

            }

    }
    }

    public void mousePressed(MouseEvent e) {
        

    }

    /**
     * Si se preciono el mouse sobre el objeto granada
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
 if (!pausa && !perdio) { // si el juego no ha empezado
                avion.setVelY(10);
                jump.play();
                inicio = true;
            }
 if (perdio) {
                loseSound.stop();
                restart();

            }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //     throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
