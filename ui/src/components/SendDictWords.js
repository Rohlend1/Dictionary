import React, { useState,useEffect } from 'react';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSpinner } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from "react-router-dom";
const SendDictWords = ({words,setWords,allwords,setAllWords}) => {
    const navigate = useNavigate()
    const link = "http://localhost:8080"
    // const [words, setWords] = useState("")
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`

    
    const sendDictionatiesWords = async () => {

        try {
            const response = await axios.post(`${link}/dict/add_words`,{ words: words },
                {
                    headers: {
                        'Authorization': Authorization
                    }
                }
            );
            window.location.reload();
            console.log(response.data)
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        // }
    };
}

      const handleDeleteWord = (word) => {
        let element = words.find(e => e.value === word)
        setWords(prevDictWords => {
          const updatedDictWords = prevDictWords.filter(e => e.value !== word);
          return updatedDictWords;
        });
        setAllWords(prevWords => [element,...prevWords]);
      }
      
    useEffect(() => {
    }, []);


    return (
        <div className="send-words-container">
        <div className='line'>
        <div className = "block">
        {words && words.length > 0 ? (
                <div className="words-list-container">
                    <div className="words-list-send">
                        {words.map((word) => (
                            <div key={word.value} className="word-item">
                                <button  onClick={()=>handleDeleteWord(word.value)} value={word.value} className="word-value-send" >
                                    <div>{word.value}</div>
                                    <div>{word.translate}</div>
                                    </button>
                            </div>
                        ))}
                    </div>
                </div>
           ) : (
                <FontAwesomeIcon icon={faSpinner} />
            )}
            </div>
            </div>
            <div className = "bottom-line">
            <button className = "button button-bottom" onClick={sendDictionatiesWords}>Изменить</button>
            </div>
    </div>
    );
};

export default SendDictWords;
