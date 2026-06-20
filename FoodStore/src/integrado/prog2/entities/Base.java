/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.entities;
import java.time.LocalDateTime;

/**
 *
 * @author Lila
 */
public abstract class Base {
    private Long id;
    private boolean eliminado;


    public Base() {
        this.eliminado = false;
    }

    public Base(Long id) {
        this();
        this.id = id;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }
}

