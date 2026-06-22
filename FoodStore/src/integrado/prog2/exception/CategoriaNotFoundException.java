package integrado.prog2.exception;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */

/**
 *
 * @author Lila
 */
public class CategoriaNotFoundException extends RuntimeException {

    /**
     * Creates a new instance of <code>CategoriaNotFoundException</code> without
     * detail message.
     */
    public CategoriaNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CategoriaNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CategoriaNotFoundException(String msg) {
        super(msg);
    }
}
