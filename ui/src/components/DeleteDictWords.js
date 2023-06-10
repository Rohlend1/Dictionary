import React, { useState,useEffect } from 'react';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSpinner,faArrowRight } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from "react-router-dom";
const DeleteDictWords = () => {
    const navigate = useNavigate()
    const link = "http://localhost:8080"
    const [words, setWords] = useState([])
    const [dictWords,setDictWords] = useState([])
    const [dictName, setDictname] = useState(' ')
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`
    const fetchWords = async () => {
        try {
            const response = await axios.get(`${link}/dict/words`,{headers:{
            'Authorization':Authorization
        }
        }); 

                setWords(response.data);
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        }
    };

    const fetchDictionaries = async () => {
        try {
            const response = await axios.get(`${link}/home`,{headers:{
            'Authorization':Authorization
        }
        }); 
            setDictname(response.data.name);
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        }
    };

    
    const sendDictName = async () => {
        console.log({newName:dictName})
        try {
            const response = await axios.patch(`${link}/dict`,{newName:dictName},{headers:{
            'Authorization':Authorization
        }
        });
            console.log(response)
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        }
    };
    

    const sendDictionaties = async () => {
        if (dictWords !== []){
        try {
            const response = await axios.post(`${link}/dict/delete_words`,{words:dictWords},{headers:{
            'Authorization':Authorization
        }
        }); 
            sendDictName()
            window.location.reload();
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        }
    } else {
        return
    }
    };


    const handleAddWord = (word) => {
        let element = words.find(e => e.value === word)
        setDictWords(prevDictWords => [element,...prevDictWords]);
        setWords(prevWords => {
            const updatedWords = prevWords.filter(e => e.value !== word);
            return updatedWords;
          });
      }
      
      const handleDeleteWord = (word) => {
        let element = dictWords.find(e => e.value === word)
        setDictWords(prevDictWords => {
          const updatedDictWords = prevDictWords.filter(e => e.value !== word);
          return updatedDictWords;
        });
        setWords(prevWords => [element,...prevWords]);
      }



    useEffect(() => {
        fetchWords();
        fetchDictionaries()
    }, []);


    return (
        <div className="create-form">
        <div className = "name-change">
        <input type="text"value={dictName} onChange={(event)=> {setDictname(event.target.value)}}></input>
        </div>
        <div className='line'>
        <div className = "block-del">
        {words && words.length > 0 ? (
                <div className="words-list-container-del">
                    <div className="words-list-del">
                        {words.map((word) => (
                            <div key={word.value} className="word-item-del">
                                <button onClick={()=>handleAddWord(word.value)} value={word.value} className="word-value-del-left">{word.value}:{word.translate}</button>
                            </div>
                        ))}
                    </div>
                </div>
            ) : (
                <FontAwesomeIcon icon={faSpinner} />
            )}
            </div>
            <div className='arrow-block'>
            <FontAwesomeIcon icon={faArrowRight} />
            <FontAwesomeIcon icon={faArrowRight} rotation={180} />
            </div>
            <div className = "block-del">
            {dictWords && dictWords.length > 0 ? (
                <div className="words-list-container-del">
                    <div className="words-list-del">
                        {dictWords.map((word) => (
                             <div key={word.value} className="word-item-del">
                                <button key={word.value} onClick={()=>handleDeleteWord(word.value)} className="word-value-del-right">{word.value}:{word.translate}</button>
                                </div>
                        ))}
                    </div>
                </div>
            ) : (
                <div>Удалить слова</div>
            )}
            </div>
            </div>
            <button className = "button center" onClick={sendDictionaties}>Изменить</button>
    </div>
    );
};

export default DeleteDictWords;
