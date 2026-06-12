package us.dit.muit.fs.gestordispositivos.control;


import us.dit.muit.fs.gestordispositivos.modelo.Dispositivo;
import us.dit.muit.fs.gestordispositivos.modelo.Dispositivo.TipoDispositivo;

import java.util.List;

/**
 * Interfaz para el manejo de la persistencia de dispositivos
 */
public interface DispositivoDAO {
    /**
     * Obtiene un dispositivo por su número de serie.
     * @param numeroSerie
     * @return El dispositivo con el número de serie especificado, o null si no se encuentra.
     */
    Dispositivo getDispositivoByNumSerie(String numeroSerie);
    /**
     * Obtiene una lista de dispositivos por su tipo.
     * @param tipo
     * @return Una lista de dispositivos del tipo especificado.
     */
    List<Dispositivo> getDispositivosByType(TipoDispositivo tipo);
    /**
     * Obtiene una lista de dispositivos por el ID de un paciente.
     * Si el paciente no tiene dispositivos asociados, debe devolver una lista vacía.
     * @param idPaciente
     * @return La lista de dispositivos asociados al paciente.
     */
    List<Dispositivo> getDispositivosByPatient(String idPaciente);
    /**
     * Agrega un nuevo dispositivo a la persistencia.
     * Si el dispositivo ya existe debe lanzar la excepción IllegalArgumentException 
     * con el mensaje "Dispositivo con Numero de Serie [numeroSerie] ya existe".
     * @param newDispositivo
     * @throws IllegalArgumentException si el dispositivo ya existe.
     */
    void addDispositivo(Dispositivo newDispositivo) throws IllegalArgumentException;
    /**
     * Actualiza un dispositivo existente en la persistencia.
     * Si el dispositivo no existe debe lanzar la excepción IllegalArgumentException
     * con el mensaje "Dispositivo con Numero de Serie [numeroSerie] no existe".
     * @param updatedDispositivo
     * @throws IllegalArgumentException si el dispositivo no existe.
     */
    void updateDispositivo(Dispositivo updatedDispositivo) throws IllegalArgumentException;

}