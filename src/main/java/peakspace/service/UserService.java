package peakspace.service;
import jakarta.mail.MessagingException;
import org.apache.coyote.BadRequestException;
import peakspace.dto.request.PasswordRequest;
import peakspace.dto.request.SampleRequest;
import peakspace.dto.response.SearchResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.SubscriptionResponse;
import peakspace.dto.response.UpdatePasswordResponse;
import java.util.List;

public interface UserService {
    SimpleResponse forgot(String email) throws MessagingException;

    SimpleResponse randomCode(int codeRequest) throws BadRequestException;

    UpdatePasswordResponse updatePassword(PasswordRequest passwordRequest) throws MessagingException;

    List<SubscriptionResponse> sendFriends(Long foundUserId);

    List<SearchResponse> searchFriends(String sample, String keyWord);
}
