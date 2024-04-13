package peakspace.service;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import peakspace.dto.request.ChapterRequest;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.response.SearchHashtagsResponse;
import peakspace.dto.response.SearchResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.UpdatePasswordResponse;
import java.util.List;

public interface UserService {
    SimpleResponse forgot(String email) throws MessagingException;

    SimpleResponse randomCode(int codeRequest) throws BadRequestException;

    UpdatePasswordResponse updatePassword(PasswordRequest passwordRequest) throws MessagingException;

    SimpleResponse sendFriends(Long foundUserId,String nameChapter);

    List<SearchResponse> searchFriends(String sample, String keyWord);

    SimpleResponse createChapter(ChapterRequest chapterRequest);

    List<SearchHashtagsResponse> searchHashtags(String keyWord);

    List<SearchResponse> searchMyFriends(String section);
}
