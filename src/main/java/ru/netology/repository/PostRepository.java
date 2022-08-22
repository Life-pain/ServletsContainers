package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Stub
public class PostRepository {
    private ConcurrentHashMap<Long, Post> postsList = new ConcurrentHashMap<>();
    private Long postCounter = 0L;

    public List<Post> all() {
        if (postsList.isEmpty()) throw new NotFoundException("Постов нет");
        return List.copyOf(postsList.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(postsList.get(id));
    }

    public Post save(Post post) {
        long id = post.getId();
        if (id == 0) {
            synchronized (postCounter) {
                post.setId(++postCounter);
                postsList.put(postCounter, post);
            }
        } else {
            if (postsList.containsKey(id)) postsList.get(id).setContent(post.getContent());
            else {
                post.setId(0);
                save(post);
            }
        }
        return post;
    }

    public void removeById(long id) {
        if (postsList.remove(id) == null) throw new NotFoundException("Пост с таким id не найден");
    }
}
