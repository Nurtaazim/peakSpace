package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peakspace.dto.response.ChapTerResponse;
import peakspace.entities.Chapter;
import peakspace.repository.UserRepository;
import peakspace.service.ChapterService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final UserRepository userRepository;

    @Override
    public List<ChapTerResponse> getAllChaptersByUserId(Long userId) {
        List<Chapter> chapters = userRepository.getReferenceById(userId).getChapters();
        List<ChapTerResponse> chapTerResponses = new ArrayList<>();
        if (chapters.isEmpty()) return new ArrayList<>();
        for (Chapter chapter : chapters) {
            ChapTerResponse chapTerResponse = new ChapTerResponse(chapter.getId(), chapter.getGroupName());
            chapTerResponses.add(chapTerResponse);
        }
        return chapTerResponses;
    }
}
