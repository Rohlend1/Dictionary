import React, { useEffect, useState } from 'react';
import axios from 'axios';
import AddDictWords from './AddDictWords';
import Modal from './Modal'
import Navbar from './Navbar';
import CreateDict from './CreateDict';
const UserPage = () => {
    const link = "http://localhost:8080"
    const [dictionaries, setDictionaries] = useState([]);
    const [showStateS,setShowStateS] = useState(false)
    const [showStateС,setShowStateС] = useState(false)
    const [user,setUser] = useState(undefined)
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`
    
    const fetchUser = async () => {
        try {
            const response = await axios.get(`${link}/me`,{headers:{
            'Authorization':Authorization
        }
        }); 
            setUser(response.data);
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
            setDictionaries(response.data);
            console.log(dictionaries)
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        }
    };

    useEffect(() => {
        fetchUser();
        fetchDictionaries();
    }, []);
    return (

        <div className="container">
            {user ? (
                        <div className="container">
                <Navbar user = {user} setUser={setUser} />
                <Modal active={showStateS} setActive={setShowStateS} children={<AddDictWords/>}/>
                <Modal active={showStateС} setActive={setShowStateС} children={<CreateDict/>}/>
                {dictionaries.words && dictionaries.words.length > 0 ? (
                    <div className="profile-dictionary-section">
                    <h2 className="dictionary-name">{dictionaries.name} 
                    <button className='button' onClick={()=>setShowStateS(true)}>Open</button></h2>
                        <div className="profile-word-list">
                            {dictionaries.words.map((word) => (
                                <div className="word-item-dict-container" onClick={()=>(console.log(word.value,word.value))}>
                                <div key={word.value} className="word-item-dict">
                                    <div className="word-value-dict font-title">{word.value}</div>
                                    <div className="word-value-dict">{word.translate}</div>
                                </div>
                                </div>
                            ))}
                        </div>
                                                        </div>
                ):dictionaries.length === 0 ? (
                        <button className='button' onClick={()=>setShowStateС(true)}>Создайте словарь </button>
    ):(
              <div className="profile-dictionary-section">
        <h2 className="dictionary-name">{dictionaries.name}</h2>
        <button className='button' onClick={()=>setShowStateS(true)}>Добавьте слова</button>
        </div>
    )}
                
                  </div>
            )
            :(
                <div className="container">
            <Navbar user = {user} setUser={setUser} />
            </div>
            )}
        </div>
    );
};

export default UserPage;
