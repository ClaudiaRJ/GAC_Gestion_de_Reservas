package com.easytable.app.API;

import com.easytable.app.AdaptersYClases.Reserva; // ¡Importa la clase Reserva!
import com.easytable.app.AdaptersYClases.requests.empleados.DesactivarEmpleadoRequest;
import com.easytable.app.AdaptersYClases.requests.empleados.EmpleadoRequest;
import com.easytable.app.AdaptersYClases.requests.menuPrincipal.LoginRequest;
import com.easytable.app.AdaptersYClases.requests.menuPrincipal.RegisterClientRequest;
import com.easytable.app.AdaptersYClases.requests.reservas.ReservasRequest;
import com.easytable.app.AdaptersYClases.requests.empleados.UpdateEmpleadoRequest;
import com.easytable.app.AdaptersYClases.requests.empleados.UpdateGerenteRequest;
import com.easytable.app.AdaptersYClases.requests.empleados.UpdateRolEmpleadoRequest;
import com.easytable.app.AdaptersYClases.requests.empleados.VacacionesRequest;
import com.easytable.app.AdaptersYClases.requests.restaurantes.RestauranteDiasTrabajoRequest;
import com.easytable.app.AdaptersYClases.requests.restaurantes.RestauranteRequest;
import com.easytable.app.AdaptersYClases.requests.restaurantes.UpdateRestauranteRequest;
import com.easytable.app.AdaptersYClases.requests.usuarios.UpdateEmailRequest;
import com.easytable.app.AdaptersYClases.requests.usuarios.UpdatePasswordRequest;
import com.easytable.app.AdaptersYClases.requests.usuarios.UpdateUsuarioRequest;
import com.easytable.app.AdaptersYClases.responses.empleados.DiaTrabajoResponse;
import com.easytable.app.AdaptersYClases.responses.menuPrincipal.LoginResponse;
import com.easytable.app.AdaptersYClases.responses.menuPrincipal.RegisterResponse;
import com.easytable.app.AdaptersYClases.responses.empleados.VacacionesResponse;
import com.easytable.app.AdaptersYClases.responses.empleados.VerEmpleadoResponse;
import com.easytable.app.AdaptersYClases.responses.reservas.VerReservaResponse;
import com.easytable.app.AdaptersYClases.responses.restaurantes.VerRestauranteResponse;
import com.easytable.app.AdaptersYClases.responses.usuarios.VerUsuarioResponse;

import java.util.List;

import retrofit2.*;
import retrofit2.http.*;

import com.easytable.app.AdaptersYClases.requests.*;

public interface ApiService {

    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/altas/registro-cliente")
    Call<RegisterResponse> registerClient(@Body RegisterClientRequest request);

    @GET("/api/altas/confirm")
    Call<String> confirmRegistration(@Query("token") String token);

    // En Android ApiService
    @POST("/api/gac/usuario/update-password")
    Call<String> updatePassword(@Body UpdatePasswordRequest request);

    // En Android ApiService
    @POST("/api/gac/usuario/update-email")
    Call<String> updateEmail(@Body UpdateEmailRequest request);

    // En Android ApiService
    @POST("/api/gac/usuario/confirm-update-email")
    Call<String> confirmUpdateEmail(@Body ConfirmationTokenRequest request);


    // --- Endpoints de Restaurantes ---

    @POST("/api/restaurante/registrar-restaurante")
    Call<String> registrarRestaurante(
            @Body RestauranteRequest request // Confirmado: RestauranteRequest (asumo que existe)
    );

    @GET("/api/restaurante/mi-restaurante")
    Call<VerRestauranteResponse> verRestaurante(@Header("Authorization") String authToken);
    @PUT("/api/restaurante/update-restaurante") // Cambiado a PUT
    Call<VerRestauranteResponse> updateRestaurante(
            @Header("Authorization") String authToken,
            @Body UpdateRestauranteRequest request // Confirmado: UpdateRestauranteRequest (asumo que existe)
    );

    // Android: ApiService
    @PUT("/api/restaurante/mi-restaurante/desactivar")
    Call<String> desactivarRestaurante(@Header("Authorization") String authToken);

