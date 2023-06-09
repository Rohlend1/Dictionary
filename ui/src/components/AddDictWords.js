import React, { useState,useEffect } from 'react';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSpinner } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from "react-router-dom";
import DeleteDictWords from './DeleteDictWords';
import Modal from './Modal';
import SendDictWords from './SendDictWords';
const EditDict = () => {
    const link = "http://localhost:8080"
    let navigate = useNavigate()
    const [words, setWords] = useState([])
    const [dictWords,setDictWords] = useState([])
    const [search,setSearch] = useState('')
    const [byTranslate,setByTranslate] = useState(false)
    const [actionType,setActionType] = useState(true)
    const [showState,setShowState] = useState(false)
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`
    
        

    const fetchWords = async () => {
        axios.get(`${link}/dict/excluded_words`,{headers:{
            'Authorization':Authorization
        },
        params:{
            page:0,
            items_per_page:600
        }
    }
    ).then(response => (setWords(response.data))).catch(err => console.error('Ошибка:', err));
};


    const handleSearch = async () => {  
        if (!search){
            fetchWords()
            return
        }
        console.log(search)
        axios.get(`${link}/words/search`,{headers:{
                    'Authorization':Authorization
                },
                params:{
                    starts_with:search,
                    by_translate:byTranslate
                }
            }
            ).then(response => (setWords(response.data))).catch(err => console.error('Ошибка:', err));
            console.log(words)
            console.log(byTranslate)
        };

    const handleAddWord = (word) => {
        let element = words.find(e => e.value === word)
        setDictWords(prevWords => [...prevWords, element]);
        setWords(prevWords => {
            const updatedWords = prevWords.filter(e => e.value !== word);
            return updatedWords;
          });
          console.log(dictWords)
      }


      useEffect(() => {
        fetchWords()
    }, []);

    useEffect(()=>{
        let Debounce = setTimeout(()=>{
            handleSearch()
        },300)
        console.log(words)
        return () => clearTimeout(Debounce) 
    },[search])


    return (
        <div className="add-words-container">
             <div className = "add-words-top-block">
        <div className = "top-title">Изменение словаря</div>
        <input type='checkbox' defaultChecked={true} onChange={(e)=> {setActionType(e.target.checked)}}/>
        </div>
        {actionType ? (
                    <div className = "add-words-block">
                        <Modal active={showState} setActive={setShowState} children={<SendDictWords words = {dictWords} setWords={setDictWords} allWords = {words} setAllWords = {setWords}/>}/>
                        <button className='button' onClick={()=>setShowState(true)}>{dictWords.length}</button>
        <div className='search'>
        <label className="search-bytranslate-container">
        <input type='checkbox' className='search-bytranslate' defaultChecked={false} onChange={(e)=> {setByTranslate(e.target.checked)}}/>
        {byTranslate ? (
                <div className='checkbox-label'>RUS</div>
            ):(
                <div className='checkbox-label'>ENG</div>
            )}
        <span className={`checkbox ${byTranslate ? "checkbox--active" : ""}`} aria-hidden="true"/>
         </label>
        <input className='search-input' placeholder='Поиск' value={search} onChange={(e) => {setSearch(e.target.value)}}/>
        </div>
             {words && words.length > 0 ? (
                <div>
                {byTranslate ?  (
                     <div className="words-list-container">
                         <div className="words-list">
                             {words.map((word) => (
                                     <button onClick={()=>handleAddWord(word.value)} value={word.value} className="word-value">{word.translate}</button>
                             ))}
                         </div>
                     </div>
                ):(
                    
                    <div className="words-list-container">
                    <div className="words-list">
                        {words.map((word) => (
                                <button onClick={()=>handleAddWord(word.value)} value={word.value} className="word-value">{word.value}</button>
                        ))}
                    </div>
                </div>
                )

                         }
                 </div>
                 ) : (
                     <FontAwesomeIcon icon={faSpinner} />
                 )}
                 </div>
            ):(
                <div>
                <div className = "block">
                <DeleteDictWords/>
            </div>
            </div>
            )}
       
       </div>
    );
};

export default EditDict;
