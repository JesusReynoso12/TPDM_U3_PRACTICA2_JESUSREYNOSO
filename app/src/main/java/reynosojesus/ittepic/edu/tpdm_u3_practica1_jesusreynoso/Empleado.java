package reynosojesus.ittepic.edu.tpdm_u3_practica1_jesusreynoso;

public class Empleado {
    String numempleado,nombre,edad,puesto;

    public Empleado() {
    }

    public Empleado(String numempleado, String nombre, String edad, String puesto) {
        this.numempleado = numempleado;
        this.nombre = nombre;
        this.edad = edad;
        this.puesto = puesto;
    }

    public String getNumempleado() {
        return numempleado;
    }

    public void setNumempleado(String numempleado) {
        this.numempleado = numempleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
}
