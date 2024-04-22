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

    SimpleResponse randomCode(int codeRequest) throws BadRequestException;

    UpdatePasswordResponse updatePassword(PasswordRequest passwordRequest) throws MessagingException;

    SimpleResponse sendFriends(Long foundUserId,String nameChapter);

    List<SearchResponse> searchFriends(Choise sample, String keyWord);

    SimpleResponse createChapter(ChapterRequest chapterRequest);

    List<SearchHashtagsResponse> searchHashtags(Choise sample,String keyWord);

    List<SearchResponse> searchMyFriends(Long chapterId, String userName);

    ProfileFriendsResponse findFriendsProfile(Long foundUserId);

    List<ChapTerResponse> searchChapter(String search);

    SimpleResponse unsubscribeUser(Long chapterId, Long foundUserId);
}
