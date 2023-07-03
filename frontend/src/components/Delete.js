import React, { useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
const Delete = () => {
    const link = process.env.REACT_APP_LINK
    const navigate = useNavigate()
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`
    const handleLogout = async () => {
        try {
            const response = await axios.delete(`${link}/delete`,{headers:{
            'Authorization':Authorization
        }
        }); 
         localStorage.clear()
            navigate("/register")
            console.log(response.data)
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        }
    }
    
    useEffect(() => {
    }, []);
    return (
            <div className="logout">
                <div className='logout-title'>Удалить аккаунт?</div>
                <button className='button button-alert' onClick={()=>{handleLogout()}}>Delete account</button>
                </div>
        
    );
};

export default Delete;
