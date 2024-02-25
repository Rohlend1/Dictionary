import React, { useState,useEffect } from 'react';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faArrowRight,faTrash } from '@fortawesome/free-solid-svg-icons'
import Loader from './Loader';
const DeleteDictWords = () => {
    const link = process.env.REACT_APP_LINK
    const [words, setWords] = useState([])
    const [dictWords,setDictWords] = useState([])
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
    
    const sendDictionaties = async () => {
        if (dictWords !== []){
        try {
            axios.post(`${link}/dict/delete/words`,{words:dictWords},{headers:{
            'Authorization':Authorization
        }
        }); 
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
    }, []);

    return (
        <div className="create-form">
        <div className='line'>
        <div className = "block-del">
        {words && words.length > 0 ? (
                <div className="words-list-container-del">
                    <div className="words-list-del">
                        {words.map((word) => (
                            <div key={word.value} className="word-item-del">
                                <button onClick={()=>handleAddWord(word.value)} value={word.value} onMouseEnter={(e)=>{e.target.innerHTML= word.translate}} onMouseLeave={(e)=>{e.target.innerHTML= word.value}} className="word-value-del-left">{word.value}</button>
                            </div>
                        ))}
                    </div>
                </div>
            ) : (
                <Loader />
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
                                <button key={word.value} onClick={()=>handleDeleteWord(word.value)} onMouseEnter={(e)=>{e.target.innerHTML= word.translate}} onMouseLeave={(e)=>{e.target.innerHTML= word.value}} className="word-value-del-right">{word.value}</button>
                                </div>
                        ))}
                     </div>
                    </div>
            ) : (
                <div className = "words-list-container-del">
                    <div className="del-trash-icon">
                <FontAwesomeIcon icon={faTrash} size='10x'/>
                </div>
                </div>
            )}
            </div>
            </div>
            <div className = "bottom-line">
            <button className = "button button-bottom" onClick={sendDictionaties}>Удалить слова</button>
            </div>
    </div>
    );
};

export default DeleteDictWords;
