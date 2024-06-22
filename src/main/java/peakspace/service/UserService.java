package peakspace.service;

import jakarta.mail.MessagingException;
import peakspace.dto.response.AllFriendsResponse;
import peakspace.dto.response.*;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.request.SignInRequest;
import peakspace.dto.request.SignUpRequest;
import peakspace.dto.request.RegisterWithGoogleRequest;
import peakspace.enums.Choise;

import java.util.List;

public interface UserService {

    SimpleResponse emailSender(String email,String link) throws MessagingException;

    SignInResponse createPassword(String uuid, String password, String confirm);

    SimpleResponse sendFriends(Long foundUserId,Long chapterId);

    List<SearchResponse> searchFriends(Choise sample, String keyWord);

    SimpleResponse createChapter(ChapterRequest chapterRequest);

    List<SearchHashtagsResponse> searchHashtags(Choise sample,String keyWord) throws MessagingException;

    ProfileFriendsResponse findFriendsProfile(Long foundUserId);

    List<ChapTerResponse> searchChapter(String search);

    SimpleResponse unsubscribeUser(Long foundUserId);

    List<SubscriptionResponse> getAllSearchUserHistory();

    List<SearchUserResponse> globalSearch(String keyWord);

    List<FriendsResponse> searchAllFriendsByChapter(Long chapterId, String search);

    ResponseWithGoogle verifyToken(String tokenFromGoogle);

    ResponseWithGoogle signUpWithGoogle(RegisterWithGoogleRequest registerWithGoogle);

    String sendConfirmationCode(String email) throws MessagingException;

    SignInResponse signIn(SignInRequest signInRequest) throws MessagingException;
    
    SignUpResponse signUp(SignUpRequest signUpRequest) throws MessagingException;

    SignInResponse confirmToSignUp(int codeInEmail, long id) throws MessagingException;

    List<AllFriendsResponse> getAllFriendsById(Long userId, String userName);

    SimpleResponse saveUserToHistorySearch(Long foundUserId);

    void cancelConfirm(long userId);
}
