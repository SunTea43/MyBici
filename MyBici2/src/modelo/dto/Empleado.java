package modelo.dto;


import java.util.*;

/**
 * 
 */




public class Empleado extends Persona {

    
    public Empleado() {
    }

    
    private String profesion;

    
    private String cargo;

    
    private double salario;

    public String getProfesion() {
        return profesion;
    }

    public String getCargo() {
        return cargo;
    }

    public double getSalario() {
        return salario;
    }

    
}

