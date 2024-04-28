package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peakspace.entities.Chapter;
import peakspace.repository.UserRepository;
import peakspace.service.ChapterService;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {

    private final UserRepository userRepository;

    @Override
    public Map<Long, String> getAllChaptersByUserId(Long userId) {
        return userRepository.getReferenceById(userId).getChapters().stream().collect(Collectors.toMap(Chapter::getId, Chapter::getGroupName));
    }
}
