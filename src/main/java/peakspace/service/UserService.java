package peakspace.service;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.response.*;
import peakspace.enums.Choise;

import java.util.List;

public interface UserService {

    SimpleResponse forgot(String email) throws MessagingException;

    SimpleResponse randomCode(int codeRequest,String email) throws BadRequestException;

    UpdatePasswordResponse updatePassword(PasswordRequest passwordRequest,String email);

    SimpleResponse sendFriends(Long foundUserId,Long chapterId);

    List<SearchResponse> searchFriends(Choise sample, String keyWord);

    SimpleResponse createChapter(ChapterRequest chapterRequest);

    List<SearchHashtagsResponse> searchHashtags(Choise sample,String keyWord) throws MessagingException;

    List<SearchResponse> searchMyFriends(Long chapterId, String userName);

    ProfileFriendsResponse findFriendsProfile(Long foundUserId);

    List<ChapTerResponse> searchChapter(String search);

    SimpleResponse unsubscribeUser(Long chapterId, Long foundUserId);

    List<SubscriptionResponse> getAllSearchUserHistory();
}
