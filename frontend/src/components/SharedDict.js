import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil,faClipboard,faCheck,faXmark, faArrowLeft} from '@fortawesome/free-solid-svg-icons'
import Navbar from './Navbar';
import Loader from './Loader'
import Alert from './Alert'
import { useNavigate, useParams } from 'react-router-dom';
const UserPage = () => {
    const link = process.env.REACT_APP_LINK
    const navigate = useNavigate()
    const {id} = useParams()
    const [dictionaries, setDictionaries] = useState([]);
    const [showStateE,setShowStateE] = useState(false)
    const [user,setUser] = useState(undefined)
    const [isLoading,setIsLoading] = useState(false)
    const [showAlert,setAlert] = useState(false)
    const [contents,setContents] = useState()
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

    const fetchSharedDictionaries = async () => {
        setIsLoading(true)
        try {
            const response = await axios.get(`${link}/dict/shared/${id}`,{headers:{
            'Authorization':Authorization
        }
        }); 
            setDictionaries(response.data);
            setIsLoading(false)
        } catch (error) {
            console.error(error.code)
        }
    };



    useEffect(() => {
        fetchUser();
        fetchSharedDictionaries();
    }, []);

    if (isLoading) return <Loader />

    return (

        <div className="container">
                <Navbar user = {user} setUser={setUser}>{<div className='alert-contents'><h1>Go back</h1></div>}</Navbar>
                <Alert type="success" isShow={showAlert} setIsShow={setAlert}>
                    {contents}
                </Alert>
                {dictionaries.words && dictionaries.words.length > 0 ? (
                    <div className="profile-dictionary-section shadow">
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
                ):(
              <div className="profile-dictionary-section profile-dictionary-noDictionary shadow">
        <h2 className="dictionary-name">{dictionaries.name}</h2>
        <h1>Нет слов</h1>
        </div>
    )}
                
                  </div>
    );
};

export default UserPage;
