package com.example.dictionary.services;

import com.example.dictionary.dto.DictionaryMetaDTO;
import com.example.dictionary.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShareService {

    //Todo перенести в параметры
    private final String shareUrlPrefix = "http://localhost:8080/dict/shared/";
    private final JwtUtil jwtUtil;
    private String secret = "yFC3O1rQIQCkaGebibu3Yh4AGDPoCssS";

    @Autowired
    public ShareService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String createSharingLink(DictionaryMetaDTO dictionary){
        StringBuilder sb = new StringBuilder(shareUrlPrefix);
        long hashedOwnerId = getHashedOwnerId(dictionary.getOwner());

        sb.append(hashedOwnerId);
        sb.append("/");
        sb.append(jwtUtil.generateSharingToken(dictionary.getId()));

        return sb.toString();
    }

    private int getHashedOwnerId(Long ownerId){
        return (secret + ownerId).hashCode();
    }

    public boolean checkIsAllowed(Long hashedOwnerId, Long ownerId){
        return hashedOwnerId == getHashedOwnerId(ownerId);
    }
}
