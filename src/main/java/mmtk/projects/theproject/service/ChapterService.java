package mmtk.projects.theproject.service;

import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.repository.ChapterRepository;
import mmtk.projects.theproject.repository.WebtoonRepository;
import org.springframework.stereotype.Service;

/**
 * Author : Min Myat Thu Kha
 * Created At : 29/11/2024, Nov , 15:40
 * Project Name : WebtoonMVC
 **/
@Service
@RequiredArgsConstructor
public class ChapterService {

    private final WebtoonRepository webtoonRepository;
    private final ChapterRepository chapterRepository;

    public long getChapterCount(){
        return chapterRepository.count();
    }
}