    // Android: ApiService
    @PUT("/api/restaurante/mi-restaurante/gerente-responsable")
    Call<String> updateGerenteResponsable(@Header("Authorization") String authToken, @Body UpdateGerenteRequest request);
    // Android: ApiService
    @PUT("/api/restaurante/mi-restaurante/dias-trabajo")
    Call<String> setDiasTrabajo(@Header("Authorization") String authToken, @Body List<RestauranteDiasTrabajoRequest> request);
    @GET("/api/restaurante/get-dias-trabajo") // Cambiado a GET
    Call<List<DiaTrabajoResponse>> getDiasTrabajo(
            @Header("Authorization") String authToken
    );

    // Android: ApiService
    @PUT("/api/restaurante/mi-restaurante/dias-vacaciones")
    Call<String> setDiasVacaciones(@Header("Authorization") String authToken, @Body VacacionesRequest request);
    @GET("/api/restaurante/get-dias-vacaciones") // Cambiado a GET
    Call<List<VacacionesResponse>> getDiasVacaciones(
            @Header("Authorization") String authToken
    );

    // Android: ApiService
    @POST("/api/restaurante/empleados")
    Call<String> registrarEmpleado(@Header("Authorization") String authToken, @Body EmpleadoRequest request);
    @GET("/api/restaurante/empleados/activos")
    Call<List<VerEmpleadoResponse>> listarEmpleadosActivos(
            @Header("Authorization") String authToken
    );

    @GET("/api/restaurante/empleados")
    Call<List<VerEmpleadoResponse>> listarTodosEmpleados(@Header("Authorization") String authToken);

    // Android: ApiService
    @PUT("/api/restaurante/empleados/{idEmpleado}")
    Call<VerEmpleadoResponse> updateEmpleado(
            @Header("Authorization") String authToken,
            @Path("idEmpleado") Long idEmpleado, // ¡Nuevo!
            @Body UpdateEmpleadoRequest request
    );

    @PUT("/api/restaurante/update-empleado") // Cambiado a PUT
    Call<VerEmpleadoResponse> updateEmpleado(
            @Header("Authorization") String authToken,
            @Body UpdateEmpleadoRequest request // Confirmado: EmpleadoUpdateRequest
    );

    @PUT("/api/restaurante/empleados/{emailEmpleado}/desactivar")
    Call<String> desactivarEmpleado( // Cambia el nombre del método si quieres
                                     @Header("Authorization") String authToken,
                                     @Path("emailEmpleado") String emailEmpleado
    );
    @PUT("/api/restaurante/desactivar-empleado") // Cambiado a PUT (si es desactivación lógica)
    Call<String> desactivarEmpleado(
            @Header("Authorization") String authToken,
            @Body DesactivarEmpleadoRequest request // Confirmado: DesactivarEmpleadoRequest (asumo que existe)
    );

    @PUT("/api/restaurante/empleados/{idEmpleado}/rol")
    Call<VerEmpleadoResponse> updateRolEmpleado(
            @Header("Authorization") String authToken,
            @Path("idEmpleado") Long idEmpleado,
            @Body UpdateRolEmpleadoRequest request
    );

    @GET("/api/restaurante/empleados/gerentes")
    Call<List<VerEmpleadoResponse>> listarGerente(@Header("Authorization") String authToken);


    // --- Endpoints de Reservas ---

    @POST("api/reservas/crear-reserva")
    Call<VerReservaResponse> crearReserva(@Header("Authorization") String authToken, @Body ReservasRequest request);

    @GET("/api/reservas/gerente/activas") // En Android
    Call<List<VerReservaResponse>> getReservasActivasGerente(
            @Header("Authorization") String authToken,
            @Query("emailGerente") String emailGerente
    );

    // En Android ApiService
    @GET("/api/reservas/usuario")
    Call<List<VerReservaResponse>> getReservasPorUsuario(@Query("email") String email);

    @PUT("/api/reservas/{id}")
    Call<Reserva> modificarReserva(
            @Path("id") Long reservaId,
            @Header("Authorization") String authToken,
            @Body Reserva requestBody,
            @Query("userType") String userType,
            @Query("email") String userEmail
    );


    @GET("/api/usuarios/perfil")
    Call<VerUsuarioResponse> getPerfilUsuario(
            @Header("Authorization") String authToken,
            @Query("email") String emailUsuario
    );

    @POST("/api/gac/usuario/update-usuario") // CAMBIO: Ahora es POST y la ruta del backend
    Call<VerUsuarioResponse> updateUsuario( // CAMBIO: Nombre del método para reflejar la acción del backend
                                            @Header("Authorization") String authToken,
                                            @Body UpdateUsuarioRequest request // CAMBIO: Usar UpdateUsuarioRequest (tendrás que crearlo en Android)
    );
}