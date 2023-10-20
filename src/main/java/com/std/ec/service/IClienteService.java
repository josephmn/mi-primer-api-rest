package com.std.ec.service;

import com.std.ec.model.dto.ClienteDto;
import com.std.ec.model.entity.Cliente;

import java.util.List;

/**
 *
 * @author ALEJANDRO
 */
public interface IClienteService {

    // Listar cliente
    // <> estos son diamos
    List<Cliente> listAll();
    
    // Guardar cliente
    Cliente save(ClienteDto cliente);
    
    // Buscar por Id de cliente
    Cliente findById(Integer id);

    // Eliminar cliente
    void delete(Cliente cliente);

    // Si existe el Id de cliente
    boolean existsById(Integer id);
    
}
