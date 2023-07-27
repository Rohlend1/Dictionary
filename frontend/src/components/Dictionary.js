import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faClipboard,faCheck,faXmark, faArrowLeft} from '@fortawesome/free-solid-svg-icons'
const Dictionary = ({dictionaries,setDictionaries}) => {

    const link = process.env.REACT_APP_LINK
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`
    const [showStateS,setShowStateS] = useState(false)
    const [showStateС,setShowStateС] = useState(false)
    const [showStateE,setShowStateE] = useState(false)
    const [user,setUser] = useState(undefined)
    const [isLoading,setIsLoading] = useState(false)
    const [showAlert,setAlert] = useState(false)
    const [contents,setContents] = useState()
    const [dictName, setDictname] = useState('Loading...')
    const [changeState,setChangeState] = useState(false)

    const sendDictName = async () => {
        axios.patch(`${link}/dict`,{newName:dictName},{headers:{
        'Authorization':Authorization
    }
    }).catch((error)=>{
        console.error('Ошибка при получении данных словарей:', error)
        if(error){
            alert(error)
        window.location.reload()
        }
    }

    ).finally(()=>{
        setShowStateE(false)
        setDictionaries({...dictionaries,name:dictName})
    })
    
};


    useEffect(() => {
    }, []);

    return (

        <div className="container">
                {dictionaries.words && dictionaries.words.length > 0 ? (
                    <div className="profile-dictionary-section shadow">
                    <div style={{display:"flex",flexDirection:"row",justifyContent:"center",margin:"20px"}}>
                    <div className='search'>
        <label className="search-bytranslate-container">
        <input type='checkbox' className="search-bytranslate" disabled={`${changeState ? "disabled" : ""}`} defaultChecked={true} onChange={()=> {sendDictName();setChangeState(true)}}/>
        {changeState ? (
                <div className='checkbox-label'><FontAwesomeIcon icon={faCheck} /></div>
            ):(
                <div className='checkbox-label'><FontAwesomeIcon icon={faXmark}/></div>
            )}
        <span className={`checkbox ${changeState ? "checkbox--active" : ""}`} aria-hidden="true"/>
         </label>
         <input className='search-input'value={dictName} onChange={(event)=> {setDictname(event.target.value);setChangeState(false)}}></input>

            </div>
            <div className='name-change-dict-close'onClick={(e)=>{setShowStateE(false);e.preventDefault()}}><FontAwesomeIcon icon={faXmark}/></div>
            </div>
                    <h2 className="dictionary-name" onClick={(e)=>{setShowStateE(true);e.preventDefault()}}>{dictionaries.name} </h2>
                        <div className="profile-word-list">
                            {dictionaries.words.map((word) => (
                                <div className="word-item-dict-container" key={word.value} onClick={() => {
                                    navigator.clipboard.writeText(word.value + " " + word.translate)
                                    setContents(<div className='alert-contents'>
                                        <FontAwesomeIcon icon={faClipboard} />
                                        <FontAwesomeIcon icon={faArrowLeft} />
                                        {word.value}
                                        </div>)
                                    setAlert(true)
                                }
                                    }>
                                <div key={word.value} className="word-item-dict">
                                    <div className="word-value-dict font-title">{word.value}</div>
                                    <div className="word-value-dict">{word.translate}</div>
                                </div>
                                </div>
                            ))}
                        </div>
                                                        </div>
                )(
    ):(
              <div className="profile-dictionary-section profile-dictionary-noDictionary shadow">
        <h2 className="dictionary-name">{dictionaries.name}</h2>
        <h1>Нет слов</h1>
        </div>
    )}
                
                  </div>
    );
};

export default Dictionary;
