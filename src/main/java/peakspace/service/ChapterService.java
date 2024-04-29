package peakspace.service;

import java.util.Map;

public interface ChapterService {
    Map<Long, String> getAllChaptersByUserId(Long userId);
}
