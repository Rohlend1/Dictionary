package com.example.dictionary.services;

import com.example.dictionary.entities.Dictionary;
import com.example.dictionary.entities.User;
import com.example.dictionary.entities.Word;
import com.example.dictionary.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final DictionaryService dictionaryService;
    private final UserRepository userRepository;

    public UserService(DictionaryService dictionaryService, UserRepository userRepository) {
        this.dictionaryService = dictionaryService;
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(int userId){
        return userRepository.findById(userId).orElse(null);
    }

    public List<Word> getAllWordsByUser(int userId){
        Dictionary dictionary = getUserById(userId).getDictionary();
        return dictionary.getWords();
    }

    public Dictionary getDictionaryByUserId(int personId){
        return getUserById(personId).getDictionary();
    }

    @Transactional(readOnly = false)
    public void saveUser(User user){
        userRepository.save(user);
    }

    @Transactional(readOnly = false)
    public void removeUser(User user){
        userRepository.delete(user);
    }

    @Transactional(readOnly = false)
    public void renameUser(String newUsername,int userId){
        User user = getUserById(userId);
        user.setUsername(newUsername);
    }
}
