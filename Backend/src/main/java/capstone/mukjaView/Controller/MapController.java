package capstone.mukjaView.Controller;

import capstone.mukjaView.Domain.Restaurant;
import capstone.mukjaView.Domain.User;
import capstone.mukjaView.Dto.CustomOAuth2User;
import capstone.mukjaView.Dto.MapPageRestaurantResponse;
import capstone.mukjaView.Service.MapService;
import capstone.mukjaView.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;
    private final UserService userService;
    @GetMapping("/api/v1/restaurants/by-coordinates")
    @Operation(summary = "find Restaurants by coordinates and page(default 0)")
    public ResponseEntity<List<MapPageRestaurantResponse>> returnRestaurantsInMap(
            @RequestParam double min_lat,
            @RequestParam double max_lat,
            @RequestParam double min_long,
            @RequestParam double max_long,
            @RequestParam(defaultValue = "0") int page) {

        String username;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            username = oAuth2User.getUsername();
        }
        else
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        User user = userService.getUser(username);
        if (user == null)
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

        try {
            Page<Restaurant> pageRestaurants = mapService.getRestaurantsBySizeAndPage(page, min_lat, max_lat, min_long, max_long);
            List<Restaurant> restaurants = pageRestaurants.getContent();
            List<MapPageRestaurantResponse> rtn = restaurants.stream()
                    .map(o -> new MapPageRestaurantResponse(o, user))
                    .collect(Collectors.toList());
            if (!rtn.isEmpty())
                System.out.println("check : " + rtn.get(0).getRestaurantId());
            return new ResponseEntity<>(rtn, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/v1/restaurants/by-tag")
    @Operation(summary = "find Restaurants by tag and page")
    public ResponseEntity<List<MapPageRestaurantResponse>> returnRestaurantByTag(
            @RequestParam String tag,
            @RequestParam(defaultValue = "0") int page) {

        String username;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            username = oAuth2User.getUsername();
        }
        else
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        User user = userService.getUser(username);
        if (user == null)
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

        try {
            List<Restaurant> restaurants = mapService.getRestaurantsByTagAndPage(page, tag);
            List<MapPageRestaurantResponse> rtn = restaurants.stream()
                    .map(o -> new MapPageRestaurantResponse(o, user))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(rtn, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/v1/restaurants/by-keyword")
    @Operation(summary = "find Restaurants by restaurant name and page. If restaurant's name contains keyword, this api returns restaurant.")
    public  ResponseEntity<List<MapPageRestaurantResponse>> returnRestaurantsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page) {

        String username;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) auth.getPrincipal();
            username = oAuth2User.getUsername();
        }
        else
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        User user = userService.getUser(username);
        if (user == null)
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

        try {
            List<Restaurant> restaurants = mapService.getRestaurantsByNameAndPage(page, name);
            List<MapPageRestaurantResponse> rtn = restaurants.stream()
                    .map(o -> new MapPageRestaurantResponse(o, user))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(rtn, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
