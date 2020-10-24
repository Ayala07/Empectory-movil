package com.example.tonyayala.empectory.objetos;

public class prueba {
    String nombre;
    String usuario;
    String email;
    String plan;

    public prueba() {
    }

    public prueba(String nombre, String usuario, String email, String plan) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.email = email;
        this.plan = plan;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}
