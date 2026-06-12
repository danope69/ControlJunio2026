package us.dit.muit.fs.gestordispositivos.control;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import us.dit.muit.fs.gestordispositivos.modelo.Dispositivo;

/**
 * Tests para el método bajaDispositivo(String numeroSerie) de GestorDispositivosCentro.
 *
 * Casos cubiertos:
 *   1. Dispositivo no encontrado  → IllegalArgumentException "...no existe"
 *   2. Dispositivo asignado a un paciente → IllegalArgumentException "...está asignado al paciente..."
 *   3. Dispositivo libre          → se marca como BAJA y se persiste
 *   4. Dispositivo en mantenimiento sin paciente → se marca como BAJA y se persiste
 */
@ExtendWith(MockitoExtension.class)
class BajaDispositivoTest {

    private static final String NUMERO_SERIE = "SN-001";
    private static final String ID_PACIENTE  = "PAC-42";

    @Mock
    private DispositivoDAO dispositivoDAO;

    private GestorDispositivosCentro gestor;

    @BeforeEach
    void setUp() {
        gestor = new GestorDispositivosCentro("CENTRO-1", dispositivoDAO);
    }

    // -----------------------------------------------------------------------
    // 1. Dispositivo no existe
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("Lanza IllegalArgumentException cuando el dispositivo no existe")
    void bajaDispositivo_dispositivoNoExiste_lanzaExcepcion() {
        when(dispositivoDAO.getDispositivoByNumSerie(NUMERO_SERIE)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> gestor.bajaDispositivo(NUMERO_SERIE)
        );

        assertEquals(
            "Dispositivo con Numero de Serie " + NUMERO_SERIE + " no existe",
            ex.getMessage()
        );
        verify(dispositivoDAO, never()).updateDispositivo(any());
    }

    // -----------------------------------------------------------------------
    // 2. Dispositivo asignado a un paciente
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("Lanza IllegalArgumentException cuando el dispositivo está asignado a un paciente")
    void bajaDispositivo_dispositivoAsignado_lanzaExcepcion() {
        Dispositivo dispositivo = new Dispositivo(
            Dispositivo.TipoDispositivo.MONITOR_SIGNOS, "ModeloX", NUMERO_SERIE
        );
        dispositivo.setEstado(Dispositivo.EstadoDispositivo.ASIGNADO);
        dispositivo.setPaciente(ID_PACIENTE);

        when(dispositivoDAO.getDispositivoByNumSerie(NUMERO_SERIE)).thenReturn(dispositivo);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> gestor.bajaDispositivo(NUMERO_SERIE)
        );

        assertEquals(
            "Dispositivo con Numero de Serie " + NUMERO_SERIE +
            " está asignado al paciente " + ID_PACIENTE +
            " y no se puede dar de baja",
            ex.getMessage()
        );
        verify(dispositivoDAO, never()).updateDispositivo(any());
    }

    // -----------------------------------------------------------------------
    // 3. Dispositivo libre → baja exitosa
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("Da de baja correctamente un dispositivo libre")
    void bajaDispositivo_dispositivoLibre_marcaBajaYPersiste() {
        Dispositivo dispositivo = new Dispositivo(
            Dispositivo.TipoDispositivo.RESPIRADOR, "ModeloY", NUMERO_SERIE
        );
        // Estado LIBRE por defecto en el constructor, paciente == null

        when(dispositivoDAO.getDispositivoByNumSerie(NUMERO_SERIE)).thenReturn(dispositivo);

        assertDoesNotThrow(() -> gestor.bajaDispositivo(NUMERO_SERIE));

        assertEquals(Dispositivo.EstadoDispositivo.BAJA, dispositivo.getEstado());
        verify(dispositivoDAO).updateDispositivo(dispositivo);
    }

    // -----------------------------------------------------------------------
    // 4. Dispositivo en mantenimiento sin paciente → baja exitosa
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("Da de baja correctamente un dispositivo en mantenimiento sin paciente asignado")
    void bajaDispositivo_dispositivoEnMantenimientoSinPaciente_marcaBajaYPersiste() {
        Dispositivo dispositivo = new Dispositivo(
            Dispositivo.TipoDispositivo.DESFIBRILADOR, "ModeloZ", NUMERO_SERIE
        );
        dispositivo.setEstado(Dispositivo.EstadoDispositivo.EN_MANTENIMIENTO);
        // paciente == null → no debe lanzar excepción

        when(dispositivoDAO.getDispositivoByNumSerie(NUMERO_SERIE)).thenReturn(dispositivo);

        assertDoesNotThrow(() -> gestor.bajaDispositivo(NUMERO_SERIE));

        assertEquals(Dispositivo.EstadoDispositivo.BAJA, dispositivo.getEstado());
        verify(dispositivoDAO).updateDispositivo(dispositivo);
    }
}
