package org.abelgomez.springcloud.msvc.usuarios.controller;

import jakarta.validation.Valid;
import org.abelgomez.springcloud.msvc.usuarios.models.entity.Usuario;
import org.abelgomez.springcloud.msvc.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;




    @GetMapping
    public Map<String, List<Usuario>> listar() {
        return Collections.singletonMap("users", service.listar());
    }





    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.porId(id);
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {


        if (result.hasErrors()) {
            return validar(result);
        }
        if (!usuario.getEmail().isEmpty() && service.porEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("Mensaje", "Ya existe el correo"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {


        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<Usuario> o = service.porId(id);
        if (o.isPresent()) {
            Usuario usuarioDb = o.get();
            if (!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail()) && service.porEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("Mensaje", "Ya existe el correo!"));
            }
            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> o = service.porId(id);
        if (o.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtnerAlumnosPorCurso(@RequestParam List<Long> ids) {

        return ResponseEntity.ok(service.ListarPorIds(ids));
    }


    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

}
