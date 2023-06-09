import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import AddDictWords from './AddDictWords';
import Modal from './Modal'
import Navbar from './Navbar';
const UserPage = () => {
    const link = "http://localhost:8080"
    const navigate = useNavigate()
    const [dictionaries, setDictionaries] = useState([]);
    const [showStateS,setShowStateS] = useState(false)
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`
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
        fetchDictionaries();
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    return (

        <div className="container">
            <Navbar />
            <h1 className="title">Словари пользователя</h1>
            <Modal active={showStateS} setActive={setShowStateS} children={<AddDictWords/>}/>
            {dictionaries.words && dictionaries.words.length > 0 ? (
                <div className="profile-dictionary-section" onClick={()=>setShowStateS(true)}>
                <h2 className="dictionary-name" onClick={()=>setShowStateS(true)}>{dictionaries.name}</h2>
                    <div className="profile-word-list">
                        {dictionaries.words.map((word) => (
                            <div className='word-item-dict-container'>
                            <div key={word.value} className="word-item-dict">
                                <div className="word-value-dict">{word.value}</div>
                                <div className="word-value-dict">{word.translate}</div>
                            </div>
                            </div>
                        ))}
                    </div>
                </div>
            ) : (
                <div  onClick={()=>setShowStateS(true)}>
                    <i>Добавьте слова</i>
                </div>
            )}
            {dictionaries.length === 0 ?(
                                <button className='button' onClick={()=>navigate("/dict/create")}>Создайте словарь </button>
            ):(
                <></>
            )}
        </div>
    );
};

export default UserPage;
