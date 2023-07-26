import React, { useState,useEffect } from 'react';
import axios from 'axios';
import DeleteDictWords from './DeleteDictWords';
import Modal from './Modal';
import Loader from './Loader';
import SendDictWords from './SendDictWords';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faTrash, faPlus } from '@fortawesome/free-solid-svg-icons'
const EditDict = () => {
    const link = process.env.REACT_APP_LINK
    const [words, setWords] = useState([])
    const [dictWords,setDictWords] = useState([])
    const [search,setSearch] = useState('')
    const [byTranslate,setByTranslate] = useState(false)
    const [actionType,setActionType] = useState(true)
    const [showState,setShowState] = useState(false)
    const [page,setPage] = useState(0)
    const [fetching,setFetching] = useState()
    const [wasSearch,setWasSearch] = useState(false)
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`
    
        

    const fetchWords = async () => {
        axios.get(`${link}/dict/find/excluded-words`,{headers:{
            'Authorization':Authorization
        },
        params:{
            page:page,
            items_per_page:600
        }
    }
    ).then(response => {
        if(wasSearch){
            setWords([])
            setWasSearch(false)
            setWords(response.data)
        } else if (!wasSearch){
        setWords([...words,...response.data])
        setPage(prevState => prevState+1)
        }
    }
    ).catch(err => console.error('Ошибка:', err)).finally(()=>{
        setFetching(false)
    });
};


    const handleSearch = () => {  
        if (!search){
            fetchWords()
            return
        } else {
        axios.get(`${link}/find/words`,{headers:{
                    'Authorization':Authorization
                },
                params:{
                    starts_with:search,
                    by_translate:byTranslate
                }
            }
            ).then(response => {
                setWords(response.data)
                setPage(0)
                setWasSearch(true)
            }).catch(err => console.error('Ошибка:', err));
        }
        };

    const handleAddWord = (word) => {
        let element = words.find(e => e.value === word)
        setDictWords(prevWords => [...prevWords, element]);
        setWords(prevWords => {
            const updatedWords = prevWords.filter(e => e.value !== word);
            return updatedWords;
          });
      }
    
    const scrollHandler = (e) => {
        if (e.target.scrollHeight - (e.target.scrollTop + (window.innerHeight/2))<100) {
            setFetching(true)
        }
    }
    const scroll = () => {
        let wordsList = document.querySelector(".words-list-container")
        if(wordsList){    
        wordsList.addEventListener("scroll",scrollHandler)
    return () => {
        wordsList.removeEventListener("scroll",scrollHandler)
}

    }
}
    
    useEffect(()=>{
        scroll()
    })

      useEffect(() => {
        if(fetching && !search){
        fetchWords()
        } 
    }, [fetching]);

    useEffect(()=>{
        let Debounce = setTimeout(()=>{
            handleSearch()
        },300)
        return () => {
            clearTimeout(Debounce)
        }
    },[search])

    return (
        <div className="add-words-container">
             <div className = "add-words-top-block">
        <div className = "top-title">Изменение словаря</div>
        <label className="search-bytranslate-container">
        <input type='checkbox' className='search-bytranslate' defaultChecked={true} onChange={(e)=> {setActionType(e.target.checked);}}/>
        {actionType ? (
                <div className='checkbox-label'><FontAwesomeIcon icon={faTrash} /></div>
            ):(
                <div className='checkbox-label'><FontAwesomeIcon icon={faPlus} size='lg' /></div>
            )}
        <span className={`checkbox ${actionType ? "checkbox--active edit" : "edit1"}`} aria-hidden="true"/>
         </label>
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
                                        <button onClick={()=>handleAddWord(word.value)} value={word.value} key={word.value}  className="word-value">{word.translate}</button>
                             ))}
                         </div>
                     </div>
                ):(
                    
                    <div className="words-list-container">
                    <div className="words-list">
                        {words.map((word) => (
                                <button onClick={()=>handleAddWord(word.value)} value={word.value} key={word.value} className="word-value">{word.value}</button>
                        ))}
                    </div>
                </div>
                )

                         }
                 </div>
                 ) : (
                    <Loader />
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
