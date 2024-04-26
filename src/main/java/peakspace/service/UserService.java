package peakspace.service;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.request.SignInRequest;
import peakspace.dto.request.SignUpRequest;
import peakspace.dto.response.*;
import peakspace.enums.Choise;

import java.util.List;

public interface UserService {

    SimpleResponse forgot(String email) throws MessagingException;

    SimpleResponse randomCode(int codeRequest) throws BadRequestException, MessagingException;

    UpdatePasswordResponse updatePassword(PasswordRequest passwordRequest) throws MessagingException;

    SimpleResponse sendFriends(Long foundUserId,Long chapterId);

    List<SearchResponse> searchFriends(Choise sample, String keyWord);

    SimpleResponse createChapter(ChapterRequest chapterRequest);

    List<SearchHashtagsResponse> searchHashtags(Choise sample,String keyWord) throws MessagingException;

    List<SearchResponse> searchMyFriends(Long chapterId, String userName);

    ProfileFriendsResponse findFriendsProfile(Long foundUserId);

    List<ChapTerResponse> searchChapter(String search);

    SimpleResponse unsubscribeUser(Long chapterId, Long foundUserId);

    List<SubscriptionResponse> getAllSearchUserHistory();

    SignInResponse signIn(SignInRequest signInRequest) throws MessagingException;

    String signUp(SignUpRequest signUpRequest) throws MessagingException;

    SimpleResponse confirmToSignUp(int codeInEmail, long id) throws MessagingException;
}
