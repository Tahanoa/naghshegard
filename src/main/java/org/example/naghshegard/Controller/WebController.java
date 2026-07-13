package org.example.naghshegard.Controller;

import org.example.naghshegard.Dto.LoginRequest;
import org.example.naghshegard.Dto.PlaceResponse;
import org.example.naghshegard.Dto.RegisterRequest;
import org.example.naghshegard.Service.TouristPlaceService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class WebController {

    private final TouristPlaceService placeService;

    public WebController(TouristPlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<PlaceResponse> places = placeService.getLatestPlaces(6);
        model.addAttribute("places", places);
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @GetMapping("/add-place")
    public String addPlace() {
        return "add-place";
    }

    @GetMapping("/my-places")
    public String myPlaces() {
        return "my-places";
    }

    @GetMapping("/place/{id}")
    public String placeDetail(@PathVariable Long id, Model model) {
        model.addAttribute("placeId", id);
        return "place-detail";
    }

    @GetMapping("/create-travelogue")
    public String createTravelogue() {
        return "create-travelogue";
    }

    @GetMapping("/travelogues")
    public String travelogues() {
        return "travelogues";
    }

    @GetMapping("/my-travelogues")
    public String myTravelogues() {
        return "my-travelogues";
    }

    @GetMapping("/travelogue/{id}")
    public String travelogueDetail(@PathVariable Long id, Model model) {
        model.addAttribute("travelogueId", id);
        return "travelogue-detail";
    }

    @GetMapping("/places")
    public String places() {
        return "places";
    }

    @GetMapping("/admin-panel")
    public String adminPanel() {
        return "admin-panel";
    }
}