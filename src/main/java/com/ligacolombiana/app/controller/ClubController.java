package com.ligacolombiana.app.controller;

import com.ligacolombiana.app.entity.*;
import com.ligacolombiana.app.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class ClubController {

    private final ClubRepository clubRepository;
    private final EntrenadorRepository entrenadorRepository;
    private final JugadorRepository jugadorRepository;
    private final AsociacionRepository asociacionRepository;
    private final CompeticionRepository competicionRepository;

    public ClubController(ClubRepository clubRepository,
                         EntrenadorRepository entrenadorRepository,
                         JugadorRepository jugadorRepository,
                         AsociacionRepository asociacionRepository,
                         CompeticionRepository competicionRepository) {
        this.clubRepository = clubRepository;
        this.entrenadorRepository = entrenadorRepository;
        this.jugadorRepository = jugadorRepository;
        this.asociacionRepository = asociacionRepository;
        this.competicionRepository = competicionRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ===================== CLUBES =====================
    @GetMapping("/clubs")
    public String listarClubs(Model model) {
        model.addAttribute("clubs", clubRepository.findAll());
        return "clubs";
    }

    @GetMapping("/clubs/nuevo")
    public String nuevoClub(Model model) {
        model.addAttribute("club", new Club());
        model.addAttribute("entrenadores", entrenadorRepository.findAll());
        model.addAttribute("jugadores", jugadorRepository.findAll());
        model.addAttribute("asociaciones", asociacionRepository.findAll());
        model.addAttribute("competiciones", competicionRepository.findAll());
        return "club-form";
    }

    @PostMapping("/clubs")
    public String guardarClub(@ModelAttribute Club club,
                            @RequestParam(required = false) Long entrenadorId,
                            @RequestParam(required = false) List<Long> jugadorIds,
                            @RequestParam(required = false) Long asociacionId,
                            @RequestParam(required = false) List<Long> competicionIds) {

        // Cargar todas las entidades correctamente desde la base de datos
        if (entrenadorId != null) {
            entrenadorRepository.findById(entrenadorId).ifPresent(club::setEntrenador);
        }
        if (asociacionId != null) {
            asociacionRepository.findById(asociacionId).ifPresent(club::setAsociacion);
        }
        if (jugadorIds != null && !jugadorIds.isEmpty()) {
            club.setJugadores(jugadorRepository.findAllById(jugadorIds));
        }
        if (competicionIds != null && !competicionIds.isEmpty()) {
            club.setCompeticiones(competicionRepository.findAllById(competicionIds));
        }

        clubRepository.save(club);
        return "redirect:/clubs";
    }

    @GetMapping("/clubs/editar/{id}")
    public String editarClub(@PathVariable Long id, Model model) {
        Club club = clubRepository.findById(id).orElse(new Club());
        model.addAttribute("club", club);
        model.addAttribute("entrenadores", entrenadorRepository.findAll());
        model.addAttribute("jugadores", jugadorRepository.findAll());
        model.addAttribute("asociaciones", asociacionRepository.findAll());
        model.addAttribute("competiciones", competicionRepository.findAll());
        return "club-form";
    }
    
    // ======================= ELIMINAR CLUB  ==================
    
    @GetMapping("/clubs/eliminar/{id}")
    public String eliminarClub(@PathVariable Long id) {
        clubRepository.deleteById(id);
        return "redirect:/clubs";
    }
    
    // ==================== ELIMINAR ENTRENADOR ====================
    @GetMapping("/entrenadores/eliminar/{id}")
    public String eliminarEntrenador(@PathVariable Long id) {
        entrenadorRepository.deleteById(id);
        return "redirect:/entrenadores";
    }

    // ==================== ELIMINAR JUGADOR ====================
    @GetMapping("/jugadores/eliminar/{id}")
    public String eliminarJugador(@PathVariable Long id) {
        jugadorRepository.deleteById(id);
        return "redirect:/jugadores";
    }

    // ==================== ELIMINAR ASOCIACION ====================
    @GetMapping("/asociaciones/eliminar/{id}")
    public String eliminarAsociacion(@PathVariable Long id) {
        // Primero quitamos la asociación de todos los clubes
        List<Club> clubs = clubRepository.findAll();
        for (Club club : clubs) {
            if (club.getAsociacion() != null && club.getAsociacion().getId().equals(id)) {
                club.setAsociacion(null);
                clubRepository.save(club);
            }
        }
        asociacionRepository.deleteById(id);
        return "redirect:/asociaciones";
    }

    // ==================== ELIMINAR COMPETICION ====================
    @GetMapping("/competiciones/eliminar/{id}")
    public String eliminarCompeticion(@PathVariable Long id) {
        // Primero quitamos la competición de todos los clubes
        List<Club> clubs = clubRepository.findAll();
        for (Club club : clubs) {
            club.getCompeticiones().removeIf(c -> c.getId().equals(id));
            clubRepository.save(club);
        }
        competicionRepository.deleteById(id);
        return "redirect:/competiciones";
    }

    // ===================== ENTRENADORES =====================
    @GetMapping("/entrenadores")
    public String listarEntrenadores(Model model) {
        model.addAttribute("entrenadores", entrenadorRepository.findAll());
        return "entrenadores";
    }

    @GetMapping("/entrenadores/nuevo")
    public String nuevoEntrenador(Model model) {
        model.addAttribute("entrenador", new Entrenador());
        return "entrenador-form";
    }

    @PostMapping("/entrenadores")
    public String guardarEntrenador(@ModelAttribute Entrenador entrenador) {
        entrenadorRepository.save(entrenador);
        return "redirect:/entrenadores";
    }
    
    @GetMapping("/entrenadores/editar/{id}")
    public String editarEntrenador(@PathVariable Long id, Model model) {
        model.addAttribute("entrenador", entrenadorRepository.findById(id).orElse(new Entrenador()));
        return "entrenador-form";
    }

    // ===================== JUGADORES =====================
    @GetMapping("/jugadores")
    public String listarJugadores(Model model) {
        model.addAttribute("jugadores", jugadorRepository.findAll());
        return "jugadores";
    }

    @GetMapping("/jugadores/nuevo")
    public String nuevoJugador(Model model) {
        model.addAttribute("jugador", new Jugador());
        return "jugador-form";
    }

    @PostMapping("/jugadores")
    public String guardarJugador(@ModelAttribute Jugador jugador) {
        jugadorRepository.save(jugador);
        return "redirect:/jugadores";
    }
    
    @GetMapping("/jugadores/editar/{id}")
    public String editarJugador(@PathVariable Long id, Model model) {
        model.addAttribute("jugador", jugadorRepository.findById(id).orElse(new Jugador()));
        return "jugador-form";
    }

    // ===================== ASOCIACIONES =====================
    @GetMapping("/asociaciones")
    public String listarAsociaciones(Model model) {
        model.addAttribute("asociaciones", asociacionRepository.findAll());
        return "asociaciones";
    }

    @GetMapping("/asociaciones/nuevo")
    public String nuevaAsociacion(Model model) {
        model.addAttribute("asociacion", new Asociacion());
        return "asociacion-form";
    }

    @PostMapping("/asociaciones")
    public String guardarAsociacion(@ModelAttribute Asociacion asociacion) {
        asociacionRepository.save(asociacion);
        return "redirect:/asociaciones";
    }
    
    @GetMapping("/asociaciones/editar/{id}")
    public String editarAsociacion(@PathVariable Long id, Model model) {
        model.addAttribute("asociacion", asociacionRepository.findById(id).orElse(new Asociacion()));
        return "asociacion-form";
    }

    // ===================== COMPETICIONES =====================
    @GetMapping("/competiciones")
    public String listarCompeticiones(Model model) {
        model.addAttribute("competiciones", competicionRepository.findAll());
        return "competiciones";
    }

    @GetMapping("/competiciones/nuevo")
    public String nuevaCompeticion(Model model) {
        model.addAttribute("competicion", new Competicion());
        return "competicion-form";
    }

    @PostMapping("/competiciones")
    public String guardarCompeticion(@ModelAttribute Competicion competicion) {
        competicionRepository.save(competicion);
        return "redirect:/competiciones";
    }
    
    @GetMapping("/competiciones/editar/{id}")
    public String editarCompeticion(@PathVariable Long id, Model model) {
        model.addAttribute("competicion", competicionRepository.findById(id).orElse(new Competicion()));
        return "competicion-form";
    }
}