package reynosojesus.ittepic.edu.tpdm_u3_practica1_jesusreynoso;

public class mesa {
    String nummesa,lugar,cantidadpersona,reservado;

    public mesa() {
    }

    public mesa(String nummesa, String lugar, String cantidadpersona, String reservado) {
        this.nummesa = nummesa;
        this.lugar = lugar;
        this.cantidadpersona = cantidadpersona;
        this.reservado = reservado;
    }

    public String getNummesa() {
        return nummesa;
    }

    public void setNummesa(String nummesa) {
        this.nummesa = nummesa;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getCantidadpersona() {
        return cantidadpersona;
    }

    public void setCantidadpersona(String cantidadpersona) {
        this.cantidadpersona = cantidadpersona;
    }

    public String getReservado() {
        return reservado;
    }

    public void setReservado(String reservado) {
        this.reservado = reservado;
    }
}
