package peakspace.service;

import peakspace.dto.response.ChapTerResponse;

import java.util.List;
import java.util.Map;

public interface ChapterService {
    List<ChapTerResponse> getAllChaptersByUserId(Long userId);
}
