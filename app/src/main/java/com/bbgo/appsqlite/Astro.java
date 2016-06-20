package com.bbgo.appsqlite;

public class Astro {
    private long id;
    private String nome;
    private String desc;
    private String tipo;

    public Astro(int id, String nome, String desc, String tipo) {
        this.id = id;
        this.nome = nome;
        this.desc = desc;
        this.tipo = tipo;
    }

    public Astro() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Astro{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", desc='" + desc + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}