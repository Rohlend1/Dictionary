import React, { useEffect, useState } from 'react';
import axios from 'axios';
import AddDictWords from './AddDictWords';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil} from '@fortawesome/free-solid-svg-icons'
import Modal from './Modal'
import Navbar from './Navbar';
import CreateDict from './CreateDict';
import Loader from './Loader'
import { useNavigate } from 'react-router-dom';
const UserPage = () => {
    const link = "http://localhost:8080"
    const navigate = useNavigate()
    const [dictionaries, setDictionaries] = useState([]);
    const [showStateS,setShowStateS] = useState(false)
    const [showStateС,setShowStateС] = useState(false)
    const [user,setUser] = useState(undefined)
    const [isLoading,setIsLoading] = useState(false)
    const [error,setErrror] = useState(undefined)
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
            setErrror(error)
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
            console.log(dictionaries)
            setIsLoading(false)
        } catch (error) {
            setErrror(error)
        }
    };

    useEffect(() => {
        fetchUser();
        fetchDictionaries();
    }, []);

    if (isLoading) return <Loader />

    return (

        <div className="container">
                <Navbar user = {user} setUser={setUser} />
                <Modal active={showStateS} setActive={setShowStateS} children={<AddDictWords/>}/>
                <Modal active={showStateС} setActive={setShowStateС} children={<CreateDict/>}/>
                {dictionaries.words && dictionaries.words.length > 0 ? (
                    <div className="profile-dictionary-section shadow">
                    <button className='button' onClick={()=>setShowStateS(true)}><FontAwesomeIcon icon={faPencil} /></button>
                    <h2 className="dictionary-name">{dictionaries.name} </h2>
                        <div className="profile-word-list">
                            {dictionaries.words.map((word) => (
                                <div className="word-item-dict-container" key={word.value} onClick={()=>(console.log(word.value,word.value))}>
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
              <div className="profile-dictionary-section profile-dictionary-noDictionary">
        <h2 className="dictionary-name">{dictionaries.name}</h2>
        <button className='button' onClick={()=>setShowStateS(true)}>Добавьте слова</button>
        </div>
    )}
                
                  </div>
    );
};

export default UserPage;
