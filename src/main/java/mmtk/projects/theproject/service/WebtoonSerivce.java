package mmtk.projects.theproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.model.User;
import mmtk.projects.theproject.model.Webtoon;
import mmtk.projects.theproject.repository.WebtoonRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Author : Min Myat Thu Kha
 * Created At : 05/11/2024, Nov ,11, 46
 * Project Name : WebtoonMVC
 **/
@Service
@RequiredArgsConstructor
public class WebtoonSerivce {

    private final WebtoonRepository webtoonRepository;

    public List<Webtoon> findAllWebtoons(int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return webtoonRepository.findAll(pageable).getContent(); // This works out of the box.
    }

    public String createWebtoon(Webtoon webtoon, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        if (connectedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return webtoonRepository.save(webtoon).getTitle();
        } else {
            throw new AccessDeniedException("Only admins can create webtoons.");
        }
    }

    public String editWebtoon(long webtoonId, Webtoon webtoon, Authentication connectedUser) {
        if (connectedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            Webtoon existingWebtoon = webtoonRepository.findById(webtoonId)
                    .orElseThrow(() -> new EntityNotFoundException("Webtoon not found"));
            existingWebtoon.setTitle(webtoon.getTitle());
            existingWebtoon.setAuthor(webtoon.getAuthor());
            existingWebtoon.setSynopsis(webtoon.getSynopsis());
            existingWebtoon.setCoverImage(webtoon.getCoverImage());
            existingWebtoon.setModificationDate(LocalDateTime.now());
            Webtoon updatedWebtoon = webtoonRepository.save(existingWebtoon);
            return updatedWebtoon.getTitle();
        } else {
            throw new AccessDeniedException("Only admins can edit webtoons.");
        }
    }

    public String deleteWebtoon(long webtoonId, Authentication connectedUser) {
        if (connectedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            Webtoon existingWebtoon = webtoonRepository.findById(webtoonId)
                    .orElseThrow(() -> new EntityNotFoundException("Webtoon not found"));
            webtoonRepository.delete(existingWebtoon);
            return existingWebtoon.getTitle();
        } else {
            throw new AccessDeniedException("Only admins can delete webtoons.");
        }
    }

    public Long getWebtoonCount() {
        return webtoonRepository.count();
    }
}
