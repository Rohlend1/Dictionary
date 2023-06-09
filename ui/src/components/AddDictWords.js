import React, { useState,useEffect } from 'react';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSpinner } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from "react-router-dom";
import DeleteDictWords from './DeleteDictWords';
const EditDict = () => {
    const link = "https://8080-rohlend1-dictionary-5jnb5hheiop.ws-eu99.gitpod.io"
    let navigate = useNavigate()
    const [words, setWords] = useState([])
    const [dictWords,setDictWords] = useState([])
    const [search,setSearch] = useState('')
    const [byTranslate,setByTranslate] = useState(false)
    const [actionType,setActionType] = useState(true)

    let Authorization = `Bearer ${localStorage.getItem("jwt")}`
    
        

    const fetchWords = async () => {
        axios.get(`${link}/dict/excluded_words`,{headers:{
            'Authorization':Authorization
        },
        params:{
            page:0,
            items_per_page:1000
        }
    }
    ).then(response => (setWords(response.data))).catch(err => console.error('Ошибка:', err));
};

    const fetchDictWords = async () => {
        try {
            const response = await axios.get(`${link}/dict/words`, { headers: {
                'Authorization': Authorization
            }});
    
            setDictWords(response.data);
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        }
    };
    
    const sendDictionatiesWords = async () => {
        try {
            const response = await axios.post(`${link}/dict/add_words`,{ words: dictWords },
                {
                    headers: {
                        'Authorization': Authorization
                    }
                }
            );
            navigate("/profile")
            console.log(response.data)
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        }
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
      }

      const handleDeleteWord = (word) => {
        let element = dictWords.find(e => e.value === word)
        setDictWords(prevDictWords => {
          const updatedDictWords = prevDictWords.filter(e => e.value !== word);
          return updatedDictWords;
        });
        setWords(prevWords => [...prevWords, element]);
      }
      

      useEffect(() => {
        fetchWords()
        fetchDictWords()
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
                 <div className = "block">
                 {/* {dictWords && dictWords.length > 0 ? (
                     <div className="">
                         <div className="">
                             {dictWords.map((word) => (
                                     <button key={word.value} onClick={()=>handleDeleteWord(word.value)} className="word-value">{word.value}</button>
                             ))}
                         </div>
                     </div>
                 ) : (
                     <div>Добавьте в словарь слова</div>
                 )} */}
                  <button className = "button" onClick={sendDictionatiesWords} >Добавить</button>
                 </div>
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
