package com.hdconsulting.springboot.webflux.client.app.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Producto {
	
	
	private String id;
	
	private String nombre;
	
	private Double precio;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createAt;
	
	private Categoria categoria;
	
	private String foto;
	
	public Producto() {
	}
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	

}
