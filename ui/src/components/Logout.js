import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from "react-router-dom";
const Logout = () => {
    const link = "https://8080-rohlend1-dictionary-5jnb5hheiop.ws-eu99.gitpod.io"
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
            <div className="logout">
                <div className='logout-title'>Are you sure?</div>
                <button className='button' onClick={()=>{handleLogout()}}>Logout</button>
                </div>
        
    );
};

export default Logout;
