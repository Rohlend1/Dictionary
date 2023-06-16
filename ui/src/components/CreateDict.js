import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
const CreateDict = () => {
    const link = "http://localhost:8080"
    const navigate = useNavigate()
    const [dictName, setDictname] = useState(' ')
    let Authorization = `Bearer ${localStorage.getItem("jwt")}` 
    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post(
                `${link}/dict`,{name:dictName},{headers:{
                    'Authorization':Authorization
                }
            }
                );
                window.location.reload()
            console.log(response.data);
        } catch (error) {
            console.error('Ошибка:', error);
        }
    };

    return (
        <div className="create-form">
                <div className="settings-container">
                <div className = "title">Создание словаря</div>
                <div className="settings-group">
                <input type="text" placeholder='Название' onChange={(event)=> {setDictname(event.target.value)}}></input>
                </div>
                <button className = "button" onClick={handleSubmit} >Создать</button>
        </div>
    </div>
    );
};

export default CreateDict;
