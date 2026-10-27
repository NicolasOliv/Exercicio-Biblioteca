package com.senai.biblioteca.controller;

import com.senai.biblioteca.model.Livro;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class LivroController {

    private final List<Livro> livros = new CopyOnWriteArrayList<>();
    private final AtomicLong proximoId = new AtomicLong(1);

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("mensagem", "Bem-vindo ao sistema de gerenciamento da Biblioteca CloudTech Solutions");
    }

    @GetMapping("/livros")
    public List<Livro> listar() {
        return livros;
    }

    @GetMapping("/livros/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        return livros.stream()
            .filter(l -> l.getId().equals(id))
            .findFirst()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/livros/cadastrar")
    public Livro cadastrar(@RequestBody Livro livro) {
        livro.setId(proximoId.getAndIncrement());
        livros.add(livro);
        return livro;
    }

    @PutMapping("/livros/alterar/{id}")
    public ResponseEntity<Livro> alterar(@PathVariable Long id, @RequestBody Livro dadosAtualizados) {
        for (Livro livro : livros) {
            if (livro.getId().equals(id)) {
                livro.setTitulo(dadosAtualizados.getTitulo());
                livro.setAutor(dadosAtualizados.getAutor());
                livro.setEditora(dadosAtualizados.getEditora());
                livro.setAno(dadosAtualizados.getAno());
                livro.setCategoria(dadosAtualizados.getCategoria());
                livro.setStatus(dadosAtualizados.getStatus());
                return ResponseEntity.ok(livro);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/documentacao")
    public Map<String, List<String>> documentacao() {
        return Map.of("endpoints", List.of(
            "/", "/livros", "/livros/cadastrar", "/livros/alterar/{id}", "/livros/{id}", "/documentacao"
        ));
    }
}