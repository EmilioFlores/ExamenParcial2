/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybirds;

/**
 * @author daniel rodriguez
 * @author Emilio Flores
 */
public class Avion extends Base {

    // Se declaran todas las variables
    private double velX;
    private double velY;
 

    private double xInicial;
    private double yInicial;
    private boolean movimiento;
    private long tiempoInicial;
    private long tiempoPausa;
    public static int acceleracion = 4;

    /**
     * Metdo constructor
     *
     * @param posX Pos inicial x
     * @param posY Pos inicial y
     * @param animacion Animacion asignada
     */
    public Avion(double posX, double posY, Animacion animacion) {
        super(posX, posY, animacion);
        xInicial = posX;
        yInicial = posY;
        volverInicio();
    }

    /**
     * Metodo que regresa a la granada al inicio y desactiva el movimiento
     */

    public void volverInicio() {

        setPosX(xInicial);
        setPosY(yInicial);

        movimiento = false;
    }

    /**
     * Método que hace que el objeto granada avanze utilizando las formulas de
     * la fisica de desplazamiento en cualquier momento de tiempo Solo avanza si
     * se lo permite
     */
    public void avanza() {
        if (movimiento) {

             setPosY(getPosY() - acceleracion*velY );
             velY-=2;
        }
    }

    /**
     * Cuando se pica espacio, se empieza el movimiento
     */
    public void empieza() {

        movimiento = true;

        tiempoInicial = System.currentTimeMillis();

        velX = 0;
        velY = 0;

    }

    /**
     * Método de pausa para el juego
     */
    public void pausa() {
        tiempoPausa = System.currentTimeMillis();
    }

    /**
     * método que facilita el envio de datos para ser guardados en un string de
     * una linea. Regresa un string que se concatena al string de la clase main.
     *
     * @return <code> String </code>
     */
    public String getDatos() {

        String salida = String.valueOf(velX) + " " + String.valueOf(velY) + " " + String.valueOf(this.getPosX()) + " ";
        salida += String.valueOf(this.getPosY()) + " " + String.valueOf(movimiento) + " "
                + String.valueOf((System.currentTimeMillis() - tiempoInicial)) + " ";

        return salida;

    }

    /**
     * Método que carga los datos dependiendo el indice del arreglo que se le
     * envia despues de leer los datos para cargarlos en el JFrame
     *
     * @param veloX es la velocidad que llevaba el objeto en X
     * @param veloY velocidad que el objeto llevaba en Y
     * @param xIni x donde se quedo el objeto
     * @param yIn y donde se quedo el objeto
     * @param mov si el objeto se estaba o no moviento
     * @param tiempoIni tiempo en el que el objeto estaba
     * @param acceleracion la gravedad que tenia
     */
    public void setDatos(String veloX, String veloY, String xIni, String yIn, String mov, String tiempoIni, String acceleracion) {

        long dif = (Long.parseLong(tiempoIni));
        tiempoInicial = System.currentTimeMillis() - dif;
        velX = Double.parseDouble(veloX);
        velY = Double.parseDouble(veloY);
        this.setPosX(Double.parseDouble(xIni));
        this.setPosY(Double.parseDouble(yIn));
        movimiento = Boolean.parseBoolean(mov);

    }

    /**
     * método que quita la <code>pausa</code> del juego
     */
    public void despausa() {
        tiempoInicial += System.currentTimeMillis() - tiempoPausa;
    }

    /**
     * Metodo para verificar movimiento
     *
     * @return booleano de movimiento o no
     */
    public boolean getMovimiento() {
        return movimiento;
    }

    /**
     * Método para asignar velocidad en X
     *
     * @param v
     */
    public void setVelX(double v) {
        velX = v;
    }

    /**
     * Método para asignar velocidad en Y
     *
     * @param v
     */
    public void setVelY(double v) {
        velY = v;
    }

    /**
     * Método que regresa velocidad en eje X
     *
     * @return double que es velocidad X
     */
    public double getVelX() {
        return velX;
    }

    /**
     * Método que regresa la velocidad del eje Y
     *
     * @return double = vlocidad Y
     */
    public double getVelY() {
        return velY;
    }

}
