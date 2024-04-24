package peakspace.service;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.response.*;

import java.util.List;

public interface UserService {

    SimpleResponse forgot(String email) throws MessagingException;

    SimpleResponse randomCode(int codeRequest) throws BadRequestException, MessagingException;

    UpdatePasswordResponse updatePassword(PasswordRequest passwordRequest) throws MessagingException;

    SimpleResponse sendFriends(Long foundUserId,String nameChapter);

    List<SearchResponse> searchFriends(String sample, String keyWord) throws MessagingException;

    SimpleResponse createChapter(ChapterRequest chapterRequest);

    List<SearchHashtagsResponse> searchHashtags(String keyWord);

    List<SearchResponse> searchMyFriends(Long chapterId, String userName);

    ProfileFriendsResponse findFriendsProfile(Long foundUserId);
}
