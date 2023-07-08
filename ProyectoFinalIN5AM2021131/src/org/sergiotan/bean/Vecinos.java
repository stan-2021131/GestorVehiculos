
package org.sergiotan.bean;


public class Vecinos {
    private String NIT;
    private long DPI;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String municipalidad;
    private int codigoPostal;
    private String telefono;

    public Vecinos() {
    }

    public Vecinos(String NIT, long DPI, String nombres, String apellidos, String direccion, String municipalidad, int codigoPostal, String telefono) {
        this.NIT = NIT;
        this.DPI = DPI;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.municipalidad = municipalidad;
        this.codigoPostal = codigoPostal;
        this.telefono = telefono;
    }

    public String getNIT() {
        return NIT;
    }

    public void setNIT(String NIT) {
        this.NIT = NIT;
    }

    public long getDPI() {
        return DPI;
    }

    public void setDPI(long DPI) {
        this.DPI = DPI;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMunicipalidad() {
        return municipalidad;
    }

    public void setMunicipalidad(String municipalidad) {
        this.municipalidad = municipalidad;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return NIT + "-" + nombres + " " + apellidos ;
    }
    
    
}
