import React, { useEffect, useState } from 'react';
import axios from 'axios';
import AddDictWords from './AddDictWords';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil,faClipboard,faCheck,faXmark, faArrowLeft} from '@fortawesome/free-solid-svg-icons'
import Modal from './Modal'
import Navbar from './Navbar';
import CreateDict from './CreateDict';
import Loader from './Loader'
import Alert from './Alert'
import { useNavigate } from 'react-router-dom';
import Quizlet from "./Quizlet";
import quizlet from "./Quizlet";
const UserPage = () => {
    const link = process.env.REACT_APP_LINK
    const navigate = useNavigate()
    const [dictionaries, setDictionaries] = useState([]);
    const [showStateS,setShowStateS] = useState(false)
    const [showStateС,setShowStateС] = useState(false)
    const [showStateE,setShowStateE] = useState(false)
    const [showStateQ,setShowStateQ] = useState(false)
    const [user,setUser] = useState(undefined)
    const [isLoading,setIsLoading] = useState(false)
    const [showAlert,setAlert] = useState(false)
    const [contents,setContents] = useState()
    const [dictName, setDictname] = useState('Loading...')
    const [changeState,setChangeState] = useState(false)
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`
    const fetchUser = async () => {
        setIsLoading(true)
        try {
            const response = await axios.get(`${link}/me`,{headers:{
            'Authorization':Authorization
        }
        }); 
            setUser(response.data);
        } catch (error) {
            console.error(error.code)
             if (error){
                navigate("/login")
            }
        }
    };

    const fetchDictionaries = async () => {
        setIsLoading(true)
        try {
            const response = await axios.get(`${link}/home`,{headers:{
            'Authorization':Authorization
        }
        }); 
            setDictionaries(response.data);
            setDictname(response.data.name);
            setIsLoading(false)
        } catch (error) {
            console.error(error.code)
        }
    };

    const shareDict = async () => {
        try {
            const response = await axios.get(`${link}/dict/share`,{headers:{
            'Authorization':Authorization
        }
        }); 
            let thisLink = window.location.href
            setContents(<div className='alert-contents'>
            {`${thisLink.substring(0,thisLink.length-7)}dict/shared/${response.data}`}
            </div>)
            navigator.clipboard.writeText(`${thisLink.substring(0,thisLink.length-7)}dict/shared/${response.data}`)
        setAlert(true)
        } catch (error) {
            console.error(error.code)
        }
    };


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
        fetchUser();
        fetchDictionaries();
    }, []);


    return (

        <div className="container">
                <Navbar user = {user} setUser={setUser} />
                <Alert type="success" isShow={showAlert} setIsShow={setAlert}>
                    {contents}
                </Alert>
                <Modal active={showStateS} setActive={setShowStateS} children={<AddDictWords/>}/>
                <Modal active={showStateС} setActive={setShowStateС} children={<CreateDict/>}/>
                <Modal children={<Quizlet/>} setActive={setShowStateQ} active={showStateQ}/>
                <button className='button' onClick={()=>setShowStateQ(true)}>Сыграть в квиз</button>
                {dictionaries.words && dictionaries.words.length > 0 ? (
                    <div className="profile-dictionary-section shadow">
                    <button className='button' onClick={()=>setShowStateS(true)}><FontAwesomeIcon icon={faPencil} /></button>
                    <button className='button' style={{marginLeft:"78%"}} onClick={shareDict}>Share</button>
                {showStateE ? ( 
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
                ):( 
                    <h2 className="dictionary-name" onClick={(e)=>{setShowStateE(true);e.preventDefault()}}>{dictionaries.name} </h2>
                )
            }

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
                ):dictionaries.length === 0 ? (
                        <button className='button' onClick={()=>setShowStateС(true)}>Создайте словарь</button>
    ):(
              <div className="profile-dictionary-section profile-dictionary-noDictionary shadow">
        <h2 className="dictionary-name">{dictionaries.name}</h2>
        <button className='button' onClick={()=>setShowStateS(true)}>Добавьте слова</button>
        </div>
    )}

                  </div>
    );
};

export default UserPage;
