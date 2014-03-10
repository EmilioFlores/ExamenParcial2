package flappybirds;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @author daniel rodriguez
 * @author Emilio Flores
 */
public class Base {

    /**
     * Se inicializan las variables de la clase Padre
     */
    private double posX;
    private double posY;
    private int velocidad;
    Animacion animacion;
    private static int W;
    private static int H;

    /**
     * Método constructor de la clase Base
     *
     * @param posX posicion del objeto
     * @param posY posicion del objeto
     * @param animacion animacion dada para el objeto
     */
    public Base(double posX, double posY, Animacion animacion) {
        this.posX = posX;
        this.posY = posY;
        this.animacion = animacion;

    }

    /**
     * Método que fija una posicion X
     *
     * @param posX
     */
    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * Método que regresa la posicion actual del objeto en X
     *
     * @return posX
     */
    public double getPosX() {
        return this.posX;
    }

    /**
     * Método que fija la posicion Y del objeto
     *
     * @param posY
     */
    public void setPosY(double posY) {
        this.posY = posY;
    }

    /**
     * Método que regresa la posicion actual del objeto en Y
     *
     * @return
     */
    public double getPosY() {
        return this.posY;
    }

    /**
     * Método que fija una animacion para el objeto
     *
     * @param anim
     */
    public void setAnimacion(Animacion anim) {
        this.animacion = anim;
    }

    /**
     * Método que regresa la <code> Animacion </code> del objeto
     *
     * @return Anim
     */
    public Animacion getAnimacion() {
        return this.animacion;
    }

    /**
     * Método que regresa el ancho del objeto
     *
     * @return int ancho
     */
    public int getAncho() {
        return new ImageIcon(this.animacion.getImagen()).getIconWidth();
    }

    /**
     * Método que regresa el alto del objeto
     *
     * @return int alto
     */
    public int getAlto() {
        return new ImageIcon(this.animacion.getImagen()).getIconHeight();
    }

    /**
     * Método que regresa un perimetro dado unas cordenadas
     *
     * @return Rectangle
     */
    public Rectangle getPerimetro() {
        return new Rectangle((int) getPosX(), (int) getPosY(), getAncho(), getAlto());
    }

    /**
     * Método que asigna el tamaño estatico del applet
     *
     * @param w
     */
    public static void setW(int w) {
        W = w;
    }

    /**
     * Método que me regresa el tamaño estatico de mi applet
     *
     * @return W que es el width
     */
    public static int getW() {
        return W;
    }

    /**
     * Método que asigna la variable estatica del applet del tamaño
     *
     * @param h
     */
    public static void setH(int h) {
        H = h;
    }

    /**
     * Método que regresa la variable estatica del applet
     *
     * @return H que es el <I> Height </I>
     */
    public static int getH() {
        return H;
    }

    /**
     * Método que actualiza las <I>Animaciones</I> del applet
     *
     * @param tiempo
     */
    public void actualiza(long tiempo) {
        animacion.actualiza(tiempo);
    }

    /**
     * Calcula si el objeto choco con otro objeto
     *
     * @param posX del objeto que esta checando
     * @param posY del objeto que esta checando
     * @return boolean true/false
     */
    public boolean checaIntersecion(int posX, int posY) {
        return getPerimetro().contains(new Point(posX, posY));
    }

    /**
     * Método que checa si intersecto un objeto con otro objeto
     *
     * @param obj
     * @return boolean true/false
     */
    public boolean intersecta(Base obj) {
        return getPerimetro().intersects(obj.getPerimetro());
    }

    /**
     * Se verifica si se choco por el centro superior para la <I>Canasta</I>
     *
     * @param obj
     * @return
     */
    public boolean intersectaCentro(Base obj) {
        return getCentro().intersects(obj.getCentroSup());
    }

    /**
     * Crea un rectangulo a partir de unas cordenadas y lo regresa para ser
     * evaluado como perimetro
     *
     * @return Rectangle
     */
    public Rectangle getCentro() {
        return new Rectangle((int) getPosX() + getAncho() / 4, (int) getPosY() + 3 * getAlto() / 8,
                getAncho() / 2, getAlto() / 4);
    }

    /**
     * Obtiene la parte superior del rectangulo para despues checar
     * <I>Interseccion</I>
     *
     * @return Rectangle
     */
    public Rectangle getCentroSup() {
        return new Rectangle((int) getPosX(), (int) getPosY(),
                getAncho(), getAlto());
    }

    /**
     * Métodos que obtienen donde choco la pelota, por arriba, abajo, izquierda
     * o derecha
     *
     * @return
     */
    public Rectangle getUp() {
        return new Rectangle((int) getPosX(), (int) getPosY(), getAncho(), getAlto() / 5);
    }

    public Rectangle getDown() {
        return new Rectangle((int) getPosX(), (int) getPosY() + 4 * getAlto() / 5, getAncho(), getAlto() / 5);
    }

    public Rectangle getLeft() {
        return new Rectangle((int) getPosX(), (int) getPosY(), getAncho() / 5, getAlto());
    }

    public Rectangle getRight() {
        return new Rectangle((int) getPosX() + 4 * getAncho() / 5, (int) getPosY(), getAncho() / 5, getAlto());
    }

    /**
     * Métodos que regresan booleano para saber donde choco el objeto
     *
     * @return boolean
     */
    public boolean hitUp(Base otro) {
        return getUp().intersects(otro.getDown());
    }

    public boolean hitDown(Base otro) {
        return getDown().intersects(otro.getUp());
    }

    public boolean hitLeft(Base otro) {
        return getLeft().intersects(otro.getRight());
    }

    public boolean hitRight(Base otro) {
        return getRight().intersects(otro.getLeft());
    }

}
