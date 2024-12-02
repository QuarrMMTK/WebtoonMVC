package mmtk.projects.theproject.service;

import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.model.Chapter;
import mmtk.projects.theproject.model.Webtoon;
import mmtk.projects.theproject.repository.ChapterRepository;
import mmtk.projects.theproject.repository.WebtoonRepository;
import mmtk.projects.theproject.util.FileUploadUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    private final WebtoonService webtoonService;

    public long getChapterCount(){
        return chapterRepository.count();
    }

    public void saveChapter(Chapter chapter) {
        chapterRepository.save(chapter);
    }
    @Transactional
    public void saveChapter(Long id, String chapterName, Authentication connectedUser, MultipartFile[] files) throws IOException, IOException {
        if (!connectedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Only admins can create webtoons.");
        }
        Chapter newChapter = new Chapter();
        // Retrieve the Webtoon object based on the ID from the URL
        Webtoon webtoon = webtoonService.getWebtoonById(id);
        newChapter.setName(chapterName);
        // Set the Webtoon reference in the Chapter object
        newChapter.setWebtoon(webtoon);
// Set All Images In 1 Folder with ForEach Looping and Set File Names to count:
        String uploadDir = "";
        int count = 1;
        for (MultipartFile file : files) {
            String fileName = count + "-optimized.jpg";
            count++;
            uploadDir = "webtoon/webtoons/" + webtoon.getTitle() + "/" + chapterName + "/" + fileName;
            FileUploadUtil.saveFile(uploadDir, fileName, file);
        }
        // Save Only 1 Path For Whole Chapter
        newChapter.setPath("webtoon-covers/" + webtoon.getTitle() + "/" + chapterName + "/");
        chapterRepository.save(newChapter);
    }

    public List<Chapter> findChaptersByWebtoon(Webtoon webtoon) {
        return chapterRepository.findByWebtoon(webtoon);
    }
}
