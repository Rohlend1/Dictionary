import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSpinner,faArrowRight,faTrash } from '@fortawesome/free-solid-svg-icons'
const Logout = () => {
    const link = "http://localhost:8080"
    const navigate = useNavigate()
    let Authorization = `Bearer ${localStorage.getItem("jwt")}`
    const handleLogout = async () => {
        try {
            const response = await axios.post(`${link}/logout`,{headers:{
            'Authorization':Authorization
        }
        }); 
         localStorage.clear()
            navigate("/login")
            console.log(response.data)
        } catch (error) {
            console.error('Ошибка при получении данных словарей:', error);
        }
    }
    
    useEffect(() => {
    }, []);
    return (
            <div className="loading">
                <FontAwesomeIcon className='loading-icon' icon={faSpinner} />
                </div>
        
    );
};

export default Logout;
