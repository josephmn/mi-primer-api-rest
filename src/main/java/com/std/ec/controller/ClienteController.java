package com.std.ec.controller;

import com.std.ec.model.dto.ClienteDto;
import com.std.ec.model.entity.Cliente;
import com.std.ec.model.payload.MensajeResponse;
import com.std.ec.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class ClienteController {

    @Autowired
    private IClienteService clienteServices;

    // Lista de cliente
    @GetMapping("clientes")
    // @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showAll(){
        List<Cliente> getList = clienteServices.listAll();
        if (getList == null){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No hay registros.")
                            .object(null)
                            .build(),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("")
                        .object(getList)
                        .build(),
                HttpStatus.OK);
    }

    // Crear cliente
    @PostMapping("cliente")
    // @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody ClienteDto clienteDto){
        Cliente clienteSave = null;
        try {
            clienteSave = clienteServices.save(clienteDto);
            clienteDto = ClienteDto.builder()
                    .idCliente(clienteSave.getIdCliente())
                    .nombre(clienteSave.getNombre())
                    .apellido(clienteSave.getApellido())
                    .correo(clienteSave.getCorreo())
                    .fechaRegistro(clienteSave.getFechaRegistro())
                    .build();
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Guardado correctamente")
                    .object(clienteDto)
                    .build(), HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.METHOD_NOT_ALLOWED);
        }

    }

    // Actualizar cliente (1er metodo con Id en la URL) bueno
    /*
    @PutMapping("cliente/{id}")
    // @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody ClienteDto clienteDto, @PathVariable Integer id){
        Cliente clienteUpdate = null;
        try {
            Cliente findCliente = clienteServices.findById(id);
            if (findCliente != null){
                clienteUpdate = clienteServices.save(clienteDto);
                clienteDto = ClienteDto.builder()
                        .idCliente(clienteUpdate.getIdCliente())
                        .nombre(clienteUpdate.getNombre())
                        .apellido(clienteUpdate.getApellido())
                        .correo(clienteUpdate.getCorreo())
                        .fechaRegistro(clienteUpdate.getFechaRegistro())
                        .build();
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Guardado correctamente")
                        .object(clienteDto)
                        .build(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El registro  que intenta actualizar no se encuentra en la base de datos.")
                                .object(null)
                                .build(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }


        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */

    // Actualizar cliente (2do metodo con Id desde RequestBody clienteDto) pero no se recomienda ya que no se obliga a enviar el {id}
    /*
    @PutMapping("cliente")
    // @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody ClienteDto clienteDto){
        Cliente clienteUpdate = null;
        try {
            Cliente findCliente = clienteServices.findById(clienteDto.getIdCliente());
            if (findCliente != null){
                clienteUpdate = clienteServices.save(clienteDto);
                clienteDto = ClienteDto.builder()
                        .idCliente(clienteUpdate.getIdCliente())
                        .nombre(clienteUpdate.getNombre())
                        .apellido(clienteUpdate.getApellido())
                        .correo(clienteUpdate.getCorreo())
                        .fechaRegistro(clienteUpdate.getFechaRegistro())
                        .build();
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Guardado correctamente")
                        .object(clienteDto)
                        .build(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El registro  que intenta actualizar no se encuentra en la base de datos.")
                                .object(null)
                                .build(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }


        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
     */

    // Actualizar cliente (3er metodo con Id en la URL, mejorando el 1er metodo)
    @PutMapping("cliente/{id}")
    // @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody ClienteDto clienteDto, @PathVariable Integer id){
        Cliente clienteUpdate = null;
        try {
            if (clienteServices.existsById(id)){
                clienteDto.setIdCliente(id);
                clienteUpdate = clienteServices.save(clienteDto);
                clienteDto = ClienteDto.builder()
                        .idCliente(clienteUpdate.getIdCliente())
                        .nombre(clienteUpdate.getNombre())
                        .apellido(clienteUpdate.getApellido())
                        .correo(clienteUpdate.getCorreo())
                        .fechaRegistro(clienteUpdate.getFechaRegistro())
                        .build();
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Guardado correctamente")
                        .object(clienteDto)
                        .build(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El registro que intenta actualizar no se encuentra en la base de datos.")
                                .object(null)
                                .build(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar cliente
    @DeleteMapping("cliente/{id}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Integer id){

        /* @Map creamos un mapa de errores
        Map<String, Object> response = new HashMap<>();
         */

        try {
            Cliente clienteDelete = clienteServices.findById(id);
            clienteServices.delete(clienteDelete);
            return new ResponseEntity<>(clienteDelete, HttpStatus.NO_CONTENT);
        } catch (DataAccessException exDt) {
            // clase personalizada (MensajeResponse)
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                        .mensaje(exDt.getMessage())
                        .object(null)
                        .build(),
                    HttpStatus.METHOD_NOT_ALLOWED);

            /* @Map
            response.put("mensaje", exDt.getMessage());
            response.put("cliente", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
             */
        }
    }

    // Consultar por id
    @GetMapping("cliente/{id}")
    // @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showById(@PathVariable Integer id){
        Cliente cliente = clienteServices.findById(id);
        if (cliente == null){
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El registro que intenta buscar no existe")
                            .object(null)
                            .build(),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("")
                        .object(ClienteDto.builder()
                                .idCliente(cliente.getIdCliente())
                                .nombre(cliente.getNombre())
                                .apellido(cliente.getApellido())
                                .correo(cliente.getCorreo())
                                .fechaRegistro(cliente.getFechaRegistro())
                                .build())
                        .build(),
                HttpStatus.OK);
    }
}
