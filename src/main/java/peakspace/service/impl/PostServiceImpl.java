package peakspace.service.impl;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import peakspace.entities.User;
import peakspace.repository.UserRepository;

import java.io.IOException;

@Repository
@RequiredArgsConstructor
public class PostServiceImpl {
    private final UserRepository userRepository;
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.getByEmail(email);
    String apiKey = "AIzaSyDvMzOD4I73b-5iUYh-pjaWHFCojuNRV4c"; // Your Google Maps API Key
    GeoApiContext context = new GeoApiContext
            .Builder()
            .apiKey(apiKey)
            .build();

        try {
        GeocodingResult[] results = new GeocodingResult[0];
        try {
            results = GeocodingApi.geocode(context, "Kyrgyzstan").await();
        } catch (ApiException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        LatLng location = results[0].geometry.location;
        System.out.println("Latitude: " + location.lat);
        System.out.println("Longitude: " + location.lng);
        } catch(Exception e) {
        e.printStackTrace();
    }
}
