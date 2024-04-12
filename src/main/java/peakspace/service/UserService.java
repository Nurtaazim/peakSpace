package peakspace.service;

import peakspace.dto.request.SignUpRequest;
import peakspace.dto.response.RegisterResponse;
import peakspace.dto.response.SignInRequest;
import peakspace.dto.response.SignInResponce;


public interface UserService  {
    public RegisterResponse signUp(SignUpRequest signUpRequest);
    public SignInResponce signIn(SignInRequest signInReqest);
}
