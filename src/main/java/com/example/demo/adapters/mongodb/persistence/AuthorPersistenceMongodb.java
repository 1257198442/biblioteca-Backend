package com.example.demo.adapters.mongodb.persistence;

import com.example.demo.adapters.mongodb.daos.AuthorRepository;
import com.example.demo.adapters.mongodb.entities.AuthorEntity;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Author;
import com.example.demo.domain.persistence.AuthorPersistence;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AuthorPersistenceMongodb implements AuthorPersistence {
    private final AuthorRepository authorDao;
    @Autowired
    public AuthorPersistenceMongodb(AuthorRepository authorDao){
        this.authorDao = authorDao;
    }

    @Override
    public Author create(Author author) {
        return this.authorDao.save(new AuthorEntity(author)).toAuthor();
    }

    @Override
    public Author read(String authorId) {
        return this.authorDao.readByAuthorId(authorId)
                .orElseThrow(()->new NotFoundException("Author: "+authorId+" is not Fount"))
                .toAuthor();
    }

    @Override
    public List<Author> readAll() {
        return this.authorDao
                .findAll()
                .stream()
                .map(AuthorEntity::toAuthor)
                .collect(Collectors.toList());
    }

    @Override
    public Author getAuthorData(String authorId) {
        Optional<AuthorEntity> authorData = this.authorDao.readByAuthorId(authorId);
        return authorData.map(AuthorEntity::toAuthor).orElse(null);
    }

    @Override
    public Author update(Author author) {
        AuthorEntity authorEntity = this.authorDao.readByAuthorId(author.getAuthorId())
                .orElseThrow(()->new NotFoundException("Author: "+author.getAuthorId()+" is not Fount"));
        BeanUtils.copyProperties(author,authorEntity);
        return this.authorDao.save(authorEntity).toAuthor();
    }

    @Override
    public Author delete(String authorId) {
        return this.authorDao
                .deleteByAuthorId(authorId)
                .orElseThrow(()->new NotFoundException("Author: "+authorId+" is not Fount"))
                .toAuthor();
    }

}
