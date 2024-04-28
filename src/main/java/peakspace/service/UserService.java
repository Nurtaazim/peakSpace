package peakspace.service;

import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import peakspace.dto.response.ChapTerResponse;
import peakspace.dto.response.FriendsPageResponse;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.request.RegisterWithGoogleRequest;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.SubscriptionResponse;
import peakspace.dto.response.ProfileFriendsResponse;
import peakspace.dto.response.UpdatePasswordResponse;
import peakspace.dto.response.SearchResponse;
import peakspace.dto.response.SearchHashtagsResponse;
import peakspace.dto.response.ResponseWithGoogle;
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

    FriendsPageResponse searchAllFriendsByChapter(Long userId, Long chapterId, String search);

    ResponseWithGoogle verifyToken(String tokenFromGoogle);

    ResponseWithGoogle signUpWithGoogle(RegisterWithGoogleRequest registerWithGoogle);

    String sendConfirmationCode(String email) throws MessagingException;

}
