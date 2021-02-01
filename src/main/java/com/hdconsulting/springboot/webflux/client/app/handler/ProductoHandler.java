package com.hdconsulting.springboot.webflux.client.app.handler;

import java.net.URI;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.hdconsulting.springboot.webflux.client.app.models.Producto;
import com.hdconsulting.springboot.webflux.client.app.models.services.ProductoService;

import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {

	@Autowired
	private ProductoService service;

	public Mono<ServerResponse> listar(ServerRequest request) {

		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(service.findAll(), Producto.class);

	}

	public Mono<ServerResponse> ver(ServerRequest request) {

		String id = request.pathVariable("id");

		return service.findById(id)
				.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				//.syncBody(p))
						.body(BodyInserters.fromValue(p)))
				.switchIfEmpty(ServerResponse.notFound().build());

	}

	public Mono<ServerResponse> crear(ServerRequest request) {

		Mono<Producto> producto = request.bodyToMono(Producto.class);
		
		
		return producto.flatMap(p -> {
			if (p.getCreateAt() == null) {
				p.setCreateAt(new Date());
			}
			return service.save(p);
		}).flatMap(p -> ServerResponse.created(URI.create("/api/client/".concat(p.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				//.syncBody(p)) 
				.body(BodyInserters.fromValue(p)))
				.onErrorResume(error -> {
					WebClientResponseException errorResponse = (WebClientResponseException) error;
					
					if (errorResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
						return ServerResponse.badRequest()
								.contentType(MediaType.APPLICATION_JSON)
								.syncBody(errorResponse.getResponseBodyAsString());
					}
					return Mono.error(errorResponse);
				});
	}

	public Mono<ServerResponse> editar(ServerRequest request) {

		Mono<Producto> producto = request.bodyToMono(Producto.class);
		String id = request.pathVariable("id");
		
		return producto.flatMap(p -> ServerResponse.created(URI.create("/api/client/".concat(id)))
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.update(p, id), Producto.class));
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest request) {
		String id = request.pathVariable("id");
		
		return service.delete(id)
				.then(ServerResponse.noContent().build());
		
	}

}
