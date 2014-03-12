/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybirds;

import javax.swing.JFrame;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_P;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author Emilio
 */
public class FlappyBirdsClass extends JFrame implements KeyListener, MouseListener, Runnable {

    private int vidas;
    private int score;
    

// strings
    private String[] arr;
    private final String nombreArchivo = "guardar.txt";

// boleanos
    private boolean pausa;      // bool que checa si se pauso
    private boolean clicked;    // checador que checa si se movio el objeto con tecl
    private boolean chocado;
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
    private Animacion animP;

    

// sounds
    private SoundClip jump; // sonido cuando saltas
    private SoundClip backMusic; //musica de fondo del juego
    private SoundClip loseSound; //musica de fondo del juego
   
    // objetos
    private Avion avion;



    private LinkedList<Tubos> listaTubos;
    private boolean perdio;

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

        setTitle("Flappy Birds Pirate");
        addKeyListener(this);
        addMouseListener(this);
        setSize(1200, 760);

        Base.setW(getWidth());
        Base.setH(getHeight());
       

        listaTubos = new LinkedList();

        vidas = 3;
        score = 0;

        chocado = false;
        instrucciones = false;
        sonido = true;
        inicio = false;
        perdio = false;

   

//
//        fotoBarraAbajo = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resources/barraAbajo.png"));
//        fotoBarraArriba = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resources/barraArriba.png"));
       
<<<<<<< HEAD

       loseSound = new SoundClip("Resources/crashSound.wav");
       backMusic = new SoundClip("Resources/gameMusic.wav");
       jump=new SoundClip("Resources/jumpSound.wav");
      
=======
       loseSound = new SoundClip("Resources/crashSound.wav");
       backMusic = new SoundClip("Resources/gameMusic.wav");
       jump=new SoundClip("Resources/jumpSound.wav");
       heart = new ImageIcon("Resources/heart.png");
>>>>>>> 4e300215d563c91788c986443782a54edf10463b
        
        gameOver = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resources/Gameover.png"));
       
        pausaImagen = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resources/pause.png"));

        fotoAvion = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resources/avion/ah.png"));

        background = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resources/background.png"));
        tableroInstrucciones = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("Resourses/instruccionesTiroParabolico.jpg"));

        animAvion = new Animacion();
        animP = new Animacion();
       

        animAvion.sumaCuadro(fotoAvion, 700);

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

    public void restart() {

        
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
                actualiza();
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

       

            if ( avion.getPosY() - 23 < 0) {
                avion.setPosY(23);
                avion.setVelY(-1);
                System.out.println( " golpeo ");
                
            }
            if ( avion.getPosY() + avion.getAlto() >= Base.getH() ) {
                
                perdio = true;
               
            }
      



           
          
        }

    

    /**
     * El método actualiza() del <code>Applet</code> que actualiza las
     * posiciones de el objeto bueno, los objetos malos y da los tiempos para
     * cada segmento de animacion.
     */
    public void actualiza() {

        if (inicio) {
            long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;

            avion.avanza();
            
            if (avion.getMovimiento()) {
                avion.actualiza(tiempoTranscurrido);

            }
             System.out.println(avion.getVelY());
            if ( avion.getVelY() > 0 && avion.getVelY() < 5) {
                avion.setAngle(-25);
               
            } 
            else if ( avion.getVelY() > 5 ) {
                avion.setAngle(-45);
            }
            else if ( avion.getVelY() < 0 && avion.getVelY() > -5) {
                avion.setAngle (25);
                
            }
            else if (avion.getVelY() < -10) {
               avion.setAngle (45);
            }
            else if (avion.getVelY() == 0 ) {
                avion.setAngle(0);
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
public static BufferedImage toBufferedImage(Image img)
{
    if (img instanceof BufferedImage)
    {
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

        //g.drawImage(introImagen, 0, 0, this);
        g.drawImage(background, 8, 30, this);
        g.setFont(new Font("Serif", Font.BOLD, 34));
        g.drawString("Score: " + score, 44, 195);
        g.setColor(Color.red);
        g.drawString("Vidas: " + vidas, 44, 250);

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

                
                g2d.drawImage(op.filter(avionB, null), (int) avion.getPosX() , (int) avion.getPosY(), this);



              
              //  g.drawImage(avion.animacion.getImagen(), (int) avion.getPosX(), (int) avion.getPosY(), this);
                
              
                

//                Graphics2D g2d = (Graphics2D)g;
//                AffineTransform trans = new AffineTransform();
//                trans.setTransform(identity);
//                trans.rotate( Math.toRadians(45) );
//                
//                g2d.drawImage(avion.getAnimacion().getImagen(), trans, this);
//                
//               // g2d.drawImage(avion.getAnimacion().getImagen(), (int) avion.getPosX(), (int) avion.getPosY(), this);
//                g2d.dispose();
                
                if ( !inicio ) {
                    
                    g.drawString("Preciones ESPACIO para empezar ", Base.getW()/2 - 200, Base.getH()/3   );
                }
                if (instrucciones) {

                    g.drawImage(tableroInstrucciones, getWidth() / 2 - new ImageIcon(tableroInstrucciones).getIconWidth() / 2,
                            getHeight() / 2 - new ImageIcon(tableroInstrucciones).getIconHeight() / 2, this);    // Tablero de instrucciones
                }

                if (vidas == 0) {
                    perdio = true;
 
                    g.drawImage(gameOver, 0, 0, this);
                    

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

        PrintWriter fileOut = new PrintWriter(new FileWriter(nombreArchivo));
        fileOut.println(score + " "  + " " + vidas + " " + avion.getDatos()
                + " " + clicked + " " + chocado + " " + pausa + " " + instrucciones + " " + sonido + " ");
               
        fileOut.close();
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
            vidas = Integer.parseInt(arr[2]);
            avion.setPosX(Double.parseDouble(arr[5]));
            avion.setPosY(Double.parseDouble(arr[6]));
            avion.setDatos(arr[3], arr[4], arr[5], arr[6], arr[7], arr[8], arr[9]);
            clicked = Boolean.parseBoolean(arr[10]);
            chocado = Boolean.parseBoolean(arr[11]);
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
        } else if (e.getKeyCode() == KeyEvent.VK_I) {

            if (!instrucciones) {
                instrucciones = true;
                avion.pausa();

            } else {

                instrucciones = false;
                avion.despausa();

            }
        } else if (e.getKeyCode() == KeyEvent.VK_S) {

            sonido = !sonido;

        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if ( !pausa ) { // si el juego no ha empezado
                avion.setVelY(10);
                jump.play();
                inicio = true;
            }
            
        } else if (e.getKeyCode() == KeyEvent.VK_N) {
            if (perdio) {
                restart();
                vidas = 3;
                avion.volverInicio();
                perdio = false;
                avion.empieza();
                inicio = false;
                backMusic.play();
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
