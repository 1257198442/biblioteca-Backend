package com.example.demo.domain.service;

import com.example.demo.adapters.rest.dto.AuthorUploadDto;
import com.example.demo.domain.models.Author;
import com.example.demo.domain.persistence.AuthorPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorPersistence authorPersistence;
    private final RandomStringService randomStringService;
    @Autowired
    public AuthorService(AuthorPersistence authorPersistence,
                         RandomStringService randomStringService){
        this.authorPersistence = authorPersistence;
        this.randomStringService = randomStringService;
    }

    public Author create(AuthorUploadDto authorData){
        Author author = new Author();
        BeanUtils.copyProperties(authorData,author);
        author.setAuthorId(randomStringService.generateRandomString(12));
        author.setImgUrl("https://localhost/images/author/user.png");
        return this.authorPersistence.create(author);
    }

    public Author read(String authorId){
        return this.authorPersistence.read(authorId);
    }

    public List<Author> readAll(){
        return this.authorPersistence.readAll();
    }

    public Author getAuthorData(String authorId){
        return this.authorPersistence.getAuthorData(authorId);
    }

}
