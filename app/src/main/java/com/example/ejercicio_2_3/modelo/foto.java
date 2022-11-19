package com.example.ejercicio_2_3.modelo;

public class foto {
    private String descrip;
    private byte[] foto;
    private Integer id;


    public foto() {
    }

    public foto(Integer id, String description, byte[] foto) {
        this.id = id;
        this.descrip = description;
        this.foto = foto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String description) {
        this.descrip = description;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}
